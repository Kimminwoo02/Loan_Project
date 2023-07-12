package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.BalanceDTO.*;
import com.fastcampus.loan.dto.RepaymentDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import com.fastcampus.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fastcampus.loan.dto.BalanceDTO.*;
import static com.fastcampus.loan.dto.BalanceDTO.RepaymentRequest.*;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService{
    private final RepaymentRepository repaymentRepository;
    private final EntryRepository entryRepository;
    private final ApplicationRepository applicationRepository;
    private final BalanceService balanceService;
    private final ModelMapper modelMapper;
    @Override
    public RepaymentDTO.Response create(Long applicationId, RepaymentDTO.Request request) {
        // validation
        // 1. 계약을 완료한 신청정보
        // 2. 집행이 되어있어야 함
        if (!isRepayableApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);
        repaymentRepository.save(repayment);

        //잔고
        // balance :500 -> 100 = 400
        Response updatedBalance = balanceService.repaymentUpdate(applicationId, builder()
                .repaymentAmount(request.getRepaymentAmount())
                .type(RepaymentType.REMOVE)
                .build());

        Response response = modelMapper.map(repayment, Response.class);
        response.setBalance(updatedBalance.getBalance());

        return null;

    }

    @Override
    public List<RepaymentDTO.ListResponse> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);

        return repayments.stream().map(r->modelMapper.map(r, RepaymentDTO.ListResponse.class)).collect(Collectors.toList());

    }

    @Override
    public RepaymentDTO.UpdateResponse update(Long repaymentId, RepaymentDTO.Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        // 500 - 100 = 400
        // 400 + 100 = 500

        // 500 = 200 = 300

        balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .repaymentAmount(beforeRepaymentAmount)
                .type(RepaymentType.ADD)
                .build());
        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        Response updatedBalance = balanceService.repaymentUpdate(applicationId, builder()
                .repaymentAmount(request.getRepaymentAmount())
                .type(RepaymentType.REMOVE).build());

        return RepaymentDTO.UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(request.getRepaymentAmount())
                .balance(updatedBalance.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(repayment.getUpdatedAt())
                .build();

    }

    @Override
    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,RepaymentRequest.builder()
                .repaymentAmount(removeRepaymentAmount)
                .type(RepaymentType.ADD)
                .build());

        repayment.setIsDeleted(true);
        repaymentRepository.save(repayment);
    }

    private boolean isRepayableApplication(Long applicationId){
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()){
            return false;
        }

        if (application.get().getContractedAt() == null){
            return false;
        }
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);
        return entry.isPresent();
    }

}

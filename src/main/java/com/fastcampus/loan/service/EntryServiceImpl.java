package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.fastcampus.loan.dto.EntryDTO.Request;
import static com.fastcampus.loan.dto.EntryDTO.Response;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService{
    private final BalanceService balanceService;
    private final EntryRepository entryRepository;
    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;
    @Override
    public Response create(Long applicationId, Request request) {
        if (isContractedApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        entryRepository.save(entry);
        // 대출 잔고 관리

        balanceService.create(applicationId,
                BalanceDTO.Request.builder()
                        .entryAmount(request.getEntryAmount())
                .build());

        return modelMapper.map(entry,Response.class);
    }
    private boolean isContractedApplication(Long applicationId){

        Optional<Application> existed = applicationRepository.findById(applicationId);
        if (existed.isEmpty()){
            return false;
        }

        return existed.get().getContractedAt() != null;
    }
}

package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.EntryDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.fastcampus.loan.dto.EntryDTO.*;
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


    @Override
    public Response get(Long applicationId) {
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);
        if(entry.isPresent()){
            return modelMapper.map(entry,Response.class);
        }else{
            return null;
        }
    }

    @Override
    public UpdateResponse update(Long entryId, Request request) {
        //entry
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // before -> after
        BigDecimal beforeEntryAmount= entry.getEntryAmount();
        entry.setEntryAmount(request.getEntryAmount());

        entryRepository.save(entry);

        //balance update
        Long applicationId = entry.getApplicationId();
        balanceService.update(applicationId, BalanceDTO.UpdateRequest.builder()
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build());

        // response
        return UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build();
    }
    @Override
    public void delete(Long entryId) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        entry.setIsDeleted(true);
        entryRepository.save(entry);

        BigDecimal beforeEntryAmount = entry.getEntryAmount();

        Long applicationId = entry.getApplicationId();
        balanceService.update(applicationId,
                BalanceDTO.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build());

    }


    private boolean isContractedApplication(Long applicationId){

        Optional<Application> existed = applicationRepository.findById(applicationId);
        if (existed.isEmpty()){
            return false;
        }

        return existed.get().getContractedAt() != null;
    }

}

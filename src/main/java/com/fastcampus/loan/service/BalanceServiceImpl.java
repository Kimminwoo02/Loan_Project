package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Balance;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.fastcampus.loan.dto.BalanceDTO.Request;
import static com.fastcampus.loan.dto.BalanceDTO.Response;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{
    private final BalanceRepository balanceRepository;
    private final ModelMapper modelMapper;
    @Override
    public Response create(Long applicationId, Request request) {
        if(balanceRepository.findByApplicationId(applicationId).isPresent()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Balance balance = modelMapper.map(request, Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        Balance saved = balanceRepository.save(balance);
        return modelMapper.map(saved, Response.class);
    }

    @Override
    public Response update(Long applicationId, BalanceDTO.UpdateRequest request) {
        //balance
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount =  request.getAfterEntryAmount();
        BigDecimal updatedBalanc = balance.getBalance();

        // as-is -> to-be
        updatedBalanc = updatedBalanc.subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalanc);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated,Response.class);
    }
}
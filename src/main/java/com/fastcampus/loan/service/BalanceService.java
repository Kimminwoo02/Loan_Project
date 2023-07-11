package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.BalanceDTO.*;

public interface BalanceService {
    Response create(Long applicationId, Request request);
    Response get(Long applicationId);

    Response repaymentUpdate(Long applicationId, RepaymentRequest request);

    Response update(Long applicationId, UpdateRequest request);

    void delete(Long applicationId);
}

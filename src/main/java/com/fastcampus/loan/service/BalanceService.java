package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.BalanceDTO.*;

public interface BalanceService {
    Response create(Long applicationId, Request request);

    Response update(Long applicationId, UpdateRequest request);
}

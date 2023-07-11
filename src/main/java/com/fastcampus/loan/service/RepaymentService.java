package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.RepaymentDTO;

import static com.fastcampus.loan.dto.RepaymentDTO.*;

public interface RepaymentService {
    Response create(Long applicationId, Request request);
}

package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.JudgementDTO.*;


public interface JudgementService {
    Response create(Request request);

    Response get(Long judgementId);
    Response getJudgementOfApplication (Long applicationId);

    Response update(Long judgementId, Request request);
}

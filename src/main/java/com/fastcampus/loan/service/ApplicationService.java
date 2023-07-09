package com.fastcampus.loan.service;


import com.fastcampus.loan.dto.ApplicationDTO.Request;
import com.fastcampus.loan.dto.ApplicationDTO.Response;

public interface ApplicationService {

    Response create(Request request);
    Response get(Long Id);
    Response update(Long applicationId, Request request);
    void delete(Long applicationId);
}
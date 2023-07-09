package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.TermsDTO.Request;
import com.fastcampus.loan.dto.TermsDTO.Response;

public interface TermsService {

    Response create(Request request);

}
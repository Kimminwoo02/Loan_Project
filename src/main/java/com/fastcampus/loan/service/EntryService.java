package com.fastcampus.loan.service;



import static com.fastcampus.loan.dto.EntryDTO.*;

public interface EntryService {
    Response create(Long application, Request request);

    Response get (Long applicationId);
}


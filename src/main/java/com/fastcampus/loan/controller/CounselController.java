package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.CounselDTO;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fastcampus.loan.dto.CounselDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/counsels")
public class CounselController extends AbstractController {
    protected final CounselService counselService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request){
        return ok(counselService.create(request));
    }
}

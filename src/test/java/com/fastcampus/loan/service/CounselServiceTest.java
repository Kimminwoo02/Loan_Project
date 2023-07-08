package com.fastcampus.loan.service;
import static org.mockito.Mockito.when;

import com.fastcampus.loan.domain.Counsel;
import static org.assertj.core.api.Assertions.assertThat;

import com.fastcampus.loan.repository.CounselRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import static com.fastcampus.loan.dto.CounselDTO.*;

@ExtendWith(MockitoExtension.class)
public class CounselServiceTest {
    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_returnResponseOfNewCounselEntity_When_RequestCounsel(){
        Counsel entity = Counsel.builder()
                .name("minu")
                .cellPhone("010-5555-5555")
                .email("qwerasdf@naver.com")
                .memo("연락주세요")
                .zipCode("12345")
                .address("서울시 동작구 대방동")
                .addressDetail("101-222")
                .build();


        Request request = Request.builder()
                .name("minu")
                .cellPhone("010-5555-5555")
                .email("qwerasdf@naver.com")
                .memo("연락주세요")
                .zipCode("12345")
                .address("서울시 동작구 대방동")
                .addressDetail("101-222")
                .build();


        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);

        Response actual = counselService.create(request);

    }
}


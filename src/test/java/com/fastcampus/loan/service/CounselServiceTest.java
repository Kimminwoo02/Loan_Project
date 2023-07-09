package com.fastcampus.loan.service;
import static org.mockito.Mockito.when;

import com.fastcampus.loan.domain.Counsel;
import static org.assertj.core.api.Assertions.assertThat;

import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.CounselRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import java.util.Optional;

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

    @Test
    void Should_ReturnResponseOfExistCounselEntity_When_requestExistCounselId(){
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response actual = counselService.get(findId);

        assertThat(actual.getCounselId()).isSameAs(findId);
    }

    @Test
    void Should_ThrowException_when_requestNotExistCounselId(){
        Long findId = 2L;
        when(counselRepository.findById(findId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        Assertions.assertThrows(BaseException.class, ()-> counselService.get(findId));
    }


}


package com.fastcampus.loan.repository;

import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.RepaymentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fastcampus.loan.dto.RepaymentDTO.*;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment,Long> {
    List<Repayment> findAllByApplicationId(Long applicationId);
}

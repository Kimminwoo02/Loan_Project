package com.fastcampus.loan.repository;

import com.fastcampus.loan.domain.TermsAcceptation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptTermsRepository extends JpaRepository<TermsAcceptation,Long> {
}

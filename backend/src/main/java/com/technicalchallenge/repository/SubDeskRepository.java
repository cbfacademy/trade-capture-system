package com.technicalchallenge.repository;

import com.technicalchallenge.model.SubDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDeskRepository extends JpaRepository<SubDesk, Long> {}

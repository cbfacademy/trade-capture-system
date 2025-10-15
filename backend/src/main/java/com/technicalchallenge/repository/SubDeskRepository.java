package com.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.SubDesk;

@Repository
public interface SubDeskRepository extends JpaRepository<SubDesk, Long> {}

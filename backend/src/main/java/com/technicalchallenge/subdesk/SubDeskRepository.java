package com.technicalchallenge.subdesk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDeskRepository extends JpaRepository<SubDesk, Long> {}

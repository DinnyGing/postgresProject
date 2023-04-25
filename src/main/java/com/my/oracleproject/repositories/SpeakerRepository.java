package com.my.oracleproject.repositories;

import com.my.oracleproject.models.Speaker;
import com.my.oracleproject.services.SpeakerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long>, SpeakerService {
}
package com.my.oracleproject.repositories;

import com.my.oracleproject.models.Meeting;
import com.my.oracleproject.services.MeetingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingService {
}
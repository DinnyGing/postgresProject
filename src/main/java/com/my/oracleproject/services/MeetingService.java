package com.my.oracleproject.services;

import com.my.oracleproject.models.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingService {
    List<Meeting> findByUserLogin(String login);
    Optional<Meeting> findByTitleAndAddress(String title, String address);
}

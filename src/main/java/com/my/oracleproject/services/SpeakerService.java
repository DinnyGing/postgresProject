package com.my.oracleproject.services;

import com.my.oracleproject.models.Speaker;

import java.util.List;

public interface SpeakerService {
    public List<Speaker> speakerOfMeeting(long id);
}

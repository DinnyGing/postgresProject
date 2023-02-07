package com.my.oracleproject.dao;

import com.my.oracleproject.models.Speaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpeakerDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpeakerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Speaker> index() {
        List<Speaker> speakers = jdbcTemplate.query("SELECT ID_SPEAKER, NAME_SPEAKER FROM SPEAKERS", new SpeakerMapper());
        return speakers;
    }
    public List<Speaker> meetingLike(long id){
        List<Speaker> speakers = jdbcTemplate.query("SELECT s.ID_SPEAKER, s.NAME_SPEAKER FROM SPEAKERS s JOIN MEETING_SPEAKER MS on s.ID_SPEAKER = MS.ID_SPEAKER WHERE MS.ID_MEETING = ?",
                new Object[]{id}, new SpeakerMapper());
        return speakers;
    }
    public Speaker likeId(long id){
        Speaker speaker = jdbcTemplate.query("SELECT s.ID_SPEAKER, s.NAME_SPEAKER FROM SPEAKERS s WHERE ID_SPEAKER = ?",
                new Object[]{id}, new SpeakerMapper()).stream().findAny().orElse(null);
        return speaker;
    }
    public void speakMeeting(long id_speaker, long id_meeting){
        jdbcTemplate.update("INSERT INTO MEETING_SPEAKER (ID_SPEAKER, ID_MEETING) VALUES (?, ?)",
                id_speaker, id_meeting);

    }
    public void removeSpeakMeeting(long id_speaker, long id_meeting){
        jdbcTemplate.update("DELETE MEETING_SPEAKER WHERE ID_MEETING = ? and ID_SPEAKER = ?", id_meeting, id_speaker);
    }
    public void deleteSpeaker(long id){
        jdbcTemplate.update("DELETE MEETING_SPEAKER WHERE ID_SPEAKER =?", id);
        jdbcTemplate.update("DELETE SPEAKERS WHERE ID_SPEAKER = ? ", id);
    }
    public void addSpeaker(Speaker speaker){
        jdbcTemplate.update("INSERT INTO SPEAKERS (NAME_SPEAKER) VALUES (?)", speaker.getName());
    }


}


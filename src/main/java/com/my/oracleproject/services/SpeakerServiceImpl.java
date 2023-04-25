package com.my.oracleproject.services;

import com.my.oracleproject.models.Speaker;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Repository
@Transactional(readOnly = true)
public class SpeakerServiceImpl implements SpeakerService{
    @PersistenceContext
    private EntityManager manager;
    @Override
    public List<Speaker> speakerOfMeeting(long id) {
        Query query = manager.createNativeQuery("SELECT s.* FROM  speakers s " +
                "JOIN meeting_speaker ms ON s.id_speaker = ms.id_speaker WHERE ms.id_meeting = ?", Speaker.class);
        query.setParameter(1, id);
        return query.getResultList();
    }
}

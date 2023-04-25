package com.my.oracleproject.services;

import com.my.oracleproject.models.Meeting;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class MeetingServiceImpl implements MeetingService{
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Meeting> findByUserLogin(String login) {
        Query query = manager.createNativeQuery("SELECT m.* FROM  meetings m " +
                "JOIN meeting_user mu ON m.id_meeting = mu.id_meeting " +
                "JOIN users u ON u.id_user = mu.id_user WHERE u.login_user = ?", Meeting.class);
        query.setParameter(1, login);
        return query.getResultList();
    }

    @Override
    public Optional<Meeting> findByTitleAndAddress(String title, String address) {
        Query query = manager.createNativeQuery("SELECT m.* FROM  meetings m " +
                "WHERE m.title_meeting = ? " +
                "and m.address_meeting = ?", Meeting.class);
        query.setParameter(1, title);
        query.setParameter(2, address);
        return query.getResultList().stream().findAny();
    }
}

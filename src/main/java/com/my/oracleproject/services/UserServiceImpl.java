package com.my.oracleproject.services;

import com.my.oracleproject.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    @PersistenceContext
    private EntityManager manager;

    public List<User> userOfMeeting(long id){
        Query query = manager.createNativeQuery("SELECT u.* FROM  users u " +
                "JOIN meeting_user mu ON u.id_user = mu.id_user WHERE mu.id_meeting = ?", User.class);
        query.setParameter(1, id);
        return query.getResultList();
    }

    @Override
    public User findUserByLogin(String login) {
        Query query = manager.createNativeQuery("SELECT u.* FROM  users u WHERE u.login_user = ?", User.class);
        query.setParameter(1, login);
        return (User) query.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        Query query = manager.createNativeQuery("SELECT u.* FROM  users u WHERE u.login_user = ? " +
                "and u.password_user = ?", User.class);
        query.setParameter(1, login);
        query.setParameter(2, password);
        return (User) query.getResultList().stream().findAny().orElse(null);
    }
}

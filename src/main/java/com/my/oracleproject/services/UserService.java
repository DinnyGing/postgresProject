package com.my.oracleproject.services;

import com.my.oracleproject.models.User;

import java.util.List;

public interface UserService {
    public List<User> userOfMeeting(long id);
    User findUserByLogin(String login);
    User findUserByLoginAndPassword(String login, String password);
}

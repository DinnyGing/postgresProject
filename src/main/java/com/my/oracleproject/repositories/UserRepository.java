package com.my.oracleproject.repositories;

import com.my.oracleproject.models.User;
import com.my.oracleproject.services.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserService {

}
package com.my.oracleproject.repositories;

import com.my.oracleproject.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
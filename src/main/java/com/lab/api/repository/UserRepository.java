package com.lab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lab.api.repository.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}

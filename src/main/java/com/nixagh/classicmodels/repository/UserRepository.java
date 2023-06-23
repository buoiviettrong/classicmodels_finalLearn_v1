package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UserRepository extends BaseRepository<User, Long>{
    public Optional<User> getUserByEmail(String email);
}

package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.user.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
  Optional<User> getUserByEmail(String email);
}

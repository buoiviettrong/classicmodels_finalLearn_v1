package com.nixagh.classicmodels.repository.user;

import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> getUserByEmail(String email);

    Optional<User> checkEmail(String email);

    void updateRefreshToken(Long id, String refreshToken);

}

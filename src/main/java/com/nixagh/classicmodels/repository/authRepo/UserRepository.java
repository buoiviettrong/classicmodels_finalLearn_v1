package com.nixagh.classicmodels.repository.authRepo;

import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> getUserByEmail(String email);

    Optional<User> checkEmail(String email);

    void updateRefreshToken(Long id, String refreshToken);

}

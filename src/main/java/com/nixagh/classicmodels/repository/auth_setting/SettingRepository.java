package com.nixagh.classicmodels.repository.auth_setting;

import com.nixagh.classicmodels.entity.auth.AuthSettings;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.Optional;

public interface SettingRepository extends BaseRepository<AuthSettings, String> {
    Optional<AuthSettings> getAuthSettings();
}

package com.nixagh.classicmodels.repository.authRepo;

import com.nixagh.classicmodels._common.settings.AuthSettings;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.Optional;

public interface SettingRepository extends BaseRepository<AuthSettings, String> {
    public Optional<AuthSettings> getAuthSettings();
}

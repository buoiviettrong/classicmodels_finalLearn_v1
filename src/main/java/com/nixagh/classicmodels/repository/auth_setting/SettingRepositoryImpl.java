package com.nixagh.classicmodels.repository.auth_setting;

import com.nixagh.classicmodels.entity.auth.AuthSettings;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SettingRepositoryImpl extends BaseRepositoryImpl<AuthSettings, String> implements SettingRepository {
    public SettingRepositoryImpl(EntityManager entityManager) {
        super(AuthSettings.class, entityManager);
    }

    @Override
    public Optional<AuthSettings> getAuthSettings() {
        return jpaQueryFactory
                .selectFrom(settings)
                .stream().findFirst();
    }
}

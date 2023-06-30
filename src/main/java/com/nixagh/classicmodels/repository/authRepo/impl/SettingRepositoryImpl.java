package com.nixagh.classicmodels.repository.authRepo.impl;

import com.nixagh.classicmodels._common.settings.AuthSettings;
import com.nixagh.classicmodels.repository.authRepo.SettingRepository;
import com.nixagh.classicmodels.repository.impl.BaseRepositoryImpl;
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
                .leftJoin(settings.clients, client)
                .stream().findFirst();
    }
}

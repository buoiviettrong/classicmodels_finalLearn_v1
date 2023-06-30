package com.nixagh.classicmodels.repository.authRepo.impl;

import com.nixagh.classicmodels._common.settings.OAuthClient;
import com.nixagh.classicmodels.repository.authRepo.ClientRepository;
import com.nixagh.classicmodels.repository.impl.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClientRepositoryImpl extends BaseRepositoryImpl<OAuthClient, String> implements ClientRepository {
    public ClientRepositoryImpl(EntityManager entityManager) {
        super(OAuthClient.class, entityManager);
    }

    @Override
    public List<OAuthClient> getClients() {
        return jpaQueryFactory
                .selectFrom(client)
                .stream().toList();
    }
}

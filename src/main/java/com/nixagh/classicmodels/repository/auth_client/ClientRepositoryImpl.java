package com.nixagh.classicmodels.repository.auth_client;

import com.nixagh.classicmodels.entity.auth.OAuthClient;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
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

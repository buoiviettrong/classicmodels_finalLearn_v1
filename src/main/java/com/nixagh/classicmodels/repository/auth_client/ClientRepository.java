package com.nixagh.classicmodels.repository.auth_client;

import com.nixagh.classicmodels.entity.auth.OAuthClient;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;

public interface ClientRepository extends BaseRepository<OAuthClient, String> {
    List<OAuthClient> getClients();
}

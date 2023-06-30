package com.nixagh.classicmodels.repository.authRepo;

import com.nixagh.classicmodels._common.settings.OAuthClient;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;

public interface ClientRepository extends BaseRepository<OAuthClient, String> {
    List<OAuthClient> getClients();
}

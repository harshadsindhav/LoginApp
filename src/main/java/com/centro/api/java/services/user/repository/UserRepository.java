package com.centro.api.java.services.user.repository;


import com.centro.api.java.models.user.UserDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface UserRepository extends MongoRepository<UserDetail, String> {

    UserDetail findByUsername(String username);

    List<UserDetail> findByCity(String city);

}

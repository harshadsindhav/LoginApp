package com.centro.api.java.services.user;

import com.centro.api.java.models.user.UserDetail;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    public List<UserDetail> findUsersAll() throws Exception;

    public UserDetail signupUser(UserDetail userDetail) throws Exception;

    public UserDetail findByUsername(String username) throws Exception;

    public List<UserDetail> findByCity(String city) throws Exception;

}

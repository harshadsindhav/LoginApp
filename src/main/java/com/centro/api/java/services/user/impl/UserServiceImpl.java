package com.centro.api.java.services.user.impl;

import com.centro.api.java.models.user.UserDetail;
import com.centro.api.java.services.user.UserService;
import com.centro.api.java.services.user.repository.UserRepository;
import com.centro.platform.api.java.security.hashing.CentroPasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDetail> findUsersAll() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public UserDetail signupUser(UserDetail userDetail) throws Exception {
        if(userDetail != null) {
            validateUserPassword(userDetail.getPassword());

            String plainPassword = userDetail.getPassword();
            if(plainPassword != null) {
                String passwordSalt = CentroPasswordHash.generatePasswordSalt();
                userDetail.setSalt(passwordSalt);
                String hashedPassword = CentroPasswordHash.generateHash( passwordSalt + plainPassword);
                userDetail.setPassword(hashedPassword);
            }

            UserDetail newUser = userRepository.insert(userDetail);
            return newUser;
        }
        throw new Exception("User sign up failed!!");
    }

    private void validateUserPassword(String password) throws Exception {
        if(password == null) {
            throw new Exception("Password can not be empty.");
        }
    }

    @Override
    public UserDetail findByUsername(String username) throws Exception {
        if(username != null && username.length() != 0) {
            UserDetail detail = userRepository.findByUsername(username);
            return detail;
        }
        throw new Exception("Invalid username value.");
    }

    /*@Override
    public UserDetail findByUseId(String id) throws Exception {
        if(id != null && id.length() != 0) {
            UserDetail detail = userRepository.findById(id);

        }
    }*/

    @Override
    public List<UserDetail> findByCity(String city) throws Exception {
        return null;
    }
}

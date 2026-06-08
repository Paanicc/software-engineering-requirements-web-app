package com.reqapp.service;

import com.reqapp.domain.User;

public interface UserService {
    void saveUser(User user);
    User findByUsername(String username);
    void updateUserProfile(String username, String firstName, String lastName, String email, String password);
}

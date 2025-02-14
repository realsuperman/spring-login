package com.csh.login.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, String> users = new HashMap<>();

    public UserService() {
        // 임시 사용자 데이터 (실제 환경에서는 DB 사용)
        users.put("hong", "password123");  // ID: hong, PW: password123
        users.put("csh", "1234");    // ID: kim, PW: securepass
    }

    public boolean validateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
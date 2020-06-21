package com.gz.dubbo;

import java.util.List;

public interface UserService {
    User getUser(Long id);

    List<User> findUser(String name, Integer age);
}

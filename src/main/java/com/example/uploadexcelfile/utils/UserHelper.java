package com.example.uploadexcelfile.utils;


import com.example.uploadexcelfile.constants.Constants;
import com.example.uploadexcelfile.model.User;
import com.google.common.collect.Lists;

import java.util.List;

public class UserHelper {

    public static List<User> getUsers() {
        return Lists.newArrayList(
                new User(1, Constants.DEFAULT_USER, Constants.DEFAULT_USER_PASSWORD, Constants.USER_ROLE),
                new User(2, Constants.DEFAULT_ADMIN, Constants.DEFAULT_ADMIN_PASSWORD, Constants.ADMIN_ROLE));
    }

    public static User getUser(String userName) {
        return getUsers()
                .stream()
                .filter(user -> user
                        .getUserName()
                        .equals(userName))
                .findFirst()
                .orElse(null);
    }

}

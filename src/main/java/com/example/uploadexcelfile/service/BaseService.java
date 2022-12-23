package com.example.uploadexcelfile.service;

import com.example.uploadexcelfile.exception.FileUploadException;
import com.example.uploadexcelfile.model.User;
import com.example.uploadexcelfile.utils.UserHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BaseService {
    /**
     * This method is used to get authenticated user
     * @return user
     */
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        User user = UserHelper.getUser(auth.getName());
        if (user == null) {
            throw new FileUploadException(HttpStatus.UNAUTHORIZED.value(), "Please login.!!");
        }
        return user;
    }

}

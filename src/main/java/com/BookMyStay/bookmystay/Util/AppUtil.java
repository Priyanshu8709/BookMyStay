package com.BookMyStay.bookmystay.Util;

import com.BookMyStay.bookmystay.Entity.User;
import org.springframework.security.core.context.SecurityContextHolder;


public class AppUtil {

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}


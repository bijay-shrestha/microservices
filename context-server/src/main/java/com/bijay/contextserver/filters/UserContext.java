package com.bijay.contextserver.filters;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private static final ThreadLocal<String> userName = new ThreadLocal<String>();

    public static String getUsername() { return userName.get(); }
    public static void setUsername(String arg) {userName.set(arg);}


}
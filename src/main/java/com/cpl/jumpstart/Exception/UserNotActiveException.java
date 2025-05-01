package com.cpl.jumpstart.Exception;

import java.io.IOException;

public class UserNotActiveException extends IllegalArgumentException {

    public UserNotActiveException(String email){
        super(String.format("User with email %s is inactive", email));
    }


}

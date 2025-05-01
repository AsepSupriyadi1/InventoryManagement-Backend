package com.cpl.jumpstart.Exception;



public class EmailAlreadyExistException extends RuntimeException {


    public EmailAlreadyExistException(String email){
        super(String.format("user with email '%s' already exist", email));
    }


}

package com.cpl.jumpstart.Exception;

public class RoleNotFoundException extends IllegalArgumentException {

    public RoleNotFoundException(String roleName){
        super(String.format("Role %s not found", roleName));
    }

}

package com.cpl.jumpstart.utils;

import com.cpl.jumpstart.entity.constraint.EnumCountry;
import org.springframework.stereotype.Component;



public class CountryConfig {

    public static String getCountryCode(EnumCountry enumCountry){

        return switch (enumCountry) {
            case indonesia -> "IND";
            case singapore -> "SGP";
            case malaysia -> "MY";
            case philippines -> "PH";
            case thailand -> "THA";
            default -> throw new RuntimeException("Country code not found !");

        };

    }


}

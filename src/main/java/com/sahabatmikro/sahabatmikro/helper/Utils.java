package com.sahabatmikro.sahabatmikro.helper;

public class Utils {

    /**
     * method for get next 30 days
     */
    public static Long next30Days(){
        return System.currentTimeMillis() + (1000*16*24*30);
    }
}

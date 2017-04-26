package com.beacon.drunkengranite.forwardspam.logic;

/**
 * Created by drunkengranite on 1/26/17.
 */

public class PhoneLogic {

//  todo more validation

    public static boolean isPhoneNumber(String phoneNumber){
        int length = phoneNumber.length();

        if(length > 10 || length < 9) return false;

        return true;
    }



}

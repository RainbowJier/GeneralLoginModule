package com.example.system.service;

import com.example.common.enums.SendCode;
import com.example.common.util.JsonData;

import javax.mail.MessagingException;

public interface NotifyService {
    JsonData sendCode(SendCode userRegister, String to) throws MessagingException;


    boolean checkCode(SendCode sendCodeEnum, String to, String code);


}

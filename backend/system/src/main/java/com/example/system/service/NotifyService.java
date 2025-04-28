package com.example.system.service;

import com.example.common.enums.SendCodeEnum;
import com.example.common.util.JsonData;

import javax.mail.MessagingException;

public interface NotifyService {
    JsonData sendCode(SendCodeEnum userRegister, String to) throws MessagingException;


    boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code);


}

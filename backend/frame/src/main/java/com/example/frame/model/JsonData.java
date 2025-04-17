package com.example.frame.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.frame.enums.BizCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description：Jsondata is a class that represents a JSON data object.
 * @Author： RainbowJier
 * @Data： 2024/8/12 21:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JsonData {
    private Integer code;

    private Object data;

    private String msg;

    /**
     * Construct a new JsonData object with the specified code, data and message.
     */
    public <T> T getData(TypeReference<T> typeReference) {
        return JSON.parseObject(JSON.toJSONString(data), typeReference);
    }

    /**
     * Successful response object.
     */
    public static JsonData buildSuccess() {
        return new JsonData(BizCode.SUCCESS.getCode(), null, BizCode.SUCCESS.getMessage());
    }

    public static JsonData buildSuccess(Object data) {
        return new JsonData(BizCode.SUCCESS.getCode(), data, BizCode.SUCCESS.getMessage());
    }

    public static JsonData buildSuccess(Object data,String msg) {
        return new JsonData(BizCode.SUCCESS.getCode(), data, msg);
    }

    /**
     * Error with description.
     */
    public static JsonData buildError() {
        return new JsonData(BizCode.FAILED.getCode(), null, BizCode.FAILED.getMessage());
    }

    public static JsonData buildError(BizCode bizCode) {
        return new JsonData(bizCode.getCode(), null, bizCode.getMessage());
    }


    /**
     * Custom response object
     */
    public static JsonData buildCodeAndMsg(int code,Object data,String msg) {
        return new JsonData(code, data, msg);
    }
}
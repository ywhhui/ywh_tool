package com.szcgc.cougua.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 不规范的返回
 * @Author liaohong
 * @create 2020/9/24 14:58
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo<T> {

    private T data;

    private String message;

    private boolean success;

}

package com.szcgc.cougua.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 不规范的返回
 * @Author liaohong
 * @create 2020/9/24 14:58
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocDataVo {

    private List<Map<String,String>> mortgage;

}

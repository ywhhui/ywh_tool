package com.szcgc.cougua.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 提交公共vo
 * @Author liaohong
 * @create 2020/9/24 14:58
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialSubmitVo {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 项目Id
     */
    private int projectId;

}

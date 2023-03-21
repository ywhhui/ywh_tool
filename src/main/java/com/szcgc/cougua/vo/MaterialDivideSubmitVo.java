package com.szcgc.cougua.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 分配评估师的提交vo
 * @Author liaohong
 * @create 2022/9/19 15:02
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialDivideSubmitVo {

        /**
         * 项目Id
         */
        private int projectId;

        /**
         * 任务id
         */
        private String taskId;

        /**
         * 被分配评估师
         */
        private int assessingAccountId;

}

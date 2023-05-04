package com.szcgc.cougua.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardEvent {

    private List<DashBoardEventValue> index;

}

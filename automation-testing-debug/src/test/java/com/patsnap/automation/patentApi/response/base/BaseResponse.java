package com.patsnap.automation.patentApi.response.base;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuyikai on 2017/9/5.
 */

public abstract  class BaseResponse {
    
    @Getter @Setter
    private String code;
    
    @Getter @Setter
    private String message;
    
    @Getter @Setter
    private long time;
}

package com.atguigu.gmall.common.exception;

import com.atguigu.gmall.common.to.CommonResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("自定义的全局异常类")
public class GmallException extends RuntimeException {

    @ApiModelProperty("错误码")
    private Integer code;

    /***
     * 接受指定的错误码和错误提示消息
     * @param code
     * @param message
     */
    public GmallException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public GmallException(CommonResult commonResult) {
        super(commonResult.getMessage());
        this.code = commonResult.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" + "message=" + this.getMessage() + ", code=" + code + '}';
    }

}

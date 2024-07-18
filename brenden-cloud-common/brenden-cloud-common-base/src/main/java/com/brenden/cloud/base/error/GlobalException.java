package com.brenden.cloud.base.error;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/6/10
 */
@Getter
public class GlobalException extends RuntimeException{

    @java.io.Serial
    private static final long serialVersionUID = -7034897190745766939L;

    private final String errorCode;

    private final String errorMsg;

    public GlobalException(GlobalCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.errorCode = codeEnum.getCode();
        this.errorMsg = codeEnum.getMsg();
    }

    public GlobalException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}

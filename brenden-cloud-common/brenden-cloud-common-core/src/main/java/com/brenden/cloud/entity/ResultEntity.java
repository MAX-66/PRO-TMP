package com.brenden.cloud.entity;

import com.brenden.cloud.error.GlobalCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/6/10
 */
@Data
public class ResultEntity<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5418751571815620505L;

    private String resultCode;

    private String resultMsg;

    private T data;

    private long timestamp = System.currentTimeMillis();

    public static <T> ResultEntity<T> success(T data) {
        ResultEntity<T> result = new ResultEntity<>();
        result.data = data;
        result.resultCode = GlobalCodeEnum.GC_0.getCode();
        result.resultMsg = GlobalCodeEnum.GC_0.getMsg();
        return result;
    }



    public static <T> ResultEntity<T> fail(String errorCode, String errorMsg) {
        ResultEntity<T> result = new ResultEntity<>();
        result.setResultCode(errorCode);
        result.setResultMsg(errorMsg);
        return result;
    }

    public static <T> ResultEntity<T> fail(GlobalCodeEnum codeEnum) {
        ResultEntity<T> result = new ResultEntity<>();
        result.setResultCode(codeEnum.getCode());
        result.setResultMsg(codeEnum.getMsg());
        return result;
    }
}

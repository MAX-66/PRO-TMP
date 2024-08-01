package com.brenden.cloud.base.entity;

import com.brenden.cloud.base.error.GlobalCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "统一返回参数")
public class ResultEntity<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5418751571815620505L;

    @Schema(description = "操作码")
    private String resultCode;
    @Schema(description = "操作提示")
    private String resultMsg;
    @Schema(description = "数据")
    private T data;
    @Schema(description = "时间戳")
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
        return fail(codeEnum.getCode(), codeEnum.getMsg());
    }
}

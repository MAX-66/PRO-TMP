package com.brenden.cloud.error;

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
public enum GlobalCodeEnum {

    GC_0(0, "操作成功!"),
    GC_500(500, "操作失败!"),
    GC_800006(800006, "参数错误!"),
    GC_800001(800001, "无访问权限!"),
    GC_800002(800002, "认证失败!"),
    GC_800003(800003, "授权失败!");

    private final Integer code;

    private final String msg;

    GlobalCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}

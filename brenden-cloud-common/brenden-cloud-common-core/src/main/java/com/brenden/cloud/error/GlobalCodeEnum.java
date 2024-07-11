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

    GC_0("0", "操作成功!"),
    GC_500("500", "操作失败!"),
    GC_800000("800000", "参数错误!"),
    GC_800001("800001", "无访问权限!"),
    GC_800002("800002", "认证失败!"),
    GC_800003("800003", "授权失败!"),
    GC_800004("800004", "登录已过期,请重新登录!");

    private final String code;

    private final String msg;

    GlobalCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static GlobalCodeEnum getEnum(String code) {
        for (GlobalCodeEnum ele : GlobalCodeEnum.values()) {
            if (ele.code.equals(code)) {
                return ele;
            }
        }
        return null;
    }


}

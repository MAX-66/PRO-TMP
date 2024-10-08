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
public enum GlobalCodeEnum {

    GC_0("0", "操作成功!"),
    GC_500("500", "操作失败!"),
    GC_800000("800000", "参数错误!"),
    GC_800001("800001", "无访问权限!"),
    GC_800002("800002", "认证失败!"),
    GC_800003("800003", "授权失败!"),
    GC_800004("800004", "登录已过期,请重新登录!"),
    GC_800005("800005", "服务不在线!"),
    GC_800006("800006", "服务内部错误，请联系管理员!"),
    GC_800007("800007", "访问资源不存在!"),
    GC_800008("800008", "错误请求!"),
    GC_800009("800009", "网关错误!"),
    GC_800010("800010", "签名错误!"),
    GC_800011("800011", "签名不能为空!"),
    GC_800012("800012", "时间戳不能为空!"),
    GC_800013("800013", "服务调用失败!"),
    GC_800014("800014", "网络拥挤，请稍后再试!"),
    GC_800015("800015", "服务已降级，请稍后再试!"),
    GC_800016("800016", "热点参数已限流，请稍后再试!"),
    GC_800017("800017", "系统规则错误，请稍后再试!"),
    GC_800018("800018", "授权规则错误，请稍后再试!"),
    GC_800099("800099", "未知错误!");

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

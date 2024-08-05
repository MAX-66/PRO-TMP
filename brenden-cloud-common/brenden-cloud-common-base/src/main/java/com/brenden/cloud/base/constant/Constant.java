package com.brenden.cloud.base.constant;

public class Constant {



    public static final int DEFAULT = 0;


    /** 否 */
    public static final int NO = 0;

    /** 是 */
    public static final int YES = 1;

    /** 环境：dev */
    public static final String DEV_PROFILE = "dev";

    /** 环境：test */
    public static final String TEST_PROFILE = "test";

    /** 环境：pre */
    public static final String PRE_PROFILE = "pre";

    /** 环境：prod */
    public static final String PROD_PROFILE = "prod";

    /** token 类型*/
    public static final String BEARER_TOKEN = "Bearer";

    /** feign 请求头标记*/
    public static final String REQUEST_FEIGN_HEADER = "X-Internal-Call";
}

package com.brenden.cloud.config.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.brenden.cloud.constant.Constant;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: wu
 * @date: 2020-08-07
 */
@Component
public class CustomizedMetaObjectHandler implements MetaObjectHandler {

    public static final String GMT_CREATE = "gmtCreate";

    public static final String GMT_MODIFIED = "gmtModified";

    public static final String DEL_FLAG = "isDelete";

    public static final String VERSION = "version";

    public static final String CREATE_BY = "createBy";

    public static final String UPDATE_BY = "updateBy";


    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, GMT_CREATE, LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, GMT_MODIFIED, LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, DEL_FLAG, () -> Constant.DEFAULT, Integer.class);
        this.strictInsertFill(metaObject, VERSION, () -> Constant.DEFAULT, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
    }
}

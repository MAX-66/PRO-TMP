package com.brenden.cloud.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/10/21
 */
@Data
public class BaseEntity implements Serializable {

    @TableField(
            fill = FieldFill.INSERT
    )
    private String createBy;
    @TableField(
            fill = FieldFill.INSERT
    )
    private String creatorId;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(
            fill = FieldFill.INSERT
    )
    private Date createTime;
    @TableField(
            fill = FieldFill.INSERT_UPDATE
    )
    private String updateBy;
    @TableField(
            fill = FieldFill.INSERT_UPDATE
    )
    private String updaterId;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(
            fill = FieldFill.INSERT_UPDATE
    )
    private Date updateTime;
    private String tenantId;
    @TableField(
            exist = false
    )
    private String remark;

    private String deleteFlag;
}

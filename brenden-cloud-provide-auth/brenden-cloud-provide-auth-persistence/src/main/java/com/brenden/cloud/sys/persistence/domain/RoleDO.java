package com.brenden.cloud.sys.persistence.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brenden.cloud.entity.BaseDO;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author brenden
 * @since 2023-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role")
public class RoleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名
     */
    @TableField("name")
    private String name;

    /**
     * 状态 1启用 2禁用
     */
    @TableField("status")
    private Integer status;


}

package com.brenden.cloud.sys.persistence.mapper;

import com.brenden.cloud.sys.persistence.domain.UserRoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author brenden
 * @since 2023-09-10
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

}

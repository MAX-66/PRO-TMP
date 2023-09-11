package com.brenden.cloud.sys.persistence.manager.impl;

import com.brenden.cloud.sys.persistence.domain.UserRoleDO;
import com.brenden.cloud.sys.persistence.mapper.UserRoleMapper;
import com.brenden.cloud.sys.persistence.manager.UserRoleManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author brenden
 * @since 2023-09-10
 */
@Service
public class UserRoleManagerImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleManager {

}

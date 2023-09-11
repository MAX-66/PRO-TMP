package com.brenden.cloud.sys.persistence.manager.impl;

import com.brenden.cloud.sys.persistence.domain.RoleDO;
import com.brenden.cloud.sys.persistence.mapper.RoleMapper;
import com.brenden.cloud.sys.persistence.manager.RoleManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author brenden
 * @since 2023-09-10
 */
@Service
public class RoleManagerImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleManager {

}

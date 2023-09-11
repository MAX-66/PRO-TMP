package com.brenden.cloud.sys.persistence.manager.impl;

import com.brenden.cloud.sys.persistence.domain.UserDO;
import com.brenden.cloud.sys.persistence.mapper.UserMapper;
import com.brenden.cloud.sys.persistence.manager.UserManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author brenden
 * @since 2023-09-10
 */
@Service
public class UserManagerImpl extends ServiceImpl<UserMapper, UserDO> implements UserManager {

}

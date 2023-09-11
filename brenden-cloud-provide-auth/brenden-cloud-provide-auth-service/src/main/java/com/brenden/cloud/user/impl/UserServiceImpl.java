package com.brenden.cloud.user.impl;

import com.brenden.cloud.auth.user.SecurityUserDetails;
import com.brenden.cloud.sys.persistence.domain.RoleDO;
import com.brenden.cloud.sys.persistence.domain.UserDO;
import com.brenden.cloud.sys.persistence.domain.UserRoleDO;
import com.brenden.cloud.sys.persistence.manager.RoleManager;
import com.brenden.cloud.sys.persistence.manager.UserManager;
import com.brenden.cloud.sys.persistence.manager.UserRoleManager;
import com.brenden.cloud.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/9/10
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserRoleManager userRoleManager;
    @Autowired
    private RoleManager roleManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userManager.lambdaQuery().eq(UserDO::getUsername, username).one();
        if (Objects.isNull(userDO)) {
            throw new RuntimeException("用户不存在");
        }
        List<UserRoleDO> userRoleDOS = userRoleManager.lambdaQuery().eq(UserRoleDO::getUserId, userDO.getId()).list();
        if (CollectionUtils.isEmpty(userRoleDOS)) {
            throw new RuntimeException("用户未初始化");
        }
        Set<Long> roleIdList = userRoleDOS.stream().map(UserRoleDO::getRoleId).collect(Collectors.toSet());
        List<RoleDO> roleDOList = roleManager.lambdaQuery().in(RoleDO::getId, roleIdList).list();
        if (CollectionUtils.isEmpty(roleDOList)) {
            throw new RuntimeException("用户未初始化");
        }
        List<String> roleNameList = roleDOList.stream().map(RoleDO::getName).collect(Collectors.toList());
        return new SecurityUserDetails(userDO.getUsername(), userDO.getPassword(), userDO.getStatus(), roleNameList);
    }
}

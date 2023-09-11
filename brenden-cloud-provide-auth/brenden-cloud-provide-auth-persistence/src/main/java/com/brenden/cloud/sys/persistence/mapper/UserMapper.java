package com.brenden.cloud.sys.persistence.mapper;

import com.brenden.cloud.sys.persistence.domain.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author brenden
 * @since 2023-09-10
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}

package com.brenden.cloud.injector.methods;

import java.io.Serial;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 根据 code 删除
 *
 * @author lisuheng
 * @since 2024-08-31
 */
public class DeleteByCode extends AbstractMethod {

	@Serial
	private static final long serialVersionUID = -1728635812701255923L;

	public DeleteByCode() {
		super("deleteByCode");
	}


	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sql = "\nUPDATE %s %s WHERE code=#{code} %s\n";
		if (tableInfo.isWithLogicDelete()) {
			sql = String.format(sql, tableInfo.getTableName(), sqlLogicSet(tableInfo),
					tableInfo.getLogicDeleteSql(true, true));
		}
		else {
			sql = String.format(sql, tableInfo.getTableName(), "", "", "");
		}
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
		return addUpdateMappedStatement(mapperClass, modelClass, sqlSource);
	}

}

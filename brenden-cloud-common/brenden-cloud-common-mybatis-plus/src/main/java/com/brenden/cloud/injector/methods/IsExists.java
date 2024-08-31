package com.brenden.cloud.injector.methods;

import java.io.Serial;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 是否存在
 *
 * @author lisuheng
 * @date 2024-08-31
 */
public class IsExists extends AbstractMethod {
	@Serial
	private static final long serialVersionUID = 7369849245698719214L;

	public IsExists() {
		super("isExists");
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sql = "<script>%s SELECT 1 FROM %s %s %s LIMIT 1</script>";
		String format = String.format(sql, sqlFirst(), tableInfo.getTableName(),
				sqlWhereEntityWrapper(true, tableInfo), sqlComment());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, format, modelClass);
		return this.addSelectMappedStatementForOther(mapperClass, sqlSource, Integer.class);
	}

}

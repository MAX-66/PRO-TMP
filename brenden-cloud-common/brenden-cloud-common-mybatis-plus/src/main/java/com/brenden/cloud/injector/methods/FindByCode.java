package com.brenden.cloud.injector.methods;

import java.io.Serial;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/8
 */
@Slf4j
public class FindByCode extends AbstractMethod {

    @Serial
    private static final long serialVersionUID = 4037675944528443641L;

    public FindByCode() {
        super("findByCode");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "SELECT %s FROM %s WHERE code=#{%s} %s LIMIT 1";
        SqlSource sqlSource = super.createSqlSource(configuration, String.format(sql,
                sqlSelectColumns(tableInfo, false),
                tableInfo.getTableName(), tableInfo.getKeyProperty(),
                tableInfo.getLogicDeleteSql(true, true)), Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
    }


}

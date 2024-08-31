package com.brenden.cloud.injector.methods;

import java.io.Serial;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * 因为 Wrapper 对象的实现泰太多, 所以在这里直接添加 LIMIT 1
 * </p>
 *
 * @author lxq
 * @since 2024/8/8
 */
public class SelectOne extends AbstractMethod {

    @Serial
    private static final long serialVersionUID = -1630864661516413805L;

    public SelectOne() {
        super("selectOne");
    }

    public static final String SELECT_ONE_SQL = "<script>%s SELECT %s FROM %s %s %s %s\n</script>";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlSource sqlSource = super.createSqlSource(configuration, String.format(SELECT_ONE_SQL,
                sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlComment(), "LIMIT 1"), modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
    }
}

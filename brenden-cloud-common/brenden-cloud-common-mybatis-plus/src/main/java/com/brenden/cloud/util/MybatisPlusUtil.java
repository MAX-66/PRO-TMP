package com.brenden.cloud.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * <p>
 * 重写批量执行（多线程）若升级到Java21 可以考虑使用虚拟线程
 * </p>
 *
 * @author lxq
 * @since 2024/8/8
 */
@Slf4j
@Deprecated
public class MybatisPlusUtil {

/*    public static <E> boolean executeBatch(SqlSessionFactory sqlSessionFactory, Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        return !CollectionUtils.isEmpty(list) && executeBatch(sqlSessionFactory, log, sqlSession -> {
            int size = list.size();
            int idxLimit = Math.min(batchSize, size);
            int i = 1;
            for (E element : list) {
                consumer.accept(sqlSession, element);
                if (i == idxLimit) {
                    sqlSession.flushStatements();
                    idxLimit = Math.min(idxLimit + batchSize, size);
                }
                i++;
            }
        });
    }*/

    public static <E> boolean executeBatch(SqlSessionFactory sqlSessionFactory, List<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        // 分片
        List<List<E>> partition = Lists.partition(list, batchSize);
        // 处理 插入 还是其他由使用者传递

        return true;
    }


    public static <E> boolean executeBatch(SqlSessionFactory sqlSessionFactory, List<E> list, BiConsumer<SqlSession, E> consumer) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            list.forEach(item -> consumer.accept(sqlSession, item));
            sqlSession.commit();
        } catch (Exception e) {
            log.error("Batch operation failed, error message", e);
            return false;
        } finally {
            sqlSession.clearCache();
            sqlSession.close();
        }
        return true;
    }
}

package com.brenden.cloud.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * cglib copy
 * </p>
 *
 * @author lxq
 * @since 2024/8/6
 */
@Slf4j
public class BeanCopyUtil {

    private BeanCopyUtil() {

    }

    /**
     * 拷贝对象属性
     * @param source 源对象
     * @param target 目标对象
     */
    public static <S, T> void copyProperties(S source, T target) {
        if (Objects.isNull(source)) {
            return;
        }
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }


    /**
     * 生成目标对象，并拷贝对象属性
     * @param source 源对象
     * @param targetClass 目标对象Class
     */
    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 生成目标对象集合，并拷贝集合对象属性
     * @param sources 源对象集合
     * @param targetClass 目标对象
     * @return 目标对象集合
     */
    public static <S, T> Collection<T> copyProperties(Collection<S> sources,  Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return CollectionUtils.emptyCollection();
        }
        return sources.stream().map(source -> copyProperties(source, targetClass)).toList();
    }

    /**
     * 对源对象集合进行 map 操作
     * @param sources 源对象集合
     * @param mapper 转换函数
     * @return 目标对象集合
     */
    public static <S, T> Collection<? extends T> copyProperties(Collection<S> sources,  Function<? super S, ? extends T> mapper) {
        if (CollectionUtils.isEmpty(sources)) {
            return CollectionUtils.emptyCollection();
        }
        return sources.stream().map(mapper).toList();
    }
}

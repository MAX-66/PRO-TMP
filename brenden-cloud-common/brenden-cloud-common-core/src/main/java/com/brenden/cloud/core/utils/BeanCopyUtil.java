package com.brenden.cloud.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Collection;
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

    public static <S, T> void copyProperties(S source, T target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            return null;
        }
    }


    public static <S, T> Collection<T> copyProperties(Collection<S> sources,  Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return CollectionUtils.emptyCollection();
        }
        return sources.stream().map(source -> copyProperties(source, targetClass)).toList();
    }

    public static <S, T> Collection<? extends T> copyProperties(Collection<S> sources,  Function<? super S, ? extends T> mapper) {
        if (CollectionUtils.isEmpty(sources)) {
            return CollectionUtils.emptyCollection();
        }
        return sources.stream().map(mapper).toList();
    }
}

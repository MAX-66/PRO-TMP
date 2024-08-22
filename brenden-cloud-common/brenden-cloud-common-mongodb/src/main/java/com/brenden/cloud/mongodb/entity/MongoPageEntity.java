package com.brenden.cloud.mongodb.entity;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 参考mybatis-plus的分页，统一返回参数
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */
@Data
public class MongoPageEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7890490884770378096L;

    /**
     * 每页显示条数，默认 10
     */
    private Long size = 10L;

    /**
     * 当前页
     */
    private Long current = 1L;

    /**
     * 查询列表总记录数
     */
    private Long total = 0L;

    /**
     * 排序字段信息
     */
    private List<Sort> sorts = Collections.emptyList();;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();


    public MongoPageEntity() {}

    public MongoPageEntity(Long size, Long current, Long total, List<T> records) {
        this.size = size;
        this.current = current;
        this.total = total;
        this.records = records;
    }

    public MongoPageEntity(List<T> content, Pageable pageable, long total) {

        this.size = (long) pageable.getPageSize();
        this.current = (long) pageable.getPageNumber() + 1;
        this.records = content;
        this.total = pageable.toOptional().filter(it -> !content.isEmpty())//
                .filter(it -> it.getOffset() + it.getPageSize() > total)//
                .map(it -> it.getOffset() + content.size())//
                .orElse(total);
    }


}

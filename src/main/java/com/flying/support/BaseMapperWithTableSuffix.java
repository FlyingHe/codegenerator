package com.flying.support;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by FlyingHe on 2018/2/3.
 * 带分表功能
 */
public interface BaseMapperWithTableSuffix<T, IDTYPE> {
    /**
     * 新增实体
     *
     * @param entity
     * @return
     */
    public int save(T entity);

    /**
     * 通过主键获取实体
     *
     * @param id
     * @return
     */
    public T getById(
            @Param("id")
                    IDTYPE id,
            @Param("suffix")
                    String suffix);

    /**
     * 通过主键删除记录
     *
     * @param id
     * @return
     */
    public int removeById(
            @Param("id")
                    IDTYPE id,
            @Param("suffix")
                    String suffix);

    /**
     * 通过主键批量删除记录
     *
     * @param ids
     * @return
     */
    public int removeByIds(
            @Param("ids")
                    IDTYPE[] ids,
            @Param("suffix")
                    String suffix);

    /**
     * 根据主键更新所有字段
     *
     * @param entity
     * @return
     */
    public int update(T entity);

    /**
     * 根据主键动态更新数据，当字段为空并且被更新字段在empF集合里不存在时不会对该字段执行更新
     *
     * @param entity 被更新实体
     * @param empF   实体属性名集合
     * @return 被更新的记录数
     */
    public int updateDynamically(
            @Param("bean")
                    T entity,
            @Param("empF")
                    List<String> empF);

    /**
     * 动态查询实体
     *
     * @param qo
     * @return
     */
    public List<T> getDynamically(T qo);

    /**
     * 动态查询记录条数
     *
     * @param qo
     * @return
     */
    public int countDynamically(T qo);
}

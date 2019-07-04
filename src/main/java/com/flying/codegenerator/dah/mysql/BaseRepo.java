package com.flying.codegenerator.dah.mysql;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2019/5/30
 */
public abstract class BaseRepo<T> {
    protected String NS = "";

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    /**
     * 新增实体
     *
     * @param entity
     * @return
     */
    public int save(T entity) {
        return this.sqlSessionTemplate.insert(NS + "save", entity);
    }

    /**
     * 通过主键获取实体
     *
     * @param id
     * @return
     */
    public T getById(String id) {
        return this.sqlSessionTemplate.selectOne(NS + "getById", id);
    }

    /**
     * 通过主键删除记录
     *
     * @param id
     * @return
     */
    public int removeById(String id) {
        return this.sqlSessionTemplate.delete(NS + "removeById", id);
    }

    /**
     * 通过主键批量删除记录
     *
     * @param ids
     * @return
     */
    public int removeByIds(String[] ids) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("ids", ids);
        return this.sqlSessionTemplate.delete(NS + "removeByIds", param);
    }

    /**
     * 根据主键更新所有字段
     *
     * @param entity
     * @return
     */
    public int update(T entity) {
        return this.sqlSessionTemplate.update(NS + "update", entity);
    }

    /**
     * 根据主键动态更新数据，当字段为空并且被更新字段在empF集合里不存在时不会对该字段执行更新
     *
     * @param entity 被更新实体
     * @param empF   实体属性名集合
     * @return 被更新的记录数
     */
    public int updateDynamically(T entity, List<String> empF) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("bean", entity);
        param.put("empF", empF);
        return this.sqlSessionTemplate.update(NS + "updateDynamically", param);
    }

    /**
     * 动态查询实体
     *
     * @param qo
     * @return
     */
    public List<T> getDynamically(T qo) {
        return this.sqlSessionTemplate.selectList(NS + "getDynamically", qo);
    }

    /**
     * 动态查询记录条数
     *
     * @param qo
     * @return
     */
    public int countDynamically(T qo) {
        return this.sqlSessionTemplate.selectOne(NS + "countDynamically", qo);
    }
}

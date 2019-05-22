package com.flying.support;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/4/12
 */
@Transactional(readOnly = true, rollbackFor = Exception.class)
public abstract class BaseService<ENTITY, IDTYPE, QO extends ENTITY, MAPPER extends BaseMapper<ENTITY, IDTYPE>> {
    @Autowired
    protected MAPPER mapper;

    public ENTITY getById(IDTYPE id) {
        return this.mapper.getById(id);
    }

    public Page<ENTITY> getPageDynamically(QO qo, int pn, int ps, Sort... sorts) {
        if (null != sorts && 0 != sorts.length) {
            StringBuffer orderBy = new StringBuffer();
            for (Sort sort : sorts) {
                orderBy.append(String.format("%s %s,", sort.getOrderBy(), sort.getOrderType()));
            }
            orderBy.deleteCharAt(orderBy.length() - 1);
            PageHelper.startPage(pn, ps, orderBy.toString());
        } else {
            PageHelper.startPage(pn, ps);
        }
        return (Page<ENTITY>) this.mapper.getDynamically(qo);
    }

    public List<ENTITY> getListDynamically(QO qo) {
        return this.mapper.getDynamically(qo);
    }

    @Transactional(readOnly = false)
    public ENTITY save(ENTITY entity) {
        this.mapper.save(entity);
        return entity;
    }

    @Transactional(readOnly = false)
    public void updateDynamically(ENTITY entity) {
        this.mapper.updateDynamically(entity, new ArrayList<>());
    }

    @Transactional(readOnly = false)
    public int deleteById(IDTYPE id) {
        return this.mapper.removeById(id);
    }
}

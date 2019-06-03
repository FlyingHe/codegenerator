package com.flying.codegenerator.dah.sqlserver;

import com.github.flyinghe.tools.Sort;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/4/12
 */
@Service
@Validated
public abstract class BaseService<ENTITY, MAPPER extends BaseMapper<ENTITY>> {
    @Autowired
    protected MAPPER mapper;

    public ENTITY getById(String id) {
        return this.mapper.getById(id);
    }

    public Page<ENTITY> getPageDynamically(ENTITY qo, int pn, int ps, Sort... sorts) {
        if (null != sorts && 0 != sorts.length) {
            StringBuilder orderBy = new StringBuilder();
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

    public List<ENTITY> getListDynamically(ENTITY qo) {
        return this.mapper.getDynamically(qo);
    }

    public ENTITY save(@Valid ENTITY entity) {
        this.mapper.save(entity);
        return entity;
    }

    public void updateDynamically(ENTITY entity) {
        this.mapper.updateDynamically(entity, new ArrayList<>());
    }

    public int deleteById(String id) {
        return this.mapper.removeById(id);
    }
}

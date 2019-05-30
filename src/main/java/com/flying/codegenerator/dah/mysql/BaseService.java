package com.flying.codegenerator.dah.mysql;

import com.github.flyinghe.tools.Sort;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/5/30
 */
public abstract class BaseService<ENTITY, REPO extends BaseRepo<ENTITY>> {

    @Autowired
    protected REPO repo;

    public ENTITY getById(String id) {
        return this.repo.getById(id);
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
        return (Page<ENTITY>) this.repo.getDynamically(qo);
    }

    public List<ENTITY> getListDynamically(ENTITY qo) {
        return this.repo.getDynamically(qo);
    }

    public ENTITY save(ENTITY entity) {
        this.repo.save(entity);
        return entity;
    }

    public void updateDynamically(ENTITY entity) {
        this.repo.updateDynamically(entity, new ArrayList<>());
    }

    public int deleteById(String id) {
        return this.repo.removeById(id);
    }
}

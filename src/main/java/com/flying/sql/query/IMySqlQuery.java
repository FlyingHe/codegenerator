package com.flying.sql.query;

import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;

/**
 * Created by FlyingHe on 2019/4/11.
 */
public class IMySqlQuery extends MySqlQuery {
    @Override
    public String[] fieldCustom() {
        return new String[]{"Null"};
    }
}

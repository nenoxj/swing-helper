package cn.note.service.toolkit.formatter;

import com.alibaba.druid.util.JdbcUtils;

/**
 * 支持的sql类型
 */
public enum SqlType {
    mysql(JdbcUtils.MYSQL),
    oracle(JdbcUtils.ORACLE),
    postgresql(JdbcUtils.POSTGRESQL),
    ;


    private String type;

    SqlType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }}

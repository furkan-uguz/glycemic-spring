package com.glycemic.core.config;

import org.hibernate.dialect.MySQLDialect;

public class DatabaseConfigurer extends MySQLDialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
    }
}
package com.fstn.common.utils.sql.builder.model.table;

/**
 * Table
 */
public class Table
{

    private String name;
    private String alias;

    public Table() {
        this.name = "";
        this.alias = "";
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }
}

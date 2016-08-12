package com.fstn.common.utils.sql.builder.model;

/**
 * And separator
 */
public class AndSeparator implements Separator
{
    private static final String SEPARATOR = "AND";

    @Override
    public String getValue() {
        return SEPARATOR;
    }
}

package com.fstn.common.utils.sql.builder.model.criterion;

/**
 * Contract for where criterion
 */
public interface Criterion<T>
{
    String getName();

    void setName(String name);

    String getOperator();

    void setOperator(String operator);

    T getValue();

    void setValue(T value);
}

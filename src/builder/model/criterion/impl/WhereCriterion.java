package com.fstn.common.utils.sql.builder.model.criterion.impl;

import com.fstn.common.utils.sql.builder.model.criterion.Criterion;

/**
 * Created by lds on 26/07/2016.
 */
public class WhereCriterion<T> implements Criterion<T>
{
    /**
     * Final value to use inside where ex: "content.invoiceLines[]"->'tax'
     */
    private String name;
    private String operator;
    /**
     * RSQL Selector use for fast request subselect  to use inside where ex: content->'invoiceLines'->'tax'
     */
    private String rsqlSelector;
    private T value;

    public WhereCriterion() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getOperator() {
        return this.operator;
    }

    @Override
    public void setOperator(final String operator) {
        this.operator = operator;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(final T value) {
        this.value = value;
    }

    public String getRsqlSelector() {
        return rsqlSelector;
    }

    public void setRsqlSelector(String rsqlSelector) {
        this.rsqlSelector = rsqlSelector;
    }
}

package com.fstn.common.utils.sql.builder;

import com.fstn.common.utils.sql.builder.exception.QueryBuilderException;
import com.fstn.common.utils.sql.builder.model.criterion.Criterion;
import com.fstn.common.utils.sql.builder.model.table.Table;

/**
 * Builder which provides methods for build query string
 */
public abstract class QueryBuilder
{
    public abstract QueryBuilder from(Table table) throws QueryBuilderException;

    public abstract QueryBuilder where() throws QueryBuilderException;

    public abstract QueryBuilder where(Criterion criteria) throws QueryBuilderException;

    public abstract QueryBuilder and() throws QueryBuilderException;

    public abstract QueryBuilder addWhereCriterion(Criterion criterion) throws QueryBuilderException;

    public abstract String getQuery();
}

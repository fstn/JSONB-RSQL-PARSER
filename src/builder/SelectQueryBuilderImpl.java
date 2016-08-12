package com.fstn.common.utils.sql.builder;

import com.fstn.common.utils.sql.builder.exception.QueryBuilderException;
import com.fstn.common.utils.sql.builder.model.AndSeparator;
import com.fstn.common.utils.sql.builder.model.criterion.Criterion;
import com.fstn.common.utils.sql.builder.model.table.Table;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query builder for postgres
 */
public class SelectQueryBuilderImpl extends SelectQueryBuilder
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectQueryBuilderImpl.class);
    private boolean isWhereInitialized;
    private String query;
    private boolean isSelectInitialized;
    private boolean isFromInitialized;
    private boolean separatorInitialized;
    private boolean criteriaInitialized;

    public SelectQueryBuilderImpl() {
        this.query = "";
        this.isFromInitialized = false;
        this.isWhereInitialized = false;
        this.isSelectInitialized = false;
        this.separatorInitialized = false;
        this.criteriaInitialized = false;
    }

    @Override
    public SelectQueryBuilder select() {
        if (!this.isSelectInitialized) {
            this.query = "SELECT *";
        } else {
            LOGGER.warn("Select part is already initialized. Query is : " + this.query);

        }
        this.isSelectInitialized = true;
        return this;
    }

    @Override
    public SelectQueryBuilder from(final Table table) throws QueryBuilderException {

        if (!this.isSelectInitialized) {
            throw new QueryBuilderException("Select part of query has not been initialized");
        }

        if (!this.isFromInitialized) {
            this.query += " FROM " + table.getName();
        }

        this.isFromInitialized = true;
        return this;
    }

    @Override
    public QueryBuilder where() throws QueryBuilderException {
        if (!this.isWhereInitialized) {
            this.query += " WHERE ";
            this.isWhereInitialized = true;
        }
        return this;
    }

    @Override
    public QueryBuilder where(final Criterion criterion) throws QueryBuilderException {

        if (!this.isFromInitialized) {
            throw new QueryBuilderException("From part of query has not been initialized");
        }

        this.where();

        addCriterion(criterion);
        this.isWhereInitialized = true;
        return this;
    }

    @Override
    public QueryBuilder and() throws QueryBuilderException {

        if (!this.separatorInitialized && this.criteriaInitialized) {

            if (!this.isWhereInitialized) {
                throw new QueryBuilderException("Where part of query has not been initialized");
            }

            this.query += " " + new AndSeparator().getValue() + " ";
            this.separatorInitialized = true;
        } else {
            LOGGER.warn("separator has been already initialized. Query is : " + this.query);
        }
        return this;
    }

    @Override
    public QueryBuilder addWhereCriterion(final Criterion criterion) throws QueryBuilderException {

        if (!this.isFromInitialized) {
            throw new QueryBuilderException("From part of query has not been initialized");
        }

        this.where();

        addCriterion(criterion);
        this.criteriaInitialized = true;
        this.separatorInitialized = false;
        return this;
    }

    private void addCriterion(final Criterion criterion) {

        //add 'name  ='
        this.query += criterion.getName() + " " + criterion.getOperator();

        //Add value
        if (criterion.getValue() instanceof String) {
            this.query += " '" + criterion.getValue() + "'";
        } else if (criterion.getValue() instanceof JSONObject) {
            this.query += " '" + criterion.getValue().toString() + "'";
        } else {
            this.query += " " + criterion.getValue();
        }

    }

    @Override
    public String getQuery() {
        return this.query;
    }
}

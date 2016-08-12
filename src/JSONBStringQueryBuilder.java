package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.sql.builder.JSONBAliasHelper;
import com.fstn.common.utils.sql.builder.JSONBHelper;
import com.fstn.common.utils.sql.builder.exception.InvalidFromException;
import com.fstn.common.utils.sql.builder.exception.InvalidTableException;
import com.fstn.common.utils.sql.builder.exception.InvalidWhereException;
import com.fstn.common.utils.sql.builder.model.criterion.impl.WhereCriterion;
import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryContent;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryMode;
import com.fstn.common.utils.sql.builder.model.query.JSONBSelect;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.Table;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Construct query string from JsonBQuery object Created by sza on 09/08/2016.
 */
public class JSONBStringQueryBuilder
{
    /**
     * Create query from object
     *
     * @param jsonbQuery jsonbQuery
     * @return string
     */
    public static String createQuery(JSONBQuery jsonbQuery) {
        String query = "";

        String select = createSelect(jsonbQuery.getSelect());
        String content = createContent(jsonbQuery.getTable(), jsonbQuery);
        String where = createWhereBasedOnAlias(jsonbQuery.getWhere());

        if (where.length() > 0) {
            where = " WHERE " + where;
        }

        query = "SELECT" + select + " FROM " + content + where;
        return query;
    }

    /**
     * create select part of query
     *
     * @param select select
     * @return select
     */
    private static String createSelect(List<JSONBSelect> select) {
        String selectResult = "";
        if (select.size() == 0) {
            selectResult = " *";
        } else {
            for (JSONBSelect selectPart : select) {
                if (selectPart == null || selectPart.getAliasedField() == null || selectPart.getAliasedField()
                                                                                            .isEmpty()) {
                    selectResult = " *";
                }else {
                    selectResult += selectResult.length() > 0 ? "," : "";
                    selectResult += " " + selectPart.getAliasedField();
                }

            }
        }
        return selectResult;
    }

    /**
     * create from part of query
     *
     * @param table table
     * @param query query
     * @return content
     */
    private static String createContent(Class table, JSONBQuery query) {
        String contentResult = "";

        if (table == null) {
            throw new InvalidTableException("Table can't be null)");
        }
        String tableName = table.getSimpleName();

        /**
         * Try get name of table declare inside column annotation
         */
        for (Annotation annotation : Arrays.asList(table.getDeclaredAnnotations())) {
            if (annotation instanceof Table) {
                tableName = ((Table) annotation).name();
            }
        }
        /**
         * NORMAL MODE WITH QUERY
         */
        if (query.getMode().equals(JSONBQueryMode.SIMPLE)) {
            contentResult += tableName + " ";
            /**
             * FAST MODE WITH SUBQUERY
             */
        } else if (query.getMode().equals(JSONBQueryMode.FAST)) {
            contentResult = "( SELECT * FROM ";
            contentResult += tableName + " ";
            String subWhereBasedOnFields = createWhereBasedOnFields(query.getWhere());
            if (subWhereBasedOnFields.length() > 0) {
                contentResult += "WHERE ";
            }
            contentResult += subWhereBasedOnFields;
            contentResult += " ) AS \"" + tableName + "\"";
        }
        for (JSONBQueryContent contentPart : query.getContent()) {
            contentResult += contentResult.length() > 0 ? "," : "";

            if (contentPart.getAliasedFields() == null || contentPart.getAliasedFields().isEmpty()) {
                throw new InvalidFromException("From alias field can't be null");
            }
            if (contentPart.getAlias() == null || contentPart.getAlias().isEmpty()) {
                throw new InvalidFromException("From alias can't be null");
            }

            contentResult += JSONBAliasHelper.toJSONBArray(contentPart.getAliasedFields(), contentPart.getAlias());
        }
        return contentResult;
    }

    private static String createWhereBasedOnFields(List<WhereCriterion> where) {

        String whereResult = "";
        for (WhereCriterion wherePart : where) {
            if (wherePart.getRsqlSelector() == null || wherePart.getRsqlSelector().isEmpty()) {
                throw new InvalidWhereException("Where fields can't be null");
            }
            if (wherePart.getOperator() == null || wherePart.getOperator().isEmpty()) {
                throw new InvalidWhereException("Operator can't be null");
            }
            String rsqlSelector = wherePart.getRsqlSelector();
            String rsqlSelectorStoppedOnFirstArray = JSONBAliasHelper.extractFirstArrayRsql(rsqlSelector);
            String rsqlSelectorAfterFirstArray = JSONBAliasHelper.extractAfterFirstArrayRsql(rsqlSelector);
            String jsonbFieldsStoppedOnFirstArray = JSONBAliasHelper.extractPath(rsqlSelectorStoppedOnFirstArray);
            if (rsqlSelectorAfterFirstArray.endsWith("@") && rsqlSelectorAfterFirstArray
                .lastIndexOf(".") != -1) {
                rsqlSelectorAfterFirstArray = rsqlSelectorAfterFirstArray.substring(0, rsqlSelectorAfterFirstArray
                    .lastIndexOf("."));
            }
            String jsonObjectAfterFirstArray = JSONBHelper.transformRSQLToObject(rsqlSelectorAfterFirstArray,
                                                                                 wherePart.getValue());
            whereResult += whereResult.length() > 0 ? " and " : "";
            //add 'name  ='
            whereResult += jsonbFieldsStoppedOnFirstArray + " " + wherePart.getOperator();

            String value = JSONBHelper.transformRSQLToObject(rsqlSelectorAfterFirstArray, wherePart.getValue());
            if (rsqlSelectorStoppedOnFirstArray.endsWith("]")) {
                whereResult = addWhereValue(whereResult, new JSONArray("[" + value + "]"));
            } else {
                whereResult = addWhereValue(whereResult, value);
            }
        }
        return whereResult;
    }

    private static String addWhereValue(String whereResult, Object wherePart) {
        if (wherePart instanceof String) {
            whereResult += " '" + wherePart + "'";
        } else if (wherePart instanceof JSONObject || wherePart instanceof JSONArray) {
            whereResult += " '" + wherePart.toString() + "'";
        } else {
            whereResult += " " + wherePart;
        }
        return whereResult;
    }

    /**
     * Create where part of query
     *
     * @param where where
     * @return where
     */
    private static String createWhereBasedOnAlias(List<WhereCriterion> where) {
        String whereResult = "";
        for (WhereCriterion wherePart : where) {
            if (wherePart.getName() == null || wherePart.getName().isEmpty()) {
                throw new InvalidWhereException("Where name can't be null");
            }
            if (wherePart.getOperator() == null || wherePart.getOperator().isEmpty()) {
                throw new InvalidWhereException("Operator can't be null");
            }
            whereResult += whereResult.length() > 0 ? " and " : "";
            //add 'name  ='
            whereResult += wherePart.getName() + " " + wherePart.getOperator();
            whereResult = addWhereValue(whereResult, wherePart.getValue());
        }
        return whereResult;
    }
}

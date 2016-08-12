package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.rsql.nativequery.entity.Invoice;
import com.fstn.common.utils.sql.builder.exception.InvalidTableException;
import com.fstn.common.utils.sql.builder.model.criterion.impl.WhereCriterion;
import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryContent;
import com.fstn.common.utils.sql.builder.model.query.JSONBSelect;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

/**
 * JSONB String Builder Test
 * Created by sza on 09/08/2016.
 */
public class JSONBStringQueryBuilderTest
{

    private JSONBQuery query;

    @Before
    public void setUp() throws Exception {
        query = new JSONBQuery();
        query.setTable(Invoice.class);

        JSONBSelect select = new JSONBSelect("content.invoiceLines[].array[]");
        select.setAliasedField("\"content.invoiceLines[]\"->'array'");
        query.setSelect(Collections.singletonList(select));

        WhereCriterion<String> criterion =new WhereCriterion<>();
        criterion.setName("\"content.invoiceLines[]\"->'array'");
        criterion.setOperator("=");
        criterion.setValue("201615154");

        query.setWhere(Collections.singletonList(criterion));
        JSONBQueryContent content = new JSONBQueryContent();
        content.setRSQL("content.invoiceLines[]");
        content.setFields("content->'invoiceLines'");
        content.setAliasedFields("content->'invoiceLines'");
        content.setAlias("\"content.invoiceLines[]\"");
        query.getContent().add(content);

        JSONBQueryContent content2;
        content2 = new JSONBQueryContent();
        content2.setRSQL("content.invoiceLines[].array[]");
        content2.setFields("content->'invoiceLines'->'array'");
        content2.setAliasedFields("\"content.invoiceLines[]\"->'array'");
        content2.setAlias("\"content.invoiceLines[].array[]\"");
        query.getContent().add(content2);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateQuery() throws Exception {
        String expected;
        expected = "SELECT \"content.invoiceLines[]\"->'array' FROM INVOICE ,jsonb_array_elements(content->'invoiceLines') AS \"content" +
            ".invoiceLines[]\"," +
            "jsonb_array_elements(\"content.invoiceLines[]\"->'array') AS \"content.invoiceLines[].array[]\" WHERE " +
            "\"content.invoiceLines[]\"->'array' = '201615154'";

        String result = JSONBStringQueryBuilder.createQuery(query);
        Assert.assertEquals(expected,result);
    }


    @Test(expected = InvalidTableException.class)
    public void testCreateQueryException() throws Exception {
        JSONBQuery queryWithoutTable = new JSONBQuery();
        queryWithoutTable.setContent(query.getContent());
        JSONBStringQueryBuilder.createQuery(queryWithoutTable);
        Assert.fail();
    }

    @Test
    public void testCreateQueryNoWhere() throws Exception {
        JSONBQuery queryWithoutWhere = new JSONBQuery();
        queryWithoutWhere.setContent(query.getContent());
        queryWithoutWhere.setTable(query.getTable());
        queryWithoutWhere.setSelect(query.getSelect());
        String expected;
        expected = "SELECT \"content.invoiceLines[]\"->'array' FROM INVOICE ,jsonb_array_elements" +
            "(content->'invoiceLines') AS \"content.invoiceLines[]\",jsonb_array_elements(\"content" +
            ".invoiceLines[]\"->'array') AS \"content.invoiceLines[].array[]\"";

        String result = JSONBStringQueryBuilder.createQuery(queryWithoutWhere);
        Assert.assertEquals(expected,result);
    }

    @Test
    public void testCreateQueryAliasedSelect() throws Exception {
        JSONBQuery queryAliasedSelect = new JSONBQuery();
        JSONBSelect select = new JSONBSelect("content.invoiceLines[]");
        select.setAliasedField("\"content.invoiceLines[]\"");
        queryAliasedSelect.setSelect(Collections.singletonList(select));
        queryAliasedSelect.setContent(query.getContent());
        queryAliasedSelect.setTable(query.getTable());
        queryAliasedSelect.setWhere(query.getWhere());

        String expected;
        expected = "SELECT \"content.invoiceLines[]\" FROM INVOICE ,jsonb_array_elements(content->'invoiceLines') AS " +
            "\"content.invoiceLines[]\",jsonb_array_elements(\"content.invoiceLines[]\"->'array') AS \"content" +
            ".invoiceLines[].array[]\" WHERE \"content.invoiceLines[]\"->'array' = '201615154'";

        String result = JSONBStringQueryBuilder.createQuery(queryAliasedSelect);
        Assert.assertEquals(expected,result);
    }

    @Test
    public void testCreateQueryNoSelect() throws Exception {
        JSONBQuery queryWithoutWhere = new JSONBQuery();
        queryWithoutWhere.setContent(query.getContent());
        queryWithoutWhere.setTable(query.getTable());
        queryWithoutWhere.setWhere(query.getWhere());
        String expected;
        expected = "SELECT * FROM INVOICE ,jsonb_array_elements(content->'invoiceLines') AS \"content" +
            ".invoiceLines[]\"," +
            "jsonb_array_elements(\"content.invoiceLines[]\"->'array') AS \"content.invoiceLines[].array[]\" WHERE " +
            "\"content.invoiceLines[]\"->'array' = '201615154'";

        String result = JSONBStringQueryBuilder.createQuery(queryWithoutWhere);
        Assert.assertEquals(expected,result);
    }
}
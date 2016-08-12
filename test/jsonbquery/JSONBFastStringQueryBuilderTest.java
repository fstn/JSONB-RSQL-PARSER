package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.rsql.nativequery.entity.Invoice;
import com.fstn.common.utils.sql.builder.model.criterion.impl.WhereCriterion;
import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryContent;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryMode;
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
public class JSONBFastStringQueryBuilderTest
{

    private JSONBQuery query;

    @Before
    public void setUp() throws Exception {
        query = new JSONBQuery();

        query.setMode(JSONBQueryMode.FAST);

        query.setTable(Invoice.class);

        JSONBSelect select = new JSONBSelect("content.invoiceLines[].array[].obj{}");
        select.setAliasedField("\"content.invoiceLines[].array[]\"->'obj'");
        query.setSelect(Collections.singletonList(select));

        WhereCriterion<String> criterion =new WhereCriterion<>();
        criterion.setName("\"content.invoiceLines[].array[]\"->'obj{}'");
        criterion.setRsqlSelector("content.invoiceLines[].array[].obj{}");
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
        expected = "SELECT \"content.invoiceLines[].array[]\"->'obj' FROM ( SELECT * FROM INVOICE WHERE " +
            "content->'invoiceLines' = '[{\"array\":[{\"obj\":201615154}]}]' ) AS \"INVOICE\",jsonb_array_elements" +
            "(content->'invoiceLines') AS \"content.invoiceLines[]\",jsonb_array_elements(\"content" +
            ".invoiceLines[]\"->'array') AS \"content.invoiceLines[].array[]\" WHERE \"content.invoiceLines[]" +
            ".array[]\"->'obj{}' = '201615154'";

        String result = JSONBStringQueryBuilder.createQuery(query);
        Assert.assertEquals(expected,result);
    }

}
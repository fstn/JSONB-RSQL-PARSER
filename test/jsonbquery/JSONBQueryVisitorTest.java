/*
 * The MIT License
 *
 * Copyright 2015 Antonio Rabelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.rsql.jpa.EntityManagerFactoryInitializer;
import com.fstn.common.utils.rsql.nativequery.AbstractVisitorJsonbTest;
import com.fstn.common.utils.rsql.nativequery.entity.Invoice;
import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;

/**
 * @author AntonioRabelo
 */
@Ignore
public class JSONBQueryVisitorTest extends AbstractVisitorJsonbTest<Invoice>
{

    @Before
    public void setUp() throws Exception {

        entityManager = EntityManagerFactoryInitializer.getEntityManagerFactory(PERSISTENCE_UNIT_JSONB)
                                                       .createEntityManager();
        entityClass = Invoice.class;
    }

    @Test
    public void testInvoiceQueryJsonSimpleSelection() throws Exception {
        String rsqlQuery;
        Node rootNode;
        RSQLVisitor<JSONBQuery, EntityManager> visitor;
        JSONBQuery query;

        rsqlQuery = "content.header{}.id@=='\"201607201638331\"'";
        rootNode = new RSQLParser().parse(rsqlQuery);
        visitor = new JSONBQueryVisitor(Invoice.class);
        query = rootNode.accept(visitor, entityManager);

        Assert.assertEquals("Query must no have a content", 0, query.getContent().size());

        rsqlQuery = "content.invoiceLines[].array[].tax{}.id@=='\"201607201638331\"'";
        rootNode = new RSQLParser().parse(rsqlQuery);
        visitor = new JSONBQueryVisitor(Invoice.class);
        query = rootNode.accept(visitor, entityManager);

        Assert.assertEquals("Query must have 2 alias", 2, query.getContent().size());
        Assert.assertEquals("Query must have a where criterion", 1, query.getWhere().size());
        Assert.assertEquals("Query must a correct criterion name",
                            "\"content.invoiceLines[].array[]\"->'tax'",
                            query.getWhere().get(0).getName());
        Assert.assertEquals("Query must a correct criterion value",
                            "{\"id\":\"201607201638331\"}",
                            query.getWhere().get(0).getValue().toString());

        /*

        final List invoices = query.getResultList();
        assertTrue("Testing invoices should not be empty", !invoices.isEmpty());
        final Invoice invoice = (Invoice) invoices.get(0);
        assertNotNull("Testing invoices", invoice.getTenant());
        assertEquals("Tenant of invoice should not be null", invoice.getTenant().getId(), new Long(1));
    */
    }

    @Test
    public void testInvoiceQueryAndJsonSimpleSelection() throws Exception {
        String rsqlQuery;
        Node rootNode;
        RSQLVisitor<JSONBQuery, EntityManager> visitor;
        JSONBQuery query;

        rsqlQuery = "content.header{}.id@==\"201607201638331\"";
        rootNode = new RSQLParser().parse(rsqlQuery);
        visitor = new JSONBQueryVisitor(Invoice.class);
        query = rootNode.accept(visitor, entityManager);

        Assert.assertEquals("Query must no have a content", 0, query.getContent().size());

        rsqlQuery = "content.invoiceLines[].array[].tax{}.id@==\"201607201638331\" " +
            "and content.invoiceLines[].array[].tax{}.name@=='\"name\"'";
        rootNode = new RSQLParser().parse(rsqlQuery);
        visitor = new JSONBQueryVisitor(Invoice.class);
        query = rootNode.accept(visitor, entityManager);

        Assert.assertEquals("Query must have 2 alias",2, query.getContent().size());
        Assert.assertEquals("Query must have a where criterion", 2, query.getWhere().size());
        Assert.assertEquals("Query must a correct criterion name",
                            "\"content.invoiceLines[].array[]\"->'tax'",
                            query.getWhere().get(0).getName());


        rsqlQuery = "content.invoiceLines[].array[].tax{}.id@==\"201607201638331\" and content.header[].array[].tax{}" +
            ".name@=='\"titi\"'";
        rootNode = new RSQLParser().parse(rsqlQuery);
        visitor = new JSONBQueryVisitor(Invoice.class);
        query = rootNode.accept(visitor, entityManager);

        Assert.assertEquals("Query must have 2 alias", 4, query.getContent().size());

        Assert.assertEquals("\"content.invoiceLines[]\"",query.getContent().get(0).getAlias());
        Assert.assertEquals("\"content.invoiceLines[].array[]\"",query.getContent().get(1).getAlias());
        Assert.assertEquals("\"content.header[]\"",query.getContent().get(2).getAlias());
        Assert.assertEquals("\"content.header[].array[]\"",query.getContent().get(3).getAlias());
        Assert.assertEquals("Query must have a where criterion", 2, query.getWhere().size());
        Assert.assertEquals("Query must a correct criterion name",
                            "\"content.invoiceLines[].array[]\"->'tax'",
                            query.getWhere().get(0).getName());

        /*

        final List invoices = query.getResultList();
        assertTrue("Testing invoices should not be empty", !invoices.isEmpty());
        final Invoice invoice = (Invoice) invoices.get(0);
        assertNotNull("Testing invoices", invoice.getTenant());
        assertEquals("Tenant of invoice should not be null", invoice.getTenant().getId(), new Long(1));
    */
    }

}

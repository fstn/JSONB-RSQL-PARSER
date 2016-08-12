package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.rsql.jpa.EntityManagerFactoryInitializer;
import com.fstn.common.utils.rsql.nativequery.AbstractVisitorJsonbTest;
import com.fstn.common.utils.rsql.nativequery.entity.Invoice;
import com.fstn.common.utils.rsql.nativequery.entity.TaxDTO;
import com.fstn.common.utils.rsql.nativequery.entity.Tenant;
import com.fstn.common.utils.rsql.nativequery.entity.TenantDTO;
import com.fstn.common.utils.serializer.DynamicSerializer;
import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryMode;
import com.fstn.common.utils.sql.builder.model.query.JSONBSelect;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test Created by sza on 09/08/2016.
 */
@Ignore
public class JSONBQueryVisitorTI extends AbstractVisitorJsonbTest<Invoice>
{
    Invoice invoice;

    @Test
    public void testInvoiceJSONBQueryStandardQueryMultiSelectionEmptyResult() throws Exception {
        final String rsqlQuery = "content.invoiceLines[].tax{}.id==16";
        final RSQLVisitor<JSONBQuery, EntityManager> visitor =
            new JSONBQueryVisitor(Invoice.class, Collections.singletonList(
                new JSONBSelect("content.invoiceLines[]" + ".tax{}")));
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        final JSONBQuery jsonbQuery = rootNode.accept(visitor, entityManager);
        Query query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        final List<Object[]> list = (List<Object[]>) query.getResultList();
        final List<TaxDTO> listOfTax = new ArrayList<>();

        assertTrue("Testing object before mapping should be empty", list.isEmpty());
        final DynamicSerializer<String, TaxDTO> dynamicSerializer =
            new DynamicSerializer<>(String.class, TaxDTO.class);
        for (Object elt : list) {
            TaxDTO dynamicEntity = dynamicSerializer.deSerialize(elt.toString());
            listOfTax.add(dynamicEntity);
        }
        assertTrue("Testing tax should be empty", listOfTax.isEmpty());
    }

    @Test
    public void testInvoiceJSONBQueryStandardSelect() throws Exception {

        final String rsqlQuery = "content.invoiceLines[].tax{}.id@==9";
        List<JSONBSelect> selectClause = Collections.singletonList(new JSONBSelect("content.invoiceLines[]"));
        final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        final JSONBQuery jsonbQuery = rootNode.accept(visitor, entityManager);
        Query query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        final List<Object[]> list = (List<Object[]>) query.getResultList();
        final List<TaxDTO> listOfTax = new ArrayList<>();

        assertTrue("Testing object before mapping should not be empty", !list.isEmpty());
    }

    @Test
    public void testInvoiceJSONBQueryStandardSelectTenant() throws Exception {

        final String rsqlQuery = "content.header{}.tenant{}.id@==1";
        List<JSONBSelect> selectClause = Collections.singletonList(new JSONBSelect("content.header{}.tenant{}.id@"));
        final JSONBQuery jsonbQuery;
        final List<Object[]> listModeNormal;
        final List<Object[]> listModeFast;
        final List<TaxDTO> listOfTax;
        Query query;
        final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        listModeNormal = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeNormal.isEmpty());

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        listModeFast = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeFast.isEmpty());

        assertEquals("Fast mode en normal mode must return the same number of result", listModeNormal.size(),
                     listModeFast
                         .size());
    }

    @Test
    public void testInvoiceJSONBQueryStandardQSelectAttribute() throws Exception {

        final String rsqlQuery = "content.invoiceLines[].tax{}.id@==9";
        List<JSONBSelect> selectClause = Collections.singletonList(new JSONBSelect("content.id@"));
        final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        final JSONBQuery jsonbQuery;
        final List<Object[]> listModeNormal;
        final List<Object[]> listModeFast;
        final List<TaxDTO> listOfTax;
        Query query;
        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        listModeNormal = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeNormal.isEmpty());

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        listModeFast = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeFast.isEmpty());

        assertEquals("Fast mode en normal mode must return the same number of result", listModeNormal.size(),
                     listModeFast
                         .size());

    }

    @Test
    public void testInvoiceJSONBQueryMultipleValue() throws Exception {

        final JSONBQuery jsonbQuery;
        final List<Object[]> listModeNormal;
        final List<Object[]> listModeFast;
        final List<TaxDTO> listOfTax;
        Query query;

        final String rsqlQuery = "content.lines[].orderNbr@=='\"S31992134\"';content.tenant[].id@==1";
        final RSQLVisitor<JSONBQuery, EntityManager> visitor =
            new JSONBQueryVisitor(Invoice.class, Collections.singletonList(
                new JSONBSelect("content.invoiceLines[]" + ".tax{}")));
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        listModeNormal = (List<Object[]>) query.getResultList();
        listOfTax = new ArrayList<>();

        assertTrue("Testing object before mapping should be empty", listModeNormal.isEmpty());
        final DynamicSerializer<String, TaxDTO> dynamicSerializer =
            new DynamicSerializer<>(String.class, TaxDTO.class);
        for (Object elt : listModeNormal) {
            TaxDTO dynamicEntity = dynamicSerializer.deSerialize(elt.toString());
            listOfTax.add(dynamicEntity);
        }
        assertTrue("Testing tax should be empty", listOfTax.isEmpty());

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        listModeFast = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", listModeFast.isEmpty());

        assertEquals("Fast mode en normal mode must return the same number of result", listModeNormal.size(),
                     listModeFast
                         .size());

    }

    @Test
    public void testInvoiceJSONBQueryStandardQueryMultiSelection() throws Exception {

        final JSONBQuery jsonbQuery;
        final List<Object[]> listModeNormal;
        final List<Object[]> listModeFast;
        final List<TaxDTO> listOfTax;
        Query query;

        final String rsqlQuery = "content.invoiceLines[].tax{}.id@==9";
        List<JSONBSelect> selectClause = Collections.singletonList(new JSONBSelect("content.invoiceLines[].tax{}"));
        final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        listModeNormal = (List<Object[]>) query.getResultList();
        listOfTax = new ArrayList<>();

        assertTrue("Testing object before mapping should not be empty", !listModeNormal.isEmpty());
        final DynamicSerializer<String, TaxDTO> dynamicSerializer =
            new DynamicSerializer<String, TaxDTO>(String.class, TaxDTO.class);
        for (Object elt : listModeNormal) {
            TaxDTO dynamicEntity = dynamicSerializer.deSerialize(elt.toString());
            listOfTax.add(dynamicEntity);
        }
        assertTrue("Testing tax should not be empty", !listOfTax.isEmpty());

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        listModeFast = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeFast.isEmpty());

        assertEquals("Fast mode en normal mode must return the same number of result", listModeNormal.size(),
                     listModeFast
                         .size());
    }

    @Test
    public void testInvoiceJSONBQueryStandardDepthSearch() throws Exception {

        final JSONBQuery jsonbQuery;
        final List<Object[]> listModeNormal;
        final List<Object[]> listModeFast;
        final List<TaxDTO> listOfTax;
        Query query;

        final String rsqlQuery = "content.invoiceLines[].obj{}.array[].obj{}.array[].array[].obj{}.id@==1";

        final Node rootNode = new RSQLParser().parse(rsqlQuery);

        final RSQLVisitor<JSONBQuery, EntityManager> visitor =
            new JSONBQueryVisitor(Invoice.class, Collections.singletonList(
                new JSONBSelect("content.invoiceLines[]" + ".tax{}")));

        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        listModeNormal = (List<Object[]>) query.getResultList();
        listOfTax = new ArrayList<>();

        assertTrue("Testing object before mapping should not be empty", !listModeNormal.isEmpty());
        final DynamicSerializer<String, TaxDTO> dynamicSerializer =
            new DynamicSerializer<>(String.class, TaxDTO.class);
        for (Object elt : listModeNormal) {
            TaxDTO dynamicEntity = dynamicSerializer.deSerialize(elt.toString());
            listOfTax.add(dynamicEntity);
        }
        assertTrue("Testing tax should not be empty", !listOfTax.isEmpty());

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        listModeFast = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeFast.isEmpty());

        assertEquals("Fast mode en normal mode must return the same number of result", listModeNormal.size(),
                     listModeFast
                         .size());
    }

    @Test
    public void testInvoiceJSONBQueryStandardQueryNullSelector() throws Exception {

        final JSONBQuery jsonbQuery;
        final List<Object[]> listModeNormal;
        final List<Object[]> listModeFast;
        final List<TaxDTO> listOfTax;
        Query query;

        final String rsqlQuery = "content.invoiceLines[].tax{}.id@==9";
        List<JSONBSelect> selectClause = Collections.singletonList(null);
        final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        listModeNormal = (List<Object[]>) query.getResultList();
        listOfTax = new ArrayList<>();

        assertTrue("Testing object before mapping should not be empty", !listModeNormal.isEmpty());

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        listModeFast = (List<Object[]>) query.getResultList();
        assertTrue("Testing object before mapping should not be empty", !listModeFast.isEmpty());

        assertEquals("Fast mode en normal mode must return the same number of result", listModeNormal.size(),
                     listModeFast
                         .size());
    }

    @Test
    public void testInvoiceJSONBQueryStandardQueryTenantCode() throws Exception {

        final JSONBQuery jsonbQuery;
        final String resultModeNormal;
        final String resultModeFast;
        TenantDTO resultTenant = null;
        DynamicSerializer<String, TenantDTO> dynamicSerializer;
        Query query;

        final String rsqlQuery = "content.header{}.tenant{}.code@=='\"ACME\"'";
        List<JSONBSelect> selectClause = Collections.singletonList(new JSONBSelect("content.header{}.tenant{}"));
        final RSQLVisitor<JSONBQuery, EntityManager> visitor = new JSONBQueryVisitor(Invoice.class, selectClause);
        final Node rootNode = new RSQLParser().parse(rsqlQuery);
        jsonbQuery = rootNode.accept(visitor, entityManager);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);

        resultModeNormal = (String) query.getSingleResult();

        assertTrue("Testing object before mapping should not be empty", resultModeNormal != null);
        dynamicSerializer =
            new DynamicSerializer<String, TenantDTO>(String.class, TenantDTO.class);
        resultTenant = dynamicSerializer.deSerialize(resultModeNormal);
        assertTrue("Testing tax should not be empty", resultTenant.getCode().equals("ACME"));

        jsonbQuery.setMode(JSONBQueryMode.FAST);
        query = JSONBQueryBuilder.createQuery(jsonbQuery, entityManager);
        resultModeFast = (String) query.getSingleResult();
        assertTrue("Testing tenant should not be empty", resultTenant.getCode().equals("ACME"));
        dynamicSerializer =
            new DynamicSerializer<String, TenantDTO>(String.class, TenantDTO.class);
        resultTenant = dynamicSerializer.deSerialize(resultModeNormal);
        assertTrue("Testing tenant should not be empty", resultTenant.getCode().equals("ACME"));

        assertEquals("Fast mode en normal mode must return the same number of result", resultModeNormal.toString(),
                     resultModeFast.toString());
    }
    @Before
    public void setUp() throws Exception {
        entityManager = EntityManagerFactoryInitializer.getEntityManagerFactory(PERSISTENCE_UNIT_JSONB)
                                                       .createEntityManager();

        invoice = new Invoice();

        Tenant tenant = entityManager.find(Tenant.class, 1);
        invoice.setTenant(tenant);
        invoice.setCode("code");
        invoice.setScanDate(new Date());
        invoice.setNetAmount(12.0);
        invoice.setTotalAmount(12.0);
        invoice.setVatAmount(12.0);
        //language=json
        String json = "{  \n" +
            "   \"id\":9, \n" +
            "   \"header\":{  \n" +
            "      \"id\":\"201607201638331\",\n" +
            "      \"tenant\":{  \n" +
            "         \"id\":1,\n" +
            "         \"code\":\"ACME\"\n" +
            "      },\n" +
            "      \"company\":{  \n" +
            "         \"code\":\"001\",\n" +
            "         \"name\":\"ACME DE FRANCE ET DE NAVARRE\",\n" +
            "         \"site\":\"001AMI\",\n" +
            "         \"service\":\"ACME_AMIENS_PROD\"\n" +
            "      },\n" +
            "      \"currency\":\"EUR\",\n" +
            "      \"scanDate\":\"2016-02-15T00:00:00Z\",\n" +
            "      \"supplier\":{  \n" +
            "         \"name\":\"EDF\",\n" +
            "         \"docNumber\":\"FAC1000006\",\n" +
            "         \"externalId\":\"12345\",\n" +
            "         \"invoiceDate\":\"2016-02-15T00:00:00Z\",\n" +
            "         \"alternativeName\":\"001001\"\n" +
            "      },\n" +
            "      \"batchName\":\"20160720163833\",\n" +
            "      \"netAmount\":100000,\n" +
            "      \"vatAmount\":18550,\n" +
            "      \"thirdParty\":{  \n" +
            "         \"code\":\"012\",\n" +
            "         \"name\":\"EDF Région Nord\"\n" +
            "      },\n" +
            "      \"totalAmount\":118550,\n" +
            "      \"documentType\":\"INV\",\n" +
            "      \"invoiceOrigin\":\"SCANNER\",\n" +
            "      \"receptionDate\":\"2016-02-15T00:00:00Z\",\n" +
            "      \"detectedDocumentCategory\":\"WOPO\"\n" +
            "   },\n" +
            "   \"footers\":[  \n" +
            "\n" +
            "   ],\n" +
            "   \"taxLines\":[  \n" +
            "      {  \n" +
            "         \"rate\":20,\n" +
            "         \"applyTax\":true,\n" +
            "         \"lineType\":\"NET\",\n" +
            "         \"netAmount\":90000\n" +
            "      },\n" +
            "      {  \n" +
            "         \"rate\":5.5,\n" +
            "         \"applyTax\":true,\n" +
            "         \"lineType\":\"NET\",\n" +
            "         \"netAmount\":10000\n" +
            "      }\n" +
            "   ],\n" +
            "   \"invoiceLines\":[  \n" +
            "      {  \n" +
            "         \"id\":\"ID1\",\n" +
            "        \"obj\":{\n" +
            "          \"array\":[\n" +
            "            {\"obj\":{\n" +
            "              \"array\":[{\n" +
            "                \"array\":[\n" +
            "                  {\"obj\":{\"id\":1}\n" +
            "                  }]\n" +
            "              }\n" +
            "                ]              \n" +
            "            }}\n" +
            "          ]\n" +
            "        },\n" +
            "         \"tax\":{  \n" +
            "            \"id\":9,\n" +
            "            \"code\":\"D5.5\",\n" +
            "            \"rate\":20\n" +
            "         },\n" +
            "         \"lineType\":\"NET\",\n" +
            "         \"position\":1,\n" +
            "         \"netAmount\":90000,\n" +
            "         \"secondaryLineType\":\"NETWITHOUTQTY\",\n" +
            "         \"estimatedTaxAmount\":18000,\n" +
            "         \"estimatedTotalAmount\":108000\n" +
            "      },\n" +
            "      {  \n" +
            "         \"id\":\"ID2\",\n" +
            "         \"tax\":{  \n" +
            "            \"id\":15,\n" +
            "            \"code\":\"D5.5\",\n" +
            "            \"rate\":5.5,\n" +
            "            \"text\":[  \n" +
            "               {  \n" +
            "                  \"obj\":{  \n" +
            "                     \"name\":\"Paul\"\n" +
            "                  },\n" +
            "                  \"value\":1\n" +
            "               }\n" +
            "            ]\n" +
            "         },\n" +
            "         \"lineType\":\"NET\",\n" +
            "         \"position\":2,\n" +
            "         \"netAmount\":10000,\n" +
            "         \"secondaryLineType\":\"NETWITHOUTQTY\",\n" +
            "         \"estimatedTaxAmount\":550,\n" +
            "         \"estimatedTotalAmount\":10550\n" +
            "      },\n" +
            "      {  \n" +
            "         \"test\":{  \n" +
            "            \"testArray\":[  \n" +
            "               {  \n" +
            "                  \"coucou\":{  \n" +
            "                     \"bla\":\"youpi\"\n" +
            "                  }\n" +
            "               }\n" +
            "            ]\n" +
            "         }\n" +
            "      }\n" +
            "   ]\n" +
            "}";
        invoice.setContent(new JSONObject(json));

        entityManager.getTransaction().begin();
        //entityManager.persist(invoice);
        entityManager.getTransaction().commit();
        entityClass = Invoice.class;
    }

    @After
    public void tearDown() throws Exception {
        entityManager = EntityManagerFactoryInitializer.getEntityManagerFactory(PERSISTENCE_UNIT_JSONB)
                                                       .createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(invoice);
        entityManager.getTransaction().commit();
        entityClass = Invoice.class;
    }
}

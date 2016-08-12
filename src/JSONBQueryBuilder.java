package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Builder for JSONB Query
 * Created by sza on 09/08/2016.
 */
public class JSONBQueryBuilder
{
    public static Query createQuery(JSONBQuery jsonbQuery, EntityManager entityManager){
        String queryString =  JSONBStringQueryBuilder.createQuery(jsonbQuery);
        return entityManager.createNativeQuery(queryString);
    }
}

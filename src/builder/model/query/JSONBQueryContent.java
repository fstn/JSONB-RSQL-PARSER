package com.fstn.common.utils.sql.builder.model.query;

/**
 * Represent RSQL JSONB Query Content
 *
 * Created by sza on 08/08/2016.
 */
public class JSONBQueryContent
{
    private String RSQL;
    private String fields;
    private String aliasedFields;
    private String alias;

    public String getRSQL() {
        return RSQL;
    }

    public void setRSQL(String RSQL) {
        this.RSQL = RSQL;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAliasedFields() {
        return aliasedFields;
    }

    public void setAliasedFields(String aliasedFields) {
        this.aliasedFields = aliasedFields;
    }

    @Override
    public String toString() {
        return "JSONBQueryContent{" +
            "RSQL='" + RSQL + '\'' +
            ", fields='" + fields + '\'' +
            ", aliasedFields='" + aliasedFields + '\'' +
            ", alias='" + alias + '\'' +
            '}';
    }
}

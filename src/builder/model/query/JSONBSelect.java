package com.fstn.common.utils.sql.builder.model.query;

/**
 * Select content of jsonb query
 * Created by sza on 09/08/2016.
 */
public class JSONBSelect
{

    private String rsqlSelector;
    private String aliasedField;

    public JSONBSelect(String field) {
        this.rsqlSelector = field;
    }

    public String getRsqlSelector() {
        return rsqlSelector;
    }

    public void setRsqlSelector(String rsqlSelector) {
        this.rsqlSelector = rsqlSelector;
    }

    public String getAliasedField() {
        return aliasedField;
    }

    public void setAliasedField(String aliasedField) {
        this.aliasedField = aliasedField;
    }
}

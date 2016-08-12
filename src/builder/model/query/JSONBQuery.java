package com.fstn.common.utils.sql.builder.model.query;

import com.fstn.common.utils.sql.builder.model.criterion.impl.WhereCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent RSQL JSONB Query
 *
 * Created by sza on 08/08/2016.
 */
public class JSONBQuery
{
    private List<JSONBQueryContent> content;
    private List<WhereCriterion> where;
    private List<JSONBSelect> select;
    private Class table;
    private JSONBQueryMode mode;

    public JSONBQuery() {
        where = new ArrayList<>();
        content = new ArrayList<>();
        select = new ArrayList<>();
        mode = JSONBQueryMode.SIMPLE;
    }

    public List<JSONBQueryContent> getContent() {
        return content;
    }

    public void setContent(List<JSONBQueryContent> content) {
        this.content = content;
    }

    public List<WhereCriterion> getWhere() {
        return where;
    }

    public void setWhere(List<WhereCriterion> where) {
        this.where = where;
    }

    public List<JSONBSelect> getSelect() {
        return select;
    }

    public void setSelect(List<JSONBSelect> select) {
        this.select = select;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }

    public JSONBQueryMode getMode() {
        return mode;
    }

    public void setMode(JSONBQueryMode mode) {
        this.mode = mode;
    }
}

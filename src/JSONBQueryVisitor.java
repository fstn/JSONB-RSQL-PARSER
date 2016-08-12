package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.sql.builder.JSONBAliasHelper;
import com.fstn.common.utils.sql.builder.exception.InvalidSelectException;
import com.fstn.common.utils.sql.builder.model.criterion.Criterion;
import com.fstn.common.utils.sql.builder.model.criterion.impl.WhereCriterion;
import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import com.fstn.common.utils.sql.builder.model.query.JSONBSelect;
import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JSONB Query visitor
 * Created by sza on 08/08/2016.
 */
public class JSONBQueryVisitor implements RSQLVisitor<JSONBQuery, EntityManager>
{

    private JSONBQuery query;
    private Class table;
    private List<JSONBSelect> select;

    public JSONBQueryVisitor(Class table) {
        this.table = table;
        this.select = new ArrayList<>();
    }

    public JSONBQueryVisitor(Class table, List<JSONBSelect> select) {
        this.table = table;
        this.select = select;
    }

    @Override
    public JSONBQuery visit(AndNode andNode, EntityManager param) {
        andNode.getChildren().stream().forEach(node -> {
            if (node instanceof AndNode) {
                this.visit((AndNode) node, param);
            } else if (node instanceof ComparisonNode) {
                this.visit((ComparisonNode) node, param);
            } else {
                this.visit((OrNode) node, param);
            }
        });
        return query;
    }

    @Override
    public JSONBQuery visit(OrNode node, EntityManager param) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JSONBQuery visit(ComparisonNode node, EntityManager param) {
        if (query == null) {
            query = new JSONBQuery();
            query.setTable(table);
            query.setSelect(select);
        }
        return consumeNode(node, param);
    }

    private JSONBQuery consumeNode(ComparisonNode node, EntityManager param) {

        /**
         * Saving alreadyDone
         */
        List<String> alreadyAddedAlias = query.getContent()
                                              .stream()
                                              .map(extractedAlias -> extractedAlias.getRSQL())
                                              .collect(
                                                  Collectors.toList());
        /**
         * Only to list if not already done
         */


        JSONBAliasHelper
            .extractAlias(node.getSelector()).stream()
            .filter(extractedAlias -> !alreadyAddedAlias.contains(extractedAlias.getRSQL()))
            .forEach
                (extractedAlias -> {
                    query.getContent().add(extractedAlias);
                });


        buildSelect(query);

        /**
         * Construct where clause
         */
        Criterion whereCriterion = JSONBWhereCriterionBuilder
            .build(node.getSelector(),JSONBAliasHelper.extractAliasedNameFromRSQL
                (node.getSelector(), query), node
                       .getOperator(), node.getArguments());

        query.getWhere().add((WhereCriterion)whereCriterion);

        return query;
    }

    /**
     * Build select from RSQL notation to jsonb aliased field notation
     *
     * @param query query
     */
    private void buildSelect(JSONBQuery query) {
        /**
         * Transform select from RSQL Notation to JSONB Aliased field
         */
        for (JSONBSelect select : query.getSelect()) {
            if(select != null) {
                String aliasedSelect = JSONBAliasHelper.extractAliasedNameFromRSQL(select.getRsqlSelector(),
                                                                                   query.getContent());
                if (aliasedSelect == null || aliasedSelect.isEmpty()) {
                    throw new InvalidSelectException("Can't find any alias for " + select.getRsqlSelector());
                }
                select.setAliasedField(aliasedSelect);
            }
        }
    }

}

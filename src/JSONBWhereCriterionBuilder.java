package com.fstn.common.utils.rsql.jsonbquery;

import com.fstn.common.utils.rsql.nativequery.EntityHelper;
import com.fstn.common.utils.rsql.parse.ast.ComparisonOperatorProxy;
import com.fstn.common.utils.sql.builder.model.criterion.Criterion;
import com.fstn.common.utils.sql.builder.model.criterion.impl.WhereCriterion;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * JSONB Where Criterion builder
 * Created by sza on 09/08/2016.
 */
public class JSONBWhereCriterionBuilder
{
    public static final String STRING_KEY_INDICATOR = "\"";
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONBWhereCriterionBuilder.class);

    /**
     * Build Criterion for JSONB object
     *
     *
     * @param fields
     * @param name name
     * @param operator operator
     * @param values value
     * @return criterion
     */
    public static Criterion build(String fields, final String name, final ComparisonOperator operator,
                                  final List<String> values) {
        final ComparisonOperatorProxy comparisonOperatorProxy = ComparisonOperatorProxy.asEnum(operator);

        if (comparisonOperatorProxy != null) {
            final String[] items = getNodeItems(name);
            final String criterionName = buildCriterionName(items);

            switch (comparisonOperatorProxy) {
                case EQUAL: {
                    final String convertedOperator = "@>";

                    final String value = values.get(0);
                    if (isString(value)) {
                        final String escapedValue = value.substring(1, value.length() - 1);
                        final WhereCriterion<JSONObject> criterion = new WhereCriterion<>();
                        criterion.setOperator(convertedOperator);
                        criterion.setName(criterionName);
                        criterion.setRsqlSelector(fields);
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put(items[items.length - 1], escapedValue);
                        criterion.setValue(jsonObject);
                        return criterion;
                    } else {

                        if (NumberUtils.isNumber(value)) {
                            if (isInteger(value)) {
                                final WhereCriterion<JSONObject> criterion = new WhereCriterion<>();
                                criterion.setOperator(convertedOperator);
                                criterion.setName(criterionName);
                                criterion.setRsqlSelector(fields);
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put(items[items.length - 1], new Integer(value));
                                criterion.setValue(jsonObject);
                                return criterion;

                            } else {
                                final WhereCriterion<JSONObject> criterion = new WhereCriterion<>();
                                criterion.setOperator(convertedOperator);
                                criterion.setName(criterionName);
                                criterion.setRsqlSelector(fields);
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put(items[items.length - 1], new Double(value));
                                criterion.setValue(jsonObject);
                                return criterion;

                            }

                        } else if (isBoolean(value)) {
                            final WhereCriterion<JSONObject> criterion = new WhereCriterion<>();
                            criterion.setOperator(convertedOperator);
                            criterion.setName(criterionName);
                            criterion.setRsqlSelector(fields);
                            final JSONObject jsonObject = new JSONObject();
                            jsonObject.put(items[items.length - 1], new Boolean(value));
                            criterion.setValue(jsonObject);
                            return criterion;

                        } else {
                            throw new IllegalArgumentException("Unknown criteria type [" + value + "] for jsonb field");
                        }

                    }
                }
            }
        }
        throw new IllegalArgumentException("Unknown operator for jsonb field: " + operator);
    }

    private static String[] getNodeItems(final String path) {

        final String[] items = path.split("\\->");

        /**
         * Remove ' that come from invoiceLines->'tax'
         */
        for (int i =0; i< items.length; i++){
            items[i] = items[i].replace("'","");
        }
        if (items.length == 0) {
            throw new IllegalArgumentException(
                "Criteria in comparison node [" + path + "] are null or empty.");
        }
        return items;
    }

    private static boolean isInteger(final String value) {
        boolean isInt = false;
        try {
            Integer.parseInt(value);
            isInt = true;
            // is an integer!
        } catch (final NumberFormatException e) {
            LOGGER.debug("Value [" + value + "] is not an integer");
        }
        return isInt;
    }

    private static boolean isJsonObjectNode(final String path, final EntityHelper entityHelper) {

        final String[] items = getNodeItems(path);

        final String firstItem = items[0];

        if (firstItem == null || firstItem.isEmpty()) {
            throw new IllegalArgumentException(
                "First Criterion in comparison node [" + path + "] is null or empty.");
        }

        final Field field = FieldUtils.getField(entityHelper.getEntityClass(), firstItem, true);

        return field != null && JSONObject.class.equals(field.getType());

    }

    private static String buildCriterionName(final String[] items) {

        String criterionName = items[0];
        for (int i = 1; i < items.length - 1; i++) {
            criterionName += "->'" + items[i] + "'";
        }
        return criterionName;
    }

    private static boolean isString(final String value) {
        return StringUtils.startsWith(value, STRING_KEY_INDICATOR) && StringUtils.endsWith(value, STRING_KEY_INDICATOR);
    }

    private static boolean isBoolean(final String value) {return "true".equals(value) || "false".equals(value);}

}

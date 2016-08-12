package com.fstn.common.utils.sql.builder;

import com.fstn.common.utils.sql.builder.model.query.JSONBQuery;
import com.fstn.common.utils.sql.builder.model.query.JSONBQueryContent;
import com.fstn.common.utils.sql.builder.model.table.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JSONB Alias helper Created by sza on 05/08/2016.
 */
public class JSONBAliasHelper
{

    /**
     * Return path alias to use after as
     *
     * @return alias
     */
    public static String getAlias(String path) {
        String alias;
        alias = "\"" + path + "\"";
        return alias;
    }

    /**
     * Remove table name from originalFieldsPath because inside rsql, there is no table name at the begin
     *
     * @param table        table name
     * @param tmpPath      path
     * @param defaultValue defautl value to use
     * @return path without table name
     */
    public static String remvoeTableNameFromPath(final Table table, final String tmpPath, final String defaultValue) {
        String result = defaultValue;
        if (tmpPath.startsWith(table.getName() + ".")) {
            result = tmpPath.substring(table.getName().length() + 1, tmpPath.length());
        }
        return result;
    }

    /**
     * Return Alias synthax of jsonbArray
     *
     * @param tmpPath  path
     * @param tmpAlias alias
     * @return jsonbarray
     */
    public static String toJSONBArray(String tmpPath, String tmpAlias) {
        return "jsonb_array_elements(" + tmpPath + ") AS " + tmpAlias;
    }

    /**
     * Extract Alias from specific path
     *
     * @param path for example content.header[].obj{}
     * @return Map of rsql/Alias/Fields
     */
    public static List<JSONBQueryContent> extractAlias(String path) {
        List<JSONBQueryContent> listOfRSQLFieldsAlias = new ArrayList<>();
        if (path == null || path.isEmpty() || !path.contains(".") || !path.contains("[]")) {
            return listOfRSQLFieldsAlias;
        }

        List<String> pathSplitByArray = Arrays.asList(path.split("\\[\\]"));

        String throughPath = "";
        for (String currentPartOfPath : pathSplitByArray) {
            //Replace array symbol, by this way we can ignore last element if it's not an array (alias are only use
            // with array)
            if (path.contains(currentPartOfPath + "[]")) {
                throughPath += currentPartOfPath + "[]";
                JSONBQueryContent alias = new JSONBQueryContent();
                alias.setRSQL(throughPath);
                listOfRSQLFieldsAlias.add(alias);
            }
        }

        for (JSONBQueryContent currentPathAlias : listOfRSQLFieldsAlias) {

            String currentPath = currentPathAlias.getRSQL();
            String tmpJSONB = "";
            String tmpAliasedJSONB;
            String tmpAlias;
            String lastState = "";
            throughPath = "";

            List<String> pathSplitByDot = Arrays.asList(currentPath.split("\\."));
            for (String currentPartOfPath : pathSplitByDot) {
                /**
                 * If JSON array
                 */
                if (JSONBHelper.isArrayProperty(currentPartOfPath)) {
                    lastState = "array";
                    tmpJSONB += tmpJSONB.length() > 0 ? "->" : "";
                    tmpJSONB += JSONBHelper.removeRSQLIndicatorAndAddQuoteIfNeeded(currentPartOfPath);
                }
                /**
                 * If JSON object
                 */
                else if (JSONBHelper.isObjectProperty(currentPartOfPath)) {
                    lastState = "object";
                    tmpJSONB += tmpJSONB.length() > 0 ? "->" : "";
                    tmpJSONB += JSONBHelper.removeRSQLIndicatorAndAddQuoteIfNeeded(currentPartOfPath);
                }
                /**
                 * If JSON properties
                 */
                else {
                    if (lastState.equals("object") || lastState.equals("array")) {
                        tmpJSONB += tmpJSONB.length() > 0 ? "->" : "";
                        tmpJSONB += currentPartOfPath;
                        lastState = "";
                    } else {
                        tmpJSONB += tmpJSONB.length() == 0 ? "" : ".";
                        tmpJSONB += currentPartOfPath;
                        lastState = "";
                    }
                }
            }
            tmpAlias = JSONBAliasHelper.getAlias(currentPath);
            tmpAliasedJSONB = JSONBAliasHelper.extractAliasedNameFromRSQL(currentPath, listOfRSQLFieldsAlias);
            currentPathAlias.setAlias(tmpAlias);
            currentPathAlias.setFields(tmpJSONB);
            currentPathAlias.setAliasedFields(tmpAliasedJSONB);
        }
        return listOfRSQLFieldsAlias;
    }

    /**
     * Extract where criterion name based on alias
     *
     * @param rsqlName rsql
     * @param query    query
     * @return aliasedName
     */
    public static String extractAliasedNameFromRSQL(String rsqlName, JSONBQuery query) {
        return extractAliasedNameFromRSQL(rsqlName, query.getContent());

    }

    /**
     * @param rsqlName     rsql
     * @param queryContent query content
     * @return aliasedName
     */
    public static String extractAliasedNameFromRSQL(String rsqlName, List<JSONBQueryContent> queryContent) {
        String nameBasedOnAlias;
        if (rsqlName == null || rsqlName.isEmpty() || !rsqlName.contains(".")) {
            return rsqlName;
        }
        /**
         * nameBasedOnAlias = 'content'->'invoiceLines'->'array'->'array2'->'id'
         * for rsqlName = content.invoiceLines[].array[].array2[].id
         */
        nameBasedOnAlias = JSONBAliasHelper.extractPath(rsqlName);

        /**
         * Looking for alias that matches with this path
         */
        int lastAliasLength = 0;
        for (JSONBQueryContent content : queryContent) {
            if (rsqlName.contains(content.getRSQL())) {
                if (content.getRSQL().length() > lastAliasLength) {
                    lastAliasLength = content.getRSQL().length();
                    String endOfContentThatIsNotPresentInAlias = rsqlName.replace(content.getRSQL(), "");
                    if (endOfContentThatIsNotPresentInAlias.startsWith(".")) {
                        endOfContentThatIsNotPresentInAlias =
                            endOfContentThatIsNotPresentInAlias.substring(1,
                                                                          endOfContentThatIsNotPresentInAlias.length());
                    }
                    String jsonBContentThatIsNotPresentInAlias = JSONBAliasHelper.extractPath(
                        endOfContentThatIsNotPresentInAlias);
                    if (content.getAlias() != null) {
                        nameBasedOnAlias = content.getAlias();
                        nameBasedOnAlias += !jsonBContentThatIsNotPresentInAlias.isEmpty() ? "->" : "";
                        nameBasedOnAlias += jsonBContentThatIsNotPresentInAlias;
                    }
                }
            }
        }
        return nameBasedOnAlias;
    }

    /**
     * Extract where criterion path from rsql
     *
     * @param rsqlSelector rsql
     * @return path
     */
    public static String extractPath(String rsqlSelector) {
        String result = "";
        if (rsqlSelector == null || rsqlSelector.isEmpty() || (!rsqlSelector.contains(".") && !rsqlSelector.contains
            ("{}") && !rsqlSelector.contains("@") && !rsqlSelector.contains("[]"))) {
            return rsqlSelector;
        }

        rsqlSelector = JSONBHelper.removeRSQLIndicatorAndAddQuoteIfNeeded(rsqlSelector);

        List<String> pathSplitByDot = Arrays.asList(rsqlSelector.split("\\."));

        String throughPath = "";
        for (String currentPartOfPath : pathSplitByDot) {
            result += result.length() > 0 ? "->" : "";
            //String optionalQuote = result.length() > 0 || pathSplitByDot.size() == 1 ? "'" : "";
            result += currentPartOfPath;
        }
        return result;
    }

    /**
     * Return first part of RSQL stop on array
     *
     * @param rsqlSelector rsqlSelector
     * @return first part
     */
    public static String extractFirstArrayRsql(String rsqlSelector) {
        //language=regexp
        String regex = "([^\\[\\]]*\\[\\]).*";
        String firstArray = rsqlSelector.replaceAll(regex, "$1");
        /**
         * If there is noarray, we split on first dot
         */
        if (firstArray.equals(rsqlSelector) &&  firstArray.indexOf(".") != -1) {
            firstArray = firstArray.substring( 0, firstArray.indexOf("."));
        }
        return firstArray;
    }

    /**
     * Return end part of RSQL stop on array
     *
     * @param rsqlSelector rsqlSelector
     * @return end part
     */
    public static String extractAfterFirstArrayRsql(String rsqlSelector) {
        /**
         * Looking for array[] and return things after
         */
        String afterFirstArray;
        //language=regexp
        String regex = "[^\\[\\]]*\\[\\]\\.(.*)";
        afterFirstArray = rsqlSelector.replaceAll(regex, "$1");
        /**
         * If there is noarray, we split on first dot
         */
        if (afterFirstArray.equals(rsqlSelector)) {
            afterFirstArray = afterFirstArray.substring(afterFirstArray.indexOf(".")+1,afterFirstArray.length());
        }
        return afterFirstArray;
    }
}

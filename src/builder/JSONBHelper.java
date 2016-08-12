package com.fstn.common.utils.sql.builder;

/**
 * Helper class for JSON Created by sza on 05/08/2016.
 */
public class JSONBHelper
{
    /**
     * property is an object
     *
     * @param s string to test
     * @return true or false
     */
    public static boolean isObjectProperty(String s) {return s.endsWith("{}");}

    /**
     * property is an array
     *
     * @param s string to test
     * @return true or false
     */
    public static boolean isArrayProperty(String s) {return s.endsWith("[]");}

    /**
     * Return path as needed by Postgres remove [] and {} Remive quote arrow etc
     *
     * @return path without RSQL indicator
     */
    public static String removeRSQLIndicatorAndAddQuoteIfNeeded(String path) {
        String realPath;
        //language=regexp
        String removeArrayIndicatorRegex = "([^\\.]*)\\[\\]";
        //language=regexp
        String removeObjectIndicatorRegex = "([^\\.]*)\\{\\}";
        //language=regexp
        String removeFieldsIndicatorRegex ="([^\\.]*)@";
        realPath = path.replaceAll(removeArrayIndicatorRegex, "'$1'");
        realPath = realPath.replaceAll(removeObjectIndicatorRegex, "'$1'");
        realPath = realPath.replaceAll(removeFieldsIndicatorRegex, "'$1'");
        return realPath;
    }

    /**
     * Return path as write as RSQL Sentence Remive quote arrow etc
     *
     * @param path to transform to RSQL
     * @return rsql path
     */
    public static String getAsRSQLPath(String path) {
        path = path.replace("'", "");
        path = path.replace("\"", "");
        path = path.replace("->", ".");
        return path;
    }

    /**
     * Transform RSQL to Query
     *
     * @param rsqlSelector for ex: tax{].id
     * @param value        for exemple 1
     * @return {"tax":{"id":1}}
     */
    public static String transformRSQLToObject(String rsqlSelector, Object value) {
        String result = "{";
        String end = "}";
        String tmp = "";
        /**
         * Replace tax{}-> "tax"{}
         */
        //language=regexp
        String regex  = "([^\\.\\{\\[\\@]+)([(?:\\{\\})(?:\\[\\])(?:\\.\\@)]*)";
        rsqlSelector = rsqlSelector.replaceAll(regex,"\"$1\"$2");
        for (int i = 0; i < rsqlSelector.length(); i++) {
            Character partOfRSQL = rsqlSelector.charAt(i);
            if (partOfRSQL == '.') {
                result += tmp;
                tmp = "";
                continue;
            }
            if (partOfRSQL == '}') {
                if (i + 1 < rsqlSelector.length()) {
                    end = partOfRSQL + end;
                }
                continue;
            }
            if (partOfRSQL == '@') {
                continue;
            }
            if (partOfRSQL == ']') {
                if (i + 1 < rsqlSelector.length()) {
                    end = "}" + partOfRSQL + end;
                }
                continue;
            }

            if (partOfRSQL == '{') {
                /**
                 * No need to add that if rsqlSelector end by {} or []
                 */
                if (i + 2 < rsqlSelector.length()) {
                    tmp += ":";
                    tmp += "{";
                }
                continue;
            }

            if (partOfRSQL == '[') {
                if (i + 2 < rsqlSelector.length()) {
                    tmp += ":";
                }
                tmp += "[{";
                continue;
            }

            tmp += partOfRSQL;
        }
        result += tmp + ":" + value;
        result += end;
        return result;
    }
}

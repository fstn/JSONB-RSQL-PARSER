package com.fstn.common.utils.sql.builder.exception;

/**
 * Exception for query builder
 */
public class QueryBuilderException extends RuntimeException
{

    /**
     * Build QueryBuilderException with message
     *
     * @param msg that describe de exception
     */
    public QueryBuilderException(final String msg) {
        super(msg);
    }

    /**
     * Build QueryBuilderException with message and cause
     *
     * @param msg   that describe de exception
     * @param cause of this exception
     */
    public QueryBuilderException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Build QueryBuilderException with cause
     *
     * @param cause of this exception
     */
    public QueryBuilderException(final Throwable cause) {
        super(cause);
    }
}

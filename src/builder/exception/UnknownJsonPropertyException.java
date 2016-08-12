package com.fstn.common.utils.sql.builder.exception;

/**
 * Exception raised on unknown property for json
 */
public class UnknownJsonPropertyException extends UnknownPropertyException
{

    public UnknownJsonPropertyException(final String property, final Class<?> metaDataType) {
        super(property, metaDataType);
    }

    /**
     * Build UnknownJsonPropertyException with message
     *
     * @param msg that describe de exception
     */
    public UnknownJsonPropertyException(final String msg) {
        super(msg);
    }

    /**
     * Build UnknownJsonPropertyException with message and cause
     *
     * @param msg   that describe de exception
     * @param cause of this exception
     */
    public UnknownJsonPropertyException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Build UnknownJsonPropertyException with cause
     *
     * @param cause of this exception
     */
    public UnknownJsonPropertyException(final Throwable cause) {
        super(cause);
    }
}

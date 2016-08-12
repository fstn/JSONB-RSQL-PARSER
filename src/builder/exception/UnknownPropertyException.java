package com.fstn.common.utils.sql.builder.exception;

/**
 * Exception raised on unknown property
 */
public class UnknownPropertyException extends RuntimeException
{

    protected String property;
    protected Class<?> metaDataType;

    public UnknownPropertyException(final String property, final Class<?> metaDataType) {
        super("Unknown property: " + property + " from entity " + metaDataType.getName());
        this.property = property;
        this.metaDataType = metaDataType;
    }

    /**
     * Build UnknownPropertyException with message
     *
     * @param msg that describe de exception
     */
    public UnknownPropertyException(final String msg) {
        super(msg);
    }

    /**
     * Build UnknownPropertyException with message and cause
     *
     * @param msg   that describe de exception
     * @param cause of this exception
     */
    public UnknownPropertyException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Build UnknownPropertyException with cause
     *
     * @param cause of this exception
     */
    public UnknownPropertyException(final Throwable cause) {
        super(cause);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    public Class<?> getMetaDataType() {
        return metaDataType;
    }

    public void setMetaDataType(final Class<?> metaDataType) {
        this.metaDataType = metaDataType;
    }
}

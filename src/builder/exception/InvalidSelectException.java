package com.fstn.common.utils.sql.builder.exception;

/**
 * RSQL Exception
 * Created by sza on 09/08/2016.
 */
public class InvalidSelectException extends RuntimeException
{
    public InvalidSelectException(String s) {
        super(s);
    }
}

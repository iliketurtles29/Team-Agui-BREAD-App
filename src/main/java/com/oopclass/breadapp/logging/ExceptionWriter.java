package com.oopclass.breadapp.logging;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

public class ExceptionWriter extends PrintWriter {

    public ExceptionWriter(Writer writer) {
        super(writer);
    }

    private String wrapAroundWithNewlines(String stringWithoutNewlines) {
        return ("\n" + stringWithoutNewlines + "\n");
    }

    public String getExceptionAsString(Throwable throwable) {
        throwable.printStackTrace(this);

        String exception = super.out.toString();

        return (wrapAroundWithNewlines(exception));
    }
}

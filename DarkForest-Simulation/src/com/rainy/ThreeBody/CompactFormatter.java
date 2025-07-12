package com.rainy.ThreeBody;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CompactFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return formatMessage(record) + "\n";
    }
}
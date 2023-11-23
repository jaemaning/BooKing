package com.booking.book.global.data;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected Object convert(String value) {
        return LocalDate.parse(value, formatter);
    }
}

package ru.job4j.quartz.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class HabrCareerDateTimeParser implements DateTimeParser {

    private static final DateTimeFormatter FORMMATTER = ISO_OFFSET_DATE_TIME;

    @Override
    public LocalDateTime parse(String parse) {
        if (!parse.contains("+")) {
            parse += "+00:00";
        }
        parse = parse.replace(' ', 'T');
        return LocalDateTime.parse(parse, FORMMATTER);
    }
}

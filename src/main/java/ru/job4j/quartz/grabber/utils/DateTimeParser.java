package ru.job4j.quartz.grabber.utils;

import java.time.LocalDateTime;

public interface DateTimeParser {
    LocalDateTime parse(String parse);
}

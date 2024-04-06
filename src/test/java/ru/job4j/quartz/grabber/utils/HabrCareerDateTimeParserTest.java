package ru.job4j.quartz.grabber.utils;

import org.junit.jupiter.api.Test;
import ru.job4j.quartz.grabber.HabrCareerParse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    public void whenStringToLocalDateTime() {
        HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
        assertThat(habrCareerDateTimeParser.parse("2024-04-03T15:59:58+03:00"))
                .isEqualTo(LocalDateTime.parse("2024-04-03T15:59:58",
                                                DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
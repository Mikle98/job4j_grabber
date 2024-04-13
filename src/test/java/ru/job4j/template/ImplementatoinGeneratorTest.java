package ru.job4j.template;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ImplementatoinGeneratorTest {
    @Test
    public void whenAllCorrect() {
        ImplementatoinGenerator generator = new ImplementatoinGenerator();
        String template = "I am a ${name}, Who are ${subject}? ";
        Map<String, String> map = Map.of("name", "Petr Arsentev",
                                        "subject", "you");
        String rsl = "I am a Petr Arsentev, Who are you? ";
        assertThat(generator.produce(template, map)).isEqualTo(rsl);
    }

    @Test
    public void whenMapHaveNotKey() {
        ImplementatoinGenerator generator = new ImplementatoinGenerator();
        String template = "I am a ${name}, Who are ${subject}? ";
        Map<String, String> map = Map.of("name", "Petr Arsentev");
        assertThatThrownBy(() -> generator.produce(template, map))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenMapExtraKey() {
        ImplementatoinGenerator generator = new ImplementatoinGenerator();
        String template = "I am a ${name}, Who are ${subject}? ";
        Map<String, String> map = Map.of("name", "Petr Arsentev",
                                        "subject", "you",
                                        "extra", "key");
        assertThatThrownBy(() -> generator.produce(template, map))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
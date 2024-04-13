package ru.job4j.kiss.fool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FoolTest {
    @Test
    public void whenCorrectInput() {
        assertThat("1").isEqualTo(Fool.answerPC(1));
        assertThat("Fizz").isEqualTo(Fool.answerPC(3));
        assertThat("Buzz").isEqualTo(Fool.answerPC(5));
        assertThat("FizzBuzz").isEqualTo(Fool.answerPC(15));
    }

    @Test
    public void whenNotCorrectInput() {
        assertThat(Fool.answerUser("3", 3)).isEqualTo(1);
        assertThat(Fool.answerUser("Fizz", 4)).isEqualTo(1);
        assertThat(Fool.answerUser("Buzz", 3)).isEqualTo(1);
        assertThat(Fool.answerUser("1", 15)).isEqualTo(1);
    }
}
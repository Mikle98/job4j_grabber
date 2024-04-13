package ru.job4j.ood.tdd;

import com.mchange.util.DuplicateElementException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ranges.RangeException;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.List;

class Cinema3DTest {
    @Test
    public void whenBuyThenGetTicket() {
        Account account = new AccountCinema();
        Cinema cinema = new Cinema3D();
        Calendar date = Calendar.getInstance();
        Ticket ticket = cinema.buy(account, 1, 1, date);
        assertThat(ticket).isEqualTo(new Ticket3D());
    }

    @Test
    public void whenAddSessionThenItExistsBetweenAllSessions() {
        Cinema cinema = new Cinema3D();
        Session session = new Session3D();
        cinema.add(session);
        List<Session> sessions = cinema.find(data -> true);
        assertThat(sessions).contains(session);
    }

    @Test
    public void whenBuyOnInvalidRowThenGetException() {
        Account account = new AccountCinema();
        Cinema cinema = new Cinema3D();
        Calendar date = Calendar.getInstance();
        assertThatThrownBy(() -> cinema.buy(account, -1, 1, date))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenInvalidAddSessionThenItNotExistsBetweenAllSessions() {
        Cinema cinema = new Cinema3D();
        Session session = new Session3D();
        assertThatThrownBy(() -> cinema.add(session))
                .isInstanceOf(RangeException.class);
    }

    @Test
    public void whenNotFoundSession() {
        Cinema cinema = new Cinema3D();
        Session session = new Session3D();
        List<Session> sessions = cinema.find(data -> true);
        assertThat(sessions.contains(session)).isFalse();
    }

    @Test
    public void whenDuplsAddSession() {
        Cinema cinema = new Cinema3D();
        Session session = new Session3D();
        assertThatThrownBy(() -> cinema.add(session))
                .isInstanceOf(DuplicateElementException.class);
    }
}
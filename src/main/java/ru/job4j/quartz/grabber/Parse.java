package ru.job4j.quartz.grabber;

import ru.job4j.quartz.grabber.Post;

import java.util.List;

public interface Parse {
    List<Post> list(String link);
}

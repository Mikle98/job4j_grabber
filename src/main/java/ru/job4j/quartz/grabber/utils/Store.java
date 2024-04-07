package ru.job4j.quartz.grabber.utils;

import ru.job4j.quartz.grabber.Post;
import java.util.List;

public interface Store extends AutoCloseable {
    void save(Post post);

    List<Post> getAll();

    Post findById(int id);
}

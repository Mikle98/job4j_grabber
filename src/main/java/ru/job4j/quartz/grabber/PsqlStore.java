package ru.job4j.quartz.grabber;

import ru.job4j.quartz.grabber.utils.HabrCareerDateTimeParser;
import ru.job4j.quartz.grabber.utils.Store;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private Connection connection;

    public PsqlStore(Properties config) throws SQLException {
        try {
            Class.forName(config.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        connection = DriverManager.getConnection(config.getProperty("jdbc.url"),
                                                config.getProperty("jdbc.username"),
                                                config.getProperty("jdbc.password"));
    }

    public void statmentExecute(String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Post> getSQLRows(String sql) {
        try (Statement statement = connection.createStatement()) {
            HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
            List<Post> posts = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                posts.add(new Post(resultSet.getString("name"),
                                   resultSet.getString("link"),
                                    resultSet.getString("text"),
                                    habrCareerDateTimeParser.parse(resultSet.getString("created"))));
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Post post) {
        String sql = String.format("insert into post(name, text, link, created) values ('%s', '%s', '%s', '%s') on conflict (link) do nothing",
                                        post.title, post.description, post.link, post.created);
        statmentExecute(sql);
    }

    @Override
    public List<Post> getAll() {
        String sql = "select * from post";
        return getSQLRows(sql);
    }

    @Override
    public Post findById(int id) {
        String sql = String.format("select * from post where id = %s",
                                    id);
        return getSQLRows(sql).get(0);
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

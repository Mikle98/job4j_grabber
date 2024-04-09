package ru.job4j.quartz.grabber;

import ru.job4j.quartz.grabber.utils.HabrCareerDateTimeParser;
import ru.job4j.quartz.grabber.utils.Store;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    public List<Post> getSQLRows(String sql) {
        List<Post> posts = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                posts.add(new Post(resultSet.getString("name"),
                                   resultSet.getString("link"),
                                    resultSet.getString("text"),
                                    resultSet.getTimestamp("created").toLocalDateTime()));
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     connection.prepareStatement("insert into post(name, text, link, created) values (?, ?, ?, ?) "
                             + "on conflict (link) do nothing")) {
            statement.setString(1, post.title);
            statement.setString(2, post.description);
            statement.setString(3, post.link);
            statement.setTimestamp(4, Timestamp.valueOf(post.created));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.id = generatedKeys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

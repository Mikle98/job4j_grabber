package ru.job4j.quartz.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.quartz.grabber.utils.DateTimeParser;
import ru.job4j.quartz.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private String sourceLink;
    private String prefix;
    private String suffix;
    public static final int COUNT_PAGE = 5;
    private final DateTimeParser dateTimeParser;
    private List<Post> list = new ArrayList<>();

    public void setListPost(String link) {
        list = list(link);
    }

    public List<Post> getList() {
        return list;
    }

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) throws IOException {
        List<String> listDescription = new ArrayList<>();
        String fullLink = "%s%s".formatted(sourceLink, link);
        Connection connection = Jsoup.connect(fullLink);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        rows.select(".style-ugc")
                .forEach(row -> {
                    listDescription.add(row.text());
                });
        return listDescription.toString();
    }

    @Override
    public List<Post> list(String link) {
        try {
            parseLink(link);
            List<Post> postList = new ArrayList<>();
            for (int i = 1; i <= COUNT_PAGE; i++) {
                String fullLink = "%s%s%d%s".formatted(sourceLink, prefix, i, suffix);
                Connection connection = Jsoup.connect(fullLink);
                Document document = null;
                document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element dateElement = row.select(".vacancy-card__date").first();
                    Element linkElement = titleElement.child(0);
                    Element dateTimeElement = dateElement.child(0);
                    String vacancyName = titleElement.text();
                    String vacancyTime = dateTimeElement.attr("datetime");
                    String vacancyLink = String.format("%s%s", sourceLink, linkElement.attr("href"));
                    String vacancyDescription = null;
                    try {
                        vacancyDescription = retrieveDescription(linkElement.attr("href"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    postList.add(new Post(vacancyName, vacancyLink, vacancyDescription.replace('\'', ' '), dateTimeParser.parse(vacancyTime)));
                });
            }
            return postList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseLink(String link) {
        sourceLink = link.substring(0, link.lastIndexOf('/'));
        prefix = link.substring(link.lastIndexOf('/'), link.indexOf('&'));
        suffix = link.substring(link.indexOf('&'));
    }
}

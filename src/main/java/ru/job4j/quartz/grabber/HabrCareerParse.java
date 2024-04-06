package ru.job4j.quartz.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int COUNT_PAGE = 5;

    public static void main(String[] args) throws IOException  {
        for (int i = 1; i <= COUNT_PAGE; i++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, i, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element dateElement = row.select(".vacancy-card__date").first();
                Element linkElement = titleElement.child(0);
                Element dateTimeElement = dateElement.child(0);
                String vacancyName = titleElement.text();
                String vacancyTime = dateTimeElement.attr("datetime");
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String vacancyDescription = null;
                try {
                    vacancyDescription = retrieveDescription(linkElement.attr("href"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("%s %s %s %s%n", vacancyName, vacancyTime, link, vacancyDescription);
            });
        }
    }

    private static String retrieveDescription(String link) throws IOException {
        List<String> listDescription = new ArrayList<>();
        String fullLink = "%s%s".formatted(SOURCE_LINK, link);
        Connection connection = Jsoup.connect(fullLink);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        rows.select(".style-ugc")
                .forEach(row -> {
                    listDescription.add(row.text());
                });
        return listDescription.toString();
    }
}

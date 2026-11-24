package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Використання: java -jar report.jar <шлях_до_файлу> <ключ_пошуку>");
            return;
        }

        String filePath = args[0];
        String searchKey = args[1];

        List<Title> allTitles;
        try {
            allTitles = loadTitles(filePath);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу " + filePath + ": " + e.getMessage());
            return;
        }

        List<Title> uniqueTitles = allTitles.stream()
                .distinct()
                .toList();

        System.out.println("Рядків у файлі: " + allTitles.size()
                + ", унікальних записів: " + uniqueTitles.size());

        Map<String, DoubleSummaryStatistics> genreStats = buildGenreStats(uniqueTitles);
        System.out.println("\nЖанри (кількість, середній рейтинг):");
        genreStats.forEach((genre, stats) ->
                System.out.printf(Locale.US, "%s %d %.2f%n",
                        genre, stats.getCount(), stats.getAverage()));

        List<Title> top5 = findTop5(uniqueTitles);
        System.out.println("\nТоп-5 за рейтингом:");
        top5.forEach(t ->
                System.out.printf(Locale.US, "%.1f %s %s%n",
                        t.getRating(), t.getId(), t.getName()));

        String searchResult = findByKey(uniqueTitles, searchKey);
        System.out.println("\nПошук " + searchKey + ": " + searchResult);
    }

    private static List<Title> loadTitles(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(filePath), StandardCharsets.UTF_8)) {
            return lines
                    .skip(1)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(Title::parse)
                    .toList();
        }
    }

    private static Map<String, DoubleSummaryStatistics> buildGenreStats(List<Title> titles) {
        return titles.stream()
                .collect(Collectors.groupingBy(
                        Title::getGenre,
                        TreeMap::new,
                        Collectors.summarizingDouble(Title::getRating)
                ));
    }

    private static List<Title> findTop5(List<Title> titles) {
        return titles.stream()
                .sorted(Comparator.comparingDouble(Title::getRating).reversed()
                        .thenComparing(Title::getId))
                .limit(5)
                .toList();
    }

    private static String findByKey(List<Title> titles, String searchKey) {
        return titles.stream()
                .filter(t -> t.getId().equals(searchKey))
                .findFirst()
                .map(t -> String.format(Locale.US, "%s (%d), рейтинг %.1f",
                        t.getName(), t.getYear(), t.getRating()))
                .orElse("не знайдено");
    }
}
package org.example;

import java.util.Objects;

public class Title {
    private final String id;
    private final String name;
    private final String genre;
    private final int year;
    private final double rating;

    public Title(String id, String name, String genre, int year, double rating) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

    public static Title parse(String line) {
        String[] parts = line.split(";");
        String id = parts[0].trim();
        String name = parts[1].trim();
        String genre = parts[2].trim();
        int year = Integer.parseInt(parts[3].trim());
        double rating = Double.parseDouble(parts[4].trim());
        return new Title(id, name, genre, year, rating);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return year == title.year
                && Double.compare(title.rating, rating) == 0
                && Objects.equals(id, title.id)
                && Objects.equals(name, title.name)
                && Objects.equals(genre, title.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, genre, year, rating);
    }

    @Override
    public String toString() {
        return String.format("%s (%d), рейтинг %.1f", name, year, rating);
    }
}
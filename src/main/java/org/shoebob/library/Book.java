package org.shoebob.library;

import org.shoebob.library.enums.Tags;

import java.util.ArrayList;
import java.util.UUID;

public class Book {
    // TODO: Find the correct data type for an ISBN number
    private UUID uuid;
    private String isbn;
    private String name;
    private String author;
    private ArrayList<Tags> tags;
    public Book(String isbn, String name, String author) {
        this.uuid = UUID.randomUUID();

        this.isbn = isbn;
        this.name = name;
        this.author = author;
    }

    // Accessor methods
    public UUID getUUID() {
        return uuid;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public Tags getTag(int index) throws ArrayIndexOutOfBoundsException {
        return tags.get(index);
    }

    public void removeTag(int index) throws ArrayIndexOutOfBoundsException {
        tags.remove(index);
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

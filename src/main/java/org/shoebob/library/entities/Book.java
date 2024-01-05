package org.shoebob.library.entities;

import org.shoebob.library.enums.Tags;
import org.shoebob.library.interfaces.RegistryItem;

import java.util.ArrayList;

public class Book implements RegistryItem {
    private String isbn;
    private String title;
    private String author;
    private ArrayList<Tags> tags = new ArrayList<Tags>();

    public Book() {}
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    // Accessor methods

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public Tags getTag(int index) {
        return tags.get(index);
    }

    public void addTag(Tags tag) {
        tags.add(tag);
    }

    public void removeTag(int index) {
        tags.remove(index);
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String toString() {
        return "ISBN: " + isbn + "\tTitle: " + title + "\tAuthor: " + author;
    }

}

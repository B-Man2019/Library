package org.shoebob.library.registry.items;

import org.shoebob.library.enums.Tags;
import org.shoebob.library.interfaces.RegistryItem;

import java.util.ArrayList;

public class Book implements RegistryItem {
    // TODO: Find the correct data type for an ISBN number
    private String isbn;
    private String name;
    private String author;
    private ArrayList<Tags> tags = new ArrayList<Tags>();

    public Book() {}
    public Book(String isbn, String name, String author) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
    }

    // Accessor methods

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

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String toString() {
        return "ISBN: " + isbn + "\nTitle: " + name + "\nAuthor: " + author;
    }
}

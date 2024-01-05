package org.shoebob.library.data;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.shoebob.library.registry.items.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.shoebob.library.data.FirestoreDataFactory.db;

public class BookDataFactory {
    public static void registerBook(Book book, FirestoreDataFactory firestore) {
        firestore.addObject(book, "books", book.getIsbn());
    }

    public static Book getExactBookByTitle(String title, FirestoreDataFactory firestore) {
        CollectionReference books = firestore.makeCollectionReference("books");

        Query query = books.whereEqualTo("title", title);

        return getBookFromQuery(query);
    }

    public static Book getExactBookByIsbn(String isbn, FirestoreDataFactory firestore) {
        CollectionReference books = firestore.makeCollectionReference("books");

        Query query = books.whereEqualTo("isbn", isbn);

        return getBookFromQuery(query);
    }

    public static ArrayList<Book> getAllBooks() {
            ApiFuture<QuerySnapshot> future = db.collection("books").get();

            List<QueryDocumentSnapshot> docs = null;
            ArrayList<Book> objects = new ArrayList<Book>();

            try {
                docs = future.get().getDocuments();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

            for (QueryDocumentSnapshot documentSnapshot : docs) {
                objects.add(documentSnapshot.toObject(Book.class));
            }
            return objects;
    }

    private static Book getBookFromQuery(Query query) {
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                return document.toObject(Book.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

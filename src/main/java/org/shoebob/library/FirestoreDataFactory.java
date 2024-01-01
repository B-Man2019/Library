package org.shoebob.library;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.shoebob.library.registry_items.Book;
import org.shoebob.library.registry_items.user.Admin;
import org.shoebob.library.registry_items.user.Patron;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FirestoreDataFactory {
    Firestore db;

    public FirestoreDataFactory() throws IOException  {
            FileInputStream serviceAccount = new FileInputStream("FirebaseServiceKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            this.db = FirestoreClient.getFirestore();
    }

    public void addPatron(Patron patron) {
        // creates a new document that is titled by the patron's username, and then puts in the object
        makeWritePromise("patrons", patron.getUsername(), patron);
    }

    public void addAdmin(Admin admin) {
        makeWritePromise("admins", admin.getUsername(), admin);
    }

    public void addBook(Book book) {
        makeWritePromise("books", book.getIsbn(), book);
    }

    public Patron getPatron(String username) {
        return makeDocReadPromise("patrons", username).toObject(Patron.class);
    }

    private ApiFuture<WriteResult> makeWritePromise(String collection, String documentTitle, Object object) {
        ApiFuture<WriteResult> future = db.collection(collection).document(documentTitle).set(object);
        if (Main.DEBUG) {
            try {
                System.out.println("WR sent for " + collection + ": " + future.get().getUpdateTime());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return future;
    }

    private DocumentSnapshot makeDocReadPromise(String collection, String document) {
        ApiFuture<DocumentSnapshot> future = db.collection(collection).document(document).get();
        DocumentSnapshot doc = null;

        try {
            doc = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Main.DEBUG) {
            try {
                System.out.println("RR sent for " + collection + ": " + future.get().getUpdateTime());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return doc;
    }
}

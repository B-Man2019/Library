package org.shoebob.library.data;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.shoebob.library.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FirestoreDataFactory {
    public static Firestore db;

    public FirestoreDataFactory() throws IOException  {
            FileInputStream serviceAccount = new FileInputStream("FirebaseServiceKey.json");
            FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setEmulatorHost("localhost:8080")
                .build();

            FirebaseOptions options;
            if (!Main.DEBUG) {
                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
            } else {
                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setFirestoreOptions(firestoreOptions)
                        .build();
            }


            FirebaseApp.initializeApp(options);

            this.db = FirestoreClient.getFirestore();
    }

    public <E> void addObject(E object, String collection, String identifier) {
        makeWritePromise(collection, identifier, object);
    }

    public <E> E getObject(String collection, String identifier, Class<E> type) {
        return makeDocReadPromise(collection, identifier).toObject(type);
    }

    public CollectionReference makeCollectionReference(String collection) {
        return db.collection(collection);
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

    public QuerySnapshot getQuerySnapshot(String collection ) {
        try {
            return db.collection(collection).get().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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

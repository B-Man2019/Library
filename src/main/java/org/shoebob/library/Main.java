package org.shoebob.library;

import org.shoebob.library.data.AccountDataFactory;
import org.shoebob.library.data.BookDataFactory;
import org.shoebob.library.data.FirestoreDataFactory;
import org.shoebob.library.data.SearchAndSortUtils;
import org.shoebob.library.entities.Admin;
import org.shoebob.library.io.Menu;
import org.shoebob.library.io.SimpleInput;
import org.shoebob.library.entities.Book;
import org.shoebob.library.entities.Patron;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static final boolean DEBUG = true;
    public static accountType accountType = null;

    public static FirestoreDataFactory firestore = null;
    static {
        System.out.println("Connecting to Firestore...\n");
        try {
            firestore = new FirestoreDataFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Patron currentPatron = null;
    public static Admin currentAdmin = null;


    public enum accountType {
        PATRON,
        ADMIN
    }

    public static void main(String[] args) {
        // DEMO DATA
        System.out.println("Creating test patron...");

        AccountDataFactory.registerPatronAccount(new Patron("500", "Test", "User",
                "TestUser", "test"), firestore);

        System.out.println("Test patron created!");

        System.out.println("Creating test admin...");

        AccountDataFactory.registerAdminAccount(new Admin("Test", "Admin", "TestAdmin",
                "test"), firestore);

        System.out.println("Test admin created!");


        System.out.println("Creating test books...");
        BookDataFactory.registerBook(new Book("99", "Harry Potter", "JK Rowling"), firestore);
        BookDataFactory.registerBook(new Book("128", "Lord of the Rings", "idk lol"), firestore);
        BookDataFactory.registerBook(new Book("812", "Another Book", "Fake Faker"), firestore);
        System.out.println("Test books created!");
        // END DEMO DATA

        Menu menu = new Menu("Select account type");

        menu.addAction(() -> {
            accountType = accountType.PATRON;
        }, "Patron");

        menu.addAction(() -> {
            accountType = accountType.ADMIN;
        }, "Admin");

        menu.addAction(() -> {
            System.exit(0);
        }, "Exit");

        System.out.println(menu);
        menu.getInput();

        if (accountType == accountType.PATRON) {
            Menu patronMenu = new Menu("Welcome Patron!");
            patronMenu.addAction(Main::startPatronLogin, "Login");
            patronMenu.addAction(Main::startPatronSignup, "Signup");

            System.out.println(patronMenu);
            patronMenu.getInput();
        } else if (accountType == accountType.ADMIN) {
            startAdminLogin();
        }
    }

    private static void startPatronLogin() {
        boolean validLogin = false;
        String username = "";

        while (!validLogin) {
            SimpleInput usernameInput = new SimpleInput("Enter username");
            username = usernameInput.getStringInput();

            SimpleInput passwordInput = new SimpleInput("Enter password");
            String password = passwordInput.getStringInput();

            validLogin = AccountDataFactory.validPatronLogin(username, password, firestore);
            if (!validLogin) {
                System.out.println("Bad credentials. Please try to log in again.");
            }
        }
            currentPatron = AccountDataFactory.getPatronAccount(username, firestore); // sync patron to database
            displayPatronMenu();

    }

    private static void displayPatronMenu() {
        Menu patronMenu = new Menu("Welcome back, " + currentPatron.getFirstName() + "!");
        patronMenu.addAction(() -> {
            System.out.println(currentPatron);
        }, "Get Account Info");

        patronMenu.addAction(() -> {
            ArrayList<String> userBooksIsbn = currentPatron.getCheckedOutBooks();
            ArrayList<Book> books = new ArrayList<>();
            for (String isbn : userBooksIsbn) {
                books.add(BookDataFactory.getExactBookByIsbn(isbn, firestore));
            }

            for (Book book : books) {
                System.out.println(book);
            }

            if (books.isEmpty()) {
                System.out.println("No books currently checked out!");
            }
            SimpleInput.prompt();

        }, "Get Checked Out Books");

        patronMenu.addAction(() -> {
            SimpleInput bookInput = new SimpleInput("Search for a book");

            String query = bookInput.getStringInput();

            ArrayList<Book> books = BookDataFactory.getAllBooks();
            ArrayList<Book> queriedBooks = new ArrayList<>();

            int count = 1;
            for (Book book : books) {
                if (SearchAndSortUtils.calcLevenshteinDistance(query, book.getTitle()) < 5) {
                    System.out.println(count + ". " + book);
                    queriedBooks.add(book);
                    count++;
                }
            }

            if (count == 1) {
                System.out.println("No books found!");
                return;
            }

            boolean validIndex = false;
            while (!validIndex) {
                SimpleInput indexInput = new SimpleInput("Enter the # of the book you want to check out. Enter 0 to quit.");
                int index = indexInput.getIntInput();
                if (index == 0 || (index < count && index > 0)) {
                    validIndex = true;
                    if (index == 0) {
                        return;
                    }
                    currentPatron.addCheckedOutBook(queriedBooks.get(index - 1));
                } else {
                    System.out.println("Please enter a valid number!");
                }
            }

            AccountDataFactory.registerPatronAccount(currentPatron, firestore);
        }, "Check Out Book");

        patronMenu.addAction(() -> {
            ArrayList<String> userBooksIsbn = currentPatron.getCheckedOutBooks();
            ArrayList<Book> books = new ArrayList<>();
            for (String isbn : userBooksIsbn) {
                books.add(BookDataFactory.getExactBookByIsbn(isbn, firestore));
            }

            int count = 1;

            for (Book book : books) {
                System.out.println(count + ". " + book);
                count++;
            }

            if (books.isEmpty()) {
                System.out.println("No books currently checked out!");
            }

            if (count == 1) {
                System.out.println("No books found!");
                return;
            }

            boolean validIndex = false;
            while (!validIndex) {
                SimpleInput indexInput = new SimpleInput("Enter the # of the book you want to return. Enter 0 to quit.");
                int index = indexInput.getIntInput();
                if (index == 0 || (index < count && index > 0)) {
                    validIndex = true;
                    if (index == 0) {
                        return;
                    }
                    currentPatron.removeCheckedOutBook(index - 1);
                } else {
                    System.out.println("Please enter a valid number!");
                }
            }

            AccountDataFactory.registerPatronAccount(currentPatron, firestore);

        }, "Return Book");

        patronMenu.addAction(() -> {
            System.exit(0);
        }, "Exit");

        while (true) { // display forever until user exits
            System.out.println("\n" + patronMenu);
            patronMenu.getInput();
        }
    }

    private static void startPatronSignup() {
        SimpleInput firstNameInput = new SimpleInput("Enter first name");
        String firstName = firstNameInput.getStringInput();

        SimpleInput lastNameInput = new SimpleInput("Enter last name");
        String lastName = lastNameInput.getStringInput();

        boolean validUsername = false;
        String username = "";
        while (!validUsername) {
            SimpleInput usernameInput = new SimpleInput("Enter username");
            username = usernameInput.getStringInput();
            if (AccountDataFactory.getPatronAccount(username, firestore) != null) {
                System.out.println("Username already taken. Please enter a different username!");
            } else {
                validUsername = true;
            }
        }

        SimpleInput passwordInput = new SimpleInput("Enter password");
        String password = passwordInput.getStringInput();

        SimpleInput cardNumInput = new SimpleInput("Enter card number");
        String cardNum = cardNumInput.getStringInput();

        AccountDataFactory.registerPatronAccount(new Patron(cardNum, firstName, lastName, username, password), firestore);
        startPatronLogin();
    }

    private static void startAdminLogin() {
        boolean validLogin = false;
        String username = "";

        while (!validLogin) {
            SimpleInput usernameInput = new SimpleInput("Enter username");
            username = usernameInput.getStringInput();

            SimpleInput passwordInput = new SimpleInput("Enter password");
            String password = passwordInput.getStringInput();

            validLogin = AccountDataFactory.validAdminLogin(username, password, firestore);
            if (!validLogin) {
                System.out.println("Bad credentials. Please try to log in again.");
            }
        }
        currentAdmin = AccountDataFactory.getAdminAccount(username, firestore); // sync patron to database
        displayAdminMenu();
    }

    private static void displayAdminMenu() {
        Menu adminMenu = new Menu("Welcome back, " + currentAdmin.getUsername() + "!");
        adminMenu.addAction(() -> {
            SimpleInput firstNameInput = new SimpleInput("Enter patron's first name");
            String firstName = firstNameInput.getStringInput();

            SimpleInput lastNameInput = new SimpleInput("Enter patron's last name");
            String lastName = lastNameInput.getStringInput();

            boolean validUsername = false;
            String username = "";
            while (!validUsername) {
                SimpleInput usernameInput = new SimpleInput("Enter patron's username");
                username = usernameInput.getStringInput();
                if (AccountDataFactory.getPatronAccount(username, firestore) != null) {
                    System.out.println("Username already taken. Please enter a different username!");
                } else {
                    validUsername = true;
                }
            }

            SimpleInput passwordInput = new SimpleInput("Enter patron's password");
            String password = passwordInput.getStringInput();

            SimpleInput cardNumInput = new SimpleInput("Enter patron's card number");
            String cardNum = cardNumInput.getStringInput();

            AccountDataFactory.registerPatronAccount(new Patron(cardNum, firstName, lastName, username, password), firestore);
        }, "Add Patron");

        adminMenu.addAction(() -> {
            SimpleInput firstNameInput = new SimpleInput("Enter the admin's first name");
            String firstName = firstNameInput.getStringInput();

            SimpleInput lastNameInput = new SimpleInput("Enter the admin's last name");
            String lastName = lastNameInput.getStringInput();

            boolean validUsername = false;
            String username = "";
            while (!validUsername) {
                SimpleInput usernameInput = new SimpleInput("Enter the admin's username");
                username = usernameInput.getStringInput();
                if (AccountDataFactory.getAdminAccount(username, firestore) != null) {
                    System.out.println("Username already taken. Please enter a different username!");
                } else {
                    validUsername = true;
                }
            }

            SimpleInput passwordInput = new SimpleInput("Enter the admin's password");
            String password = passwordInput.getStringInput();

            AccountDataFactory.registerAdminAccount(new Admin(firstName, lastName, username, password), firestore);
        }, "Add Admin");

        adminMenu.addAction(() -> {
            SimpleInput usernameInput = new SimpleInput("Enter Patron's EXACT username: ");
            String username = usernameInput.getStringInput();
            Patron patron = AccountDataFactory.getPatronAccount(username, firestore);
            if (patron == null) {
                System.out.println("Username not found. Exiting...");
                return;
            }

            Menu editMenu = new Menu("Edit Patron");
            editMenu.addAction(() -> System.out.println(patron), "Get Patron Info");

            editMenu.addAction(() -> {
                ArrayList<String> userBooksIsbn = patron.getCheckedOutBooks();
                ArrayList<Book> books = new ArrayList<>();
                for (String isbn : userBooksIsbn) {
                    books.add(BookDataFactory.getExactBookByIsbn(isbn, firestore));
                }

                for (Book book : books) {
                    System.out.println(book);
                }

                if (books.isEmpty()) {
                    System.out.println("No books currently checked out by Patron.");
                }
                SimpleInput.prompt();

            }, "Get Checked Out Books");

            editMenu.addAction(() -> {
                SimpleInput firstNameInput = new SimpleInput("Enter first name");
                String firstName = firstNameInput.getStringInput();
                patron.setFirstName(firstName);
            }, "Set First Name");

            editMenu.addAction(() -> {
                SimpleInput lastNameInput = new SimpleInput("Enter last name");
                String lastName = lastNameInput.getStringInput();
                patron.setLastName(lastName);
            }, "Set Last Name");

            editMenu.addAction(() -> {
                SimpleInput usernameEditInput = new SimpleInput("Enter username");
                String usernameEdit = usernameEditInput.getStringInput();
                patron.setUsername(usernameEdit);
            }, "Set Username");

            editMenu.addAction(() -> {
                SimpleInput passwordInput = new SimpleInput("Enter password");
                String password = passwordInput.getStringInput();
                patron.setPassword(password);
            }, "Set Password");

            editMenu.addAction(() -> {
                SimpleInput cardNumInput = new SimpleInput("Enter card num");
                String password = cardNumInput.getStringInput();
                patron.setCardNum(password);
            }, "Set Card Number");

            editMenu.addAction(() -> {
                System.exit(0);
            }, "Exit");

            while (true) {
                System.out.println(editMenu);
                editMenu.getInput();
                AccountDataFactory.registerPatronAccount(patron, firestore);
            }

        }, "Edit Patron");

        adminMenu.addAction(() -> {
            System.exit(0);
        }, "Exit");

        while (true) {
            System.out.println(adminMenu);
            adminMenu.getInput();
        }
    }
}
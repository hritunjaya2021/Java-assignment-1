import java.util.*;
import java.io.*;

public class LibrarySystem {

    // ---------------- BOOK CLASS ----------------
    static class Book implements Comparable<Book> {
        int bookId;
        String title;
        String author;
        String category;
        boolean isIssued;

        Book(int bookId, String title, String author, String category, boolean isIssued) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.category = category;
            this.isIssued = isIssued;
        }

        void display() {
            System.out.println("ID: " + bookId + " | " + title + " | " + author +
                    " | " + category + " | Issued: " + isIssued);
        }

        @Override
        public int compareTo(Book b) {
            return this.title.compareToIgnoreCase(b.title);
        }
    }

    // ---------------- MEMBER CLASS ----------------
    static class Member {
        int memberId;
        String name;
        String email;
        List<Integer> issuedBooks = new ArrayList<>();

        Member(int memberId, String name, String email) {
            this.memberId = memberId;
            this.name = name;
            this.email = email;
        }

        void display() {
            System.out.println("Member ID: " + memberId + " | Name: " + name + " | Email: " + email);
        }
    }

    // ---------------- COLLECTIONS ----------------
    static Map<Integer, Book> books = new HashMap<>();
    static Map<Integer, Member> members = new HashMap<>();

    static File bookFile = new File("books.txt");
    static File memberFile = new File("members.txt");

    // ---------------- LOAD DATA ----------------
    static void loadFromFile() {
        try {
            if (bookFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(bookFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String d[] = line.split(",");
                    int id = Integer.parseInt(d[0]);
                    books.put(id, new Book(id, d[1], d[2], d[3], Boolean.parseBoolean(d[4])));
                }
                br.close();
            }

            if (memberFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(memberFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String d[] = line.split(",");
                    int id = Integer.parseInt(d[0]);
                    Member m = new Member(id, d[1], d[2]);

                    for (int i = 3; i < d.length; i++) {
                        m.issuedBooks.add(Integer.parseInt(d[i]));
                    }

                    members.put(id, m);
                }
                br.close();
            }
        } catch (Exception e) {}
    }

    // ---------------- SAVE DATA ----------------
    static void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(bookFile));
            for (Book b : books.values()) {
                bw.write(b.bookId + "," + b.title + "," + b.author + "," + b.category + "," + b.isIssued);
                bw.newLine();
            }
            bw.close();

            BufferedWriter bw2 = new BufferedWriter(new FileWriter(memberFile));
            for (Member m : members.values()) {
                bw2.write(m.memberId + "," + m.name + "," + m.email);
                for (int id : m.issuedBooks) bw2.write("," + id);
                bw2.newLine();
            }
            bw2.close();
        } catch (Exception e) {}
    }

    // ---------------- OPERATIONS ----------------

    static void addBook(Scanner sc) {
        System.out.print("Book ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();
        System.out.print("Category: ");
        String category = sc.nextLine();

        books.put(id, new Book(id, title, author, category, false));
        saveToFile();
        System.out.println("Book added!");
    }

    static void addMember(Scanner sc) {
        System.out.print("Member ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        members.put(id, new Member(id, name, email));
        saveToFile();
        System.out.println("Member added!");
    }

    static void issueBook(Scanner sc) {
        System.out.print("Book ID: ");
        int bid = sc.nextInt();
        System.out.print("Member ID: ");
        int mid = sc.nextInt();

        if (!books.containsKey(bid) || !members.containsKey(mid)) {
            System.out.println("Invalid IDs!");
            return;
        }

        Book b = books.get(bid);
        Member m = members.get(mid);

        if (b.isIssued) {
            System.out.println("Already issued!");
            return;
        }

        b.isIssued = true;
        m.issuedBooks.add(bid);
        saveToFile();
        System.out.println("Book issued!");
    }

    static void returnBook(Scanner sc) {
        System.out.print("Book ID: ");
        int bid = sc.nextInt();
        System.out.print("Member ID: ");
        int mid = sc.nextInt();

        Book b = books.get(bid);
        Member m = members.get(mid);

        b.isIssued = false;
        m.issuedBooks.remove(Integer.valueOf(bid));
        saveToFile();

        System.out.println("Book returned!");
    }

    static void searchBooks(Scanner sc) {
        sc.nextLine();
        System.out.print("Keyword: ");
        String key = sc.nextLine().toLowerCase();

        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(key) ||
                    b.author.toLowerCase().contains(key) ||
                    b.category.toLowerCase().contains(key)) {
                b.display();
            }
        }
    }

    static void sortBooks() {
        List<Book> list = new ArrayList<>(books.values());
        Collections.sort(list);
        for (Book b : list) b.display();
    }

    // ---------------- MAIN MENU ----------------

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        loadFromFile();

        while (true) {
            System.out.println("\n--- City Library Digital System ---");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Sort Books");
            System.out.println("7. Exit");
            System.out.print("Choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1: addBook(sc); break;
                case 2: addMember(sc); break;
                case 3: issueBook(sc); break;
                case 4: returnBook(sc); break;
                case 5: searchBooks(sc); break;
                case 6: sortBooks(); break;
                case 7:
                    saveToFile();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid!");
            }
        }
    }
}
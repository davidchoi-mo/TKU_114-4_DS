import java.util.ArrayList;
import java.util.List;

public class LibraryBookBst {
    public static void main(String[] args) {
        LibraryBst library = new LibraryBst();
        System.out.println("add=" + library.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch")));
        System.out.println("add=" + library.add(new Book("978-0596007126", "Head First Design Patterns", "Freeman & Robson")));
        System.out.println("add=" + library.add(new Book("978-0132350884", "Clean Code", "Robert Martin")));
        System.out.println("duplicate=" + library.add(new Book("978-0134685991", "Effective Java (dup)", "?")));

        System.out.println("borrow=" + library.borrow("978-0134685991"));
        System.out.println("borrowAgain=" + library.borrow("978-0134685991"));
        System.out.println("removeWhileBorrowed=" + library.remove("978-0134685991"));
        System.out.println("returnBook=" + library.returnBook("978-0134685991"));
        System.out.println("returnAgain=" + library.returnBook("978-0134685991"));
        System.out.println("removeAfterReturn=" + library.remove("978-0134685991"));

        System.out.println("missing=" + library.remove("978-0000000000"));
        System.out.println("range=" + library.isbnRange("978-0132350884", "978-0596007126"));

        System.out.println("-- inorder report --");
        for (Book book : library.inorder()) {
            System.out.println(book);
        }
    }
}

class Book {
    String isbn;
    String title;
    String author;
    boolean available;

    Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    @Override
    public String toString() {
        return isbn + " \"" + title + "\" by " + author + " available=" + available;
    }
}

class BookNode {
    Book data;
    BookNode left;
    BookNode right;

    BookNode(Book data) {
        this.data = data;
    }
}

class LibraryBst {
    private BookNode root;

    boolean add(Book book) {
        if (root == null) {
            root = new BookNode(book);
            return true;
        }
        BookNode current = root;
        while (true) {
            int cmp = book.isbn.compareTo(current.data.isbn);
            if (cmp == 0) {
                return false;
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new BookNode(book);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new BookNode(book);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Book find(String isbn) {
        BookNode current = root;
        while (current != null) {
            int cmp = isbn.compareTo(current.data.isbn);
            if (cmp == 0) {
                return current.data;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    boolean borrow(String isbn) {
        Book book = find(isbn);
        if (book == null || !book.available) {
            return false;
        }
        book.available = false;
        return true;
    }

    boolean returnBook(String isbn) {
        Book book = find(isbn);
        if (book == null || book.available) {
            return false;
        }
        book.available = true;
        return true;
    }

    boolean remove(String isbn) {
        Book book = find(isbn);
        if (book == null || !book.available) {
            return false;
        }
        root = remove(root, isbn);
        return true;
    }

    private BookNode remove(BookNode node, String isbn) {
        int cmp = isbn.compareTo(node.data.isbn);
        if (cmp < 0) {
            node.left = remove(node.left, isbn);
        } else if (cmp > 0) {
            node.right = remove(node.right, isbn);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            BookNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.isbn);
        }
        return node;
    }

    private BookNode minimumNode(BookNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    List<Book> isbnRange(String low, String high) {
        List<Book> result = new ArrayList<>();
        if (low.compareTo(high) <= 0) {
            isbnRange(root, low, high, result);
        }
        return result;
    }

    private void isbnRange(BookNode node, String low, String high, List<Book> result) {
        if (node == null) {
            return;
        }
        if (low.compareTo(node.data.isbn) < 0) {
            isbnRange(node.left, low, high, result);
        }
        if (low.compareTo(node.data.isbn) <= 0 && node.data.isbn.compareTo(high) <= 0) {
            result.add(node.data);
        }
        if (node.data.isbn.compareTo(high) < 0) {
            isbnRange(node.right, low, high, result);
        }
    }

    List<Book> inorder() {
        List<Book> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(BookNode node, List<Book> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }
}

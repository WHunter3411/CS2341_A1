import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class Queue<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public Queue() { //Sets up the queue
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return first.item;
    }

    public void enqueue(Item item) {
        Node<Item> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) last = null;
        return item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(" ");
        }
        return s.toString();
    }

    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();
        Stack<String> errorStack = new Stack<>();
        List<String> recentErrors = new ArrayList<>();
        int infoCount = 0;
        int warnCount = 0;
        int errorCount = 0;
        int memoryWarnings = 0;

        // Specify the path to your CSV file
        String csvFile = "src/log-data.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                queue.enqueue(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!queue.isEmpty()) {
            String logEntry = queue.dequeue();

            if (logEntry.contains("ERROR")) {
                errorStack.push(logEntry);
                errorCount++;
                recentErrors.add(logEntry);
                if (recentErrors.size() > 100) {
                    recentErrors.remove(0); // Keep only the last 100 errors
                }
            } else if (logEntry.contains("WARN")) {
                warnCount++;
                if (logEntry.contains("Memory")) {
                    memoryWarnings++; // Count memory warnings
                }
            } else if (logEntry.contains("INFO")) {
                infoCount++;
            }
        }

        System.out.println("INFO logged: " + infoCount);
        System.out.println("WARN logged: " + warnCount);
        System.out.println("ERROR logged: " + errorCount);
        System.out.println("Memory warnings logged: " + memoryWarnings);

        System.out.println("100 Recent Errors:");
        for (String error : recentErrors) {
            System.out.println(error);
        }
    }
}

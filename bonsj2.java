import java.io.FileNotFoundException;
import java.io.PrintStream;


class bonsj2 {
    public static void main(String[] args) {
        System.out.println("Julia Bonsack");
        System.out.println("CS2340 Fall 2024");

        System.out.println("Hello, Julia!");

        try {
            PrintStream fileOut = new PrintStream("bonsj2.txt");
            System.setOut(fileOut);
            System.out.println("Julia Bonsack");
            System.out.println("CS2340 Fall 2024");
            System.out.println("Hello, Julia!");
        } catch (FileNotFoundException e) {
            System.err.println("Error: Could not write to file.");
        }
    }

}

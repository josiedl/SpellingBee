import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    // Generates all possible words
    public void makeWords(String word, String letters) {
        words.add(word);
        //words.add(word);
        for (int i = 0; i < letters.length(); i++) {
            makeWords(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words, 0, words.size() - 1);
    }

    // Merges two arraylists of strings
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> mergedArray = new ArrayList<String>();
        int index1 = 0, index2 = 0;
        while (index1 < arr1.size() && index2 < arr2.size()) {
            if (arr1.get(index1).compareTo(arr2.get(index2)) < 0) {
                mergedArray.add(arr1.get(index1++));
            }
            else {
                mergedArray.add(arr2.get(index2++));
            }
        }

        // Copy over remaining elements
        while (index1 < arr1.size()) {
            mergedArray.add(arr1.get(index1++));
        }
        while (index2 < arr2.size()) {
            mergedArray.add(arr2.get(index2++));
        }

        return mergedArray;
    }

    // Mergesorts an arraylist of strings
    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        if (high - low == 0) {
            ArrayList<String> newWords = new ArrayList<String>();
            newWords.add(words.get(low));
            return newWords;
        }
        int mid = (high + low) / 2;
        ArrayList<String> arr1 = mergeSort(words, low, mid);
        ArrayList<String> arr2 = mergeSort(words, mid + 1, high);
        return merge(arr1, arr2);
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            if (!canBeFound(words.get(i), 0, DICTIONARY.length)) {
                words.remove(i);
                i--;
            }
        }
    }

    // Returns true if an inputted word is found in dictionary, otherwise returns false
    public boolean canBeFound(String word, int start, int end){
            int mid = start + (end - start) / 2;
            if (word.equals(DICTIONARY[mid])) {
                return true;
            }
            if (start >= end) {
                return false;
            }
            else if (word.compareTo(DICTIONARY[mid]) < 0) {
                return canBeFound(word, start, mid - 1);
            }
            else {
                return canBeFound(word, mid + 1, end);
            }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}

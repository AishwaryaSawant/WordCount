import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.*;

//@author Aishwarya

public class WordFrequency {
    public static void main(String args[]) {

        String fileName = "Coding Challenge.txt";

        //A separate method is method created and file name and number of words is passed to it
        //so that if we wanted to search any other number of high frequency elements, we can pass that in k

        //output is written to a file : out.txt
        topKWords(fileName, 10);
        //Testing with a different parameter
        //System.out.println(topKWords(fileName,5));
    }

    //Time complexity of the code is taken into consideration and memory management
    //so pointer technique is used instead of multiple for loops
    //Also, all the sentences are not stored, only the last(most recent/current last) sentence containing the high frequency element is saved
    //to store the sentence only contributing to the output the highest frequency element is taken from the top of the priority queue(max heap)
    public static List<String> topKWords(String fileName, int k) {
        try {
            //PrintStream consoleOutput = System.out;

            PrintStream fileOut = new PrintStream("./out.txt");
            System.setOut(fileOut);

        List<String> output = null;
        int totalWordCount = 0;

        try (FileInputStream fis = new FileInputStream(fileName);
             DataInputStream dis = new DataInputStream(fis);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(dis))) {

            //testResult is used to save the last sentence with the most used word
            String testResult = "";
            String line = null;

            //wordMap saves the word as key and integer value of the frequency of that particular key(word)
            Map<String, Integer> wordMap = new HashMap<>();

            //priority queue max heap is created to store the highest frequency word on the top and get it in O(1) time complexity
            PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                    //while storing words in priority queue, it is sorted is descending order of frequency of word,
                    //if any two words have same frequency then the words are sorted alphabetically in ascending order
                    (a, b) -> a.getValue() != b.getValue() ? b.getValue() - a.getValue() : a.getKey().compareTo(b.getKey()));

            String sentence = "";

            //set to store the unique words of a sentence
            Set<String> sentenceWords = new HashSet<>();

            while ((line = br.readLine()) != null) {
                //all words in the line are converted to lowercase to avoid the differentiation on words because of case of letters
                //eg. to ensure file and File are same, etc
                line = line.toLowerCase();

                //all the punctuations in the line are replaced accept for the '.', as period symbol helps keep track of sentences
                line = line.replaceAll("[\\p{Punct}&&[^.]]+", "");

                //i and j are the pointers to traverse the line read from the file
                int i = 0;
                int j = 0;

                //j will go until the end of the line
                while (j < line.length()) {
                    char c = line.charAt(j);

                    if (c == '.' || c == ' ') {
                        //if we encounter . or space as c, in the current position of j, we save it as a word
                        String word = line.substring(i, j++);

                        //we now reset the pointer moving i to the position of j to get the next word.
                        i = j;

                        //this is used to handle the case of empty string
                        if (word.equals(""))
                            continue;

                        //as we encounter a word, we check if the word exists in the map then the value of frequency is increased by one
                        //if the word is not previously present in the map then we add the word and set the value of frequency to 1
                        wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);

                        //we add unique words in a sentence to a HashSet, this set is used to check if the current high frequency word/top of priority queue
                        //exists in the current sentence
                        sentenceWords.add(word);

                        //sentence is appended character by character
                        sentence = sentence + c;

                        //If we reach to the end of the sentence then we clear the priority queue and make it with the map
                        //having updated count of all the words
                        if (c == '.') {
                            pq.clear();
                            //creating priority queue with the map entry elements
                            for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
                                pq.offer(entry);

                            }
                            //get the top of the maxheap/priority queue to get the highest frequency element
                            String frequentWord = pq.peek().getKey();

                            //if most frequent word is found in the current senetence the the test result string is appended with the current sentence
                            if (sentenceWords.contains(frequentWord)) {
                                testResult = sentence;
                            }

                            //we empty the sentence string to get the next new sentence
                            sentence = "";
                            //we empty the hashset of sentence words to get the unique words of new sentence
                            sentenceWords = new HashSet<>();
                        }

                    } else {
                        //to increment j pointer to the next char in the word if ' ' or '.' is not encountered
                        j++;
                        sentence = sentence + c;
                    }

                }
            }
            //Used to display most used word
            System.out.println("Most used word: " + pq.peek().getKey());

            output = new ArrayList<>();

            //Used to get total word count from the file read
            for (int i = 0; i < k; i++) {
                output.add(pq.peek().getKey());
                totalWordCount += pq.peek().getValue();
                pq.remove();
            }

            for (int i = k; i < pq.size(); i++) {
                totalWordCount += pq.peek().getValue();
                pq.remove();
            }

            //Output is written to a text file out.text
            System.out.println("Total word count: " + totalWordCount);
            System.out.println();
            System.out.println("Last sentence on the file that contains the most used word: ");
            System.out.println(testResult);
            System.out.println();
            System.out.println("Top 10 highest frequency words sorted in descending order of their frequency:");
            System.out.println(output);
            fileOut.close();

//            consoleOutput.println("Total word count: " + totalWordCount);
//            consoleOutput.println("Last sentence on the file that contains the most used word: ");
//            consoleOutput.println(testResult);
//            consoleOutput.println("Top 10 highest frequency words sorted in descending order of their frequency:");
//            for(int i=0;i<output.size();i++){
//                consoleOutput.println(output.get(i));
//            }
//            //System.setOut(consoleOutput);

            return output;

        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        }catch(Exception e){

        }
        return null;
    }
}

/*Expected output: // output is written to a text file out.txt
Most used word: the
Total word count: 350

Last sentence on the file that contains the most used word:
closed formats are opague only the seller is able to tell how much each customer is willing to pay for an object.

Top 10 highest frequency words sorted in descending order of their frequency:
[the, a, is, auctions, for, of, in, object, to, an] */
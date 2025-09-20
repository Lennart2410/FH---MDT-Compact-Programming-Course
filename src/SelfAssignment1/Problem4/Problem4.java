package SelfAssignment1.Problem4;

import java.util.Arrays;

public class Problem4 {
    public static void main(String[] args) {
        String text = "To be or not to be, that is the question;"
                + "Whether `tis nobler in the mind to suffer"
                + " the slings and arrows of outrageous fortune,"
                + " or to take arms against a sea of troubles,"
                + " and by opposing end them?";
        String[] splitText = text.split(" ");
        // Split string will be fitered out for any special characters
        // Everything will be set to lowercase, since Uppercase words will always be sorted in favor for lowercase words
        // Setting to lowercas and printing the letters is done by using a method-reference for the respective methods
        //Arrays.stream(splitText).map(elem -> elem.replaceAll("[^a-zA-Z0-9\\\\s]", "")).map(String::toLowerCase).sorted().forEach(System.out::println);

        // Sorting with bubble sort but without setting the words to lower case and filtering out the special characters
        Arrays.stream(bubbleSort(splitText)).forEach(System.out::println);
    }

    private static String[] bubbleSort(String[] splitText) {
        int n = splitText.length;

        // Outer Bubble-Loop
        // Checking each Word except the last one (the last one has nothing to compare to) with the respective next element
        for (int i = 0; i < n - 1; i++) {

            // Inner Bubble-Loop
            // Comparing the words, starting from the first one and iterating through the array
            // First Iteration will place the "highest" word into the last position
            for (int j = 0; j < n - 1 - i; j++) {

                if (splitText[j].compareTo(splitText[j + 1]) > 0) {
                    // In case the second element is greater than the first, it will be swapped with a triangle swap
                    String temp = splitText[j];
                    splitText[j] = splitText[j + 1];
                    splitText[j + 1] = temp;
                }
            }
        }
        return splitText;
    }
}

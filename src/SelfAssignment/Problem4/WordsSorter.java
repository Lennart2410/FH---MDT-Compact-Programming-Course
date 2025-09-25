package SelfAssignment.Problem4;

public class WordsSorter {
	public static void main(String[] args) {
        // Create a string from the previous question
        String text = "To be or not to be that is the question";

        // Split into words (define words as sequences separated by spaces)
        String[] words = text.toLowerCase().split("\\s+");

        // Bubble Sort the words
        for (int i = 0; i < words.length - 1; i++) {
            for (int j = 0; j < words.length - 1 - i; j++) {
                if (words[j].compareTo(words[j + 1]) > 0) {
                    // swap
                    String temp = words[j];
                    words[j] = words[j + 1];
                    words[j + 1] = temp;
                }
            }
        }

        // Print sorted words
        System.out.println("Words in alphabetical order:");
        for (String word : words) {
            System.out.println(word);
        }
    }

}


public class StringCharecters {
    public static void main(String[] args) {
        String str = "To be or not to be, that is the question--"
                + "whether 'tis nobler in the mind to suffer"
                + "the slings and arrows of outrageous fortune,"
                + "or to take arms against a sea of troubles,"
                + "and by opposing end them?";

        int spaces = 0;
        int vowels = 0;
        int letters = 0;

        for (int i = 0; i < str.length(); i++) {
            char ch = Character.toLowerCase(str.charAt(i));

            if (Character.isLetter(ch)) {
                letters++;
                if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                    vowels++;
                }
            } else if (ch == ' ') {
                spaces++;
            }
        }

        System.out.println("The text contained vowels: " + vowels + "\n" +
                "spaces: " + spaces + "\n" +
                "consonants " + (letters - vowels) + "\n" +
                "spaces + consonants: " + ((letters - vowels) + spaces));

    }
}

public class RecursiveTextTools {
    public static String reverse(String text) {
        requireText(text);
        return reverse(text, text.length() - 1);
    }

    private static String reverse(String text, int index) {
        if (index < 0) {
            return "";
        }
        return text.charAt(index) + reverse(text, index - 1);
    }

    public static boolean isPalindrome(String text) {
        requireText(text);
        return isPalindrome(text, 0, text.length() - 1);
    }

    private static boolean isPalindrome(String text, int left, int right) {
        if (left >= right) {
            return true;
        }
        if (Character.isWhitespace(text.charAt(left))) {
            return isPalindrome(text, left + 1, right);
        }
        if (Character.isWhitespace(text.charAt(right))) {
            return isPalindrome(text, left, right - 1);
        }
        if (Character.toLowerCase(text.charAt(left))
                != Character.toLowerCase(text.charAt(right))) {
            return false;
        }
        return isPalindrome(text, left + 1, right - 1);
    }

    public static int countCharacter(String text, char target) {
        requireText(text);
        return countCharacter(text, target, 0);
    }

    private static int countCharacter(String text, char target, int index) {
        if (index == text.length()) {
            return 0;
        }
        int currentCount = text.charAt(index) == target ? 1 : 0;
        return currentCount + countCharacter(text, target, index + 1);
    }

    private static void requireText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text must not be null");
        }
    }

    private static void showTest(String text) {
        System.out.printf("text=%s, reverse=%s, palindrome=%s%n",
                quote(text), quote(reverse(text)), isPalindrome(text));
    }

    private static String quote(String text) {
        return '"' + text + '"';
    }

    public static void main(String[] args) {
        showTest("");
        showTest("A");
        showTest("Level");
        showTest("Never odd or even");
        showTest("Data Structure");

        System.out.println("countCharacter(\"banana\", 'a')="
                + countCharacter("banana", 'a'));
        System.out.println("countCharacter(\"banana\", 'z')="
                + countCharacter("banana", 'z'));
    }
}

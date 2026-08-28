import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Q07_RequestPipeline {

    public static boolean isBalanced(String text) {
        if (text == null) {
            return false;
        }

        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : text.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else if (ch == ')' || ch == ']' || ch == '}') {
                if (stack.isEmpty()) {
                    return false;
                }

                char opening = stack.pop();

                if ((ch == ')' && opening != '(')
                        || (ch == ']' && opening != '[')
                        || (ch == '}' && opening != '{')) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static List<String> process(String[] commands) {
        List<String> result = new ArrayList<>();

        if (commands == null) {
            return result;
        }

        Deque<String> normalQueue = new ArrayDeque<>();
        Deque<String> urgentQueue = new ArrayDeque<>();

        for (String command : commands) {
            if (command == null) {
                continue;
            }

            command = command.trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.split("\\s+");

            if (parts.length == 1 && parts[0].equals("PROCESS")) {
                if (!urgentQueue.isEmpty()) {
                    result.add(urgentQueue.removeFirst());
                } else if (!normalQueue.isEmpty()) {
                    result.add(normalQueue.removeFirst());
                } else {
                    result.add("EMPTY");
                }
            } else if (parts.length == 2 && parts[0].equals("NORMAL")) {
                normalQueue.addLast(parts[1]);
            } else if (parts.length == 2 && parts[0].equals("URGENT")) {
                urgentQueue.addLast(parts[1]);
            }
        }

        return result;
    }
}
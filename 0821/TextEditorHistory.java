import java.util.ArrayDeque;
import java.util.Deque;

public class TextEditorHistory {
    private final Deque<String> undoStack = new ArrayDeque<>();
    private final Deque<String> redoStack = new ArrayDeque<>();

    public TextEditorHistory() {
        undoStack.addLast("");
    }

    public void edit(String newText) {
        if (newText == null) {
            throw new IllegalArgumentException("文字不可為 null");
        }

        undoStack.addLast(newText);
        redoStack.clear();
        printState("新增操作：\"" + newText + "\"");
    }

    public void undo() {
        if (undoStack.size() <= 1) {
            System.out.println("Undo 失敗：已經是最早狀態");
            printState("Undo");
            return;
        }

        redoStack.addLast(undoStack.removeLast());
        printState("Undo");
    }

    public void redo() {
        String restoredState = redoStack.pollLast();
        if (restoredState == null) {
            System.out.println("Redo 失敗：沒有可恢復的狀態");
            printState("Redo");
            return;
        }

        undoStack.addLast(restoredState);
        printState("Redo");
    }

    public String currentText() {
        return undoStack.peekLast();
    }

    private void printState(String action) {
        System.out.printf(
                "%-18s current=\"%s\", undo=%s, redo=%s%n",
                action,
                currentText(),
                formatStack(undoStack),
                formatStack(redoStack));
    }

    private String formatStack(Deque<String> stack) {
        StringBuilder result = new StringBuilder("[");
        boolean first = true;
        for (String state : stack) {
            if (!first) {
                result.append(", ");
            }
            result.append(state.isEmpty() ? "<空字串>" : state);
            first = false;
        }
        return result.append("]").toString();
    }

    public static void main(String[] args) {
        TextEditorHistory editor = new TextEditorHistory();

        System.out.println("=== 文字編輯 Undo / Redo ===");
        editor.printState("初始狀態");
        editor.undo();
        editor.edit("Hello");
        editor.edit("Hello Java");
        editor.edit("Hello Java!");
        editor.undo();
        editor.undo();
        editor.redo();
        editor.edit("Hello Collections");
        editor.redo();
    }
}

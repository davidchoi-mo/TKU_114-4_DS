import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class EnrollmentCleanup {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(Arrays.asList(
                "Amy", " Ben ", null, "", "Amy", "   ",
                "Cara", "Ben", "David", "Cara", "Cara"));

        System.out.println("清理前：" + names);

        ListIterator<String> iterator = names.listIterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name == null || name.isBlank()) {
                iterator.remove();
            } else {
                iterator.set(name.trim());
            }
        }

        Set<String> seen = new LinkedHashSet<>();
        Set<String> duplicates = new LinkedHashSet<>();
        Iterator<String> cleanedIterator = names.iterator();
        while (cleanedIterator.hasNext()) {
            String name = cleanedIterator.next();
            if (!seen.add(name)) {
                duplicates.add(name);
            }
        }

        System.out.println("清理後：" + names);
        System.out.println("不重複姓名：" + seen);
        System.out.println("重複報告：" + duplicates);
    }
}

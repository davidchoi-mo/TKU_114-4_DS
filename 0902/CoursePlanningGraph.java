import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CoursePlanningGraph {
    private final Map<String, List<String>> dependents = new LinkedHashMap<>();

    public boolean addCourse(String course) {
        if (course == null || course.isBlank() || dependents.containsKey(course)) {
            return false;
        }
        dependents.put(course, new ArrayList<>());
        return true;
    }

    public boolean addPrerequisite(String prerequisite, String course) {
        if (prerequisite == null || course == null
                || !dependents.containsKey(prerequisite)
                || !dependents.containsKey(course)) {
            return false;
        }

        List<String> nextCourses = dependents.get(prerequisite);
        if (nextCourses.contains(course)) {
            return false;
        }
        nextCourses.add(course);
        return true;
    }

    public boolean reachable(String from, String target) {
        if (from == null || target == null
                || !dependents.containsKey(from)
                || !dependents.containsKey(target)) {
            return false;
        }
        return reachableDfs(from, target, new HashSet<>());
    }

    private boolean reachableDfs(String current, String target,
                                 Set<String> visited) {
        if (current.equals(target)) {
            return true;
        }
        if (!visited.add(current)) {
            return false;
        }

        for (String next : dependents.getOrDefault(current, List.of())) {
            if (reachableDfs(next, target, visited)) {
                return true;
            }
        }
        return false;
    }

    public List<String> affectedCourses(String course) {
        if (course == null || !dependents.containsKey(course)) {
            return List.of();
        }

        Set<String> visited = new HashSet<>();
        Set<String> affected = new LinkedHashSet<>();
        visited.add(course);
        collectAffectedDfs(course, visited, affected);
        return new ArrayList<>(affected);
    }

    private void collectAffectedDfs(String current, Set<String> visited,
                                    Set<String> affected) {
        for (String next : dependents.getOrDefault(current, List.of())) {
            if (visited.add(next)) {
                affected.add(next);
                collectAffectedDfs(next, visited, affected);
            }
        }
    }

    public boolean isEmpty() {
        return dependents.isEmpty();
    }

    public static void main(String[] args) {
        CoursePlanningGraph graph = new CoursePlanningGraph();
        for (String course : List.of(
                "Programming", "DataStructures", "Algorithms",
                "Database", "MachineLearning", "Capstone", "English")) {
            graph.addCourse(course);
        }

        graph.addPrerequisite("Programming", "DataStructures");
        graph.addPrerequisite("DataStructures", "Algorithms");
        graph.addPrerequisite("Algorithms", "MachineLearning");
        graph.addPrerequisite("Database", "Capstone");
        graph.addPrerequisite("MachineLearning", "Capstone");

        System.out.println("=== Course planning ===");
        System.out.println("Programming -> Capstone = "
                + graph.reachable("Programming", "Capstone"));
        System.out.println("Database -> Algorithms = "
                + graph.reachable("Database", "Algorithms"));
        System.out.println("Programming -> Programming = "
                + graph.reachable("Programming", "Programming"));
        System.out.println("Programming -> MissingCourse = "
                + graph.reachable("Programming", "MissingCourse"));
        System.out.println("affected by Programming = "
                + graph.affectedCourses("Programming"));
        System.out.println("affected by English = "
                + graph.affectedCourses("English"));
        System.out.println("affected by missing = "
                + graph.affectedCourses("MissingCourse"));

        CoursePlanningGraph emptyGraph = new CoursePlanningGraph();
        System.out.println("empty graph reachable = "
                + emptyGraph.reachable("A", "B"));

        CoursePlanningGraph cyclicGraph = new CoursePlanningGraph();
        for (String course : List.of("A", "B", "C")) {
            cyclicGraph.addCourse(course);
        }
        cyclicGraph.addPrerequisite("A", "B");
        cyclicGraph.addPrerequisite("B", "C");
        cyclicGraph.addPrerequisite("C", "A");
        System.out.println("cycle: affected by A = "
                + cyclicGraph.affectedCourses("A"));
    }
}

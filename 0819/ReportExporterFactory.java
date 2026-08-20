interface ReportExporter {
    String export(String title, int[] values);
}

class CsvExporter implements ReportExporter {
    @Override
    public String export(String title, int[] values) {
        int[] safeValues = values == null ? new int[0] : values;
        StringBuilder result = new StringBuilder();
        result.append("title,value\n");
        for (int value : safeValues) {
            result.append(csvEscape(title)).append(',').append(value).append('\n');
        }
        return result.toString().trim();
    }

    private String csvEscape(String value) {
        String safe = value == null ? "Untitled" : value;
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }
}

class JsonExporter implements ReportExporter {
    @Override
    public String export(String title, int[] values) {
        int[] safeValues = values == null ? new int[0] : values;
        StringBuilder result = new StringBuilder();
        result.append("{\"title\":\"")
                .append(jsonEscape(title == null ? "Untitled" : title))
                .append("\",\"values\":[");
        for (int i = 0; i < safeValues.length; i++) {
            if (i > 0) {
                result.append(',');
            }
            result.append(safeValues[i]);
        }
        result.append("]}");
        return result.toString();
    }

    private String jsonEscape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

class TextExporter implements ReportExporter {
    @Override
    public String export(String title, int[] values) {
        int[] safeValues = values == null ? new int[0] : values;
        StringBuilder result = new StringBuilder();
        result.append(title == null || title.trim().isEmpty()
                ? "Untitled" : title.trim());
        result.append(": ");
        if (safeValues.length == 0) {
            result.append("(no data)");
        } else {
            for (int i = 0; i < safeValues.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(safeValues[i]);
            }
        }
        return result.toString();
    }
}

public class ReportExporterFactory {
    static ReportExporter createExporter(String format) {
        if ("csv".equalsIgnoreCase(format)) {
            return new CsvExporter();
        }
        if ("json".equalsIgnoreCase(format)) {
            return new JsonExporter();
        }
        return new TextExporter();
    }

    static void exportReport(ReportExporter exporter,
                             String title, int[] values) {
        ReportExporter safeExporter = exporter == null
                ? new TextExporter() : exporter;
        System.out.println(safeExporter.export(title, values));
    }

    public static void main(String[] args) {
        int[] sales = {120, 180, 160};

        System.out.println("CSV：");
        exportReport(createExporter("csv"), "Weekly Sales", sales);

        System.out.println("\nJSON：");
        exportReport(createExporter("json"), "Weekly Sales", sales);

        System.out.println("\n不支援格式，改用文字：");
        exportReport(createExporter("xml"), "Weekly Sales", sales);

        System.out.println("\nnull 陣列：");
        exportReport(createExporter("text"), "Empty Report", null);
    }
}

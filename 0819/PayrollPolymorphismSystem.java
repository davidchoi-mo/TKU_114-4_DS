abstract class Employee {
    private final String id;
    private final String name;

    Employee(String id, String name) {
        this.id = normalize(id, "UNKNOWN");
        this.name = normalize(name, "Unknown");
    }

    private static String normalize(String value, String defaultValue) {
        return value == null || value.trim().isEmpty()
                ? defaultValue : value.trim();
    }

    abstract double calculatePay();

    abstract String getEmployeeType();

    @Override
    public String toString() {
        return String.format("%s %s %-8s 薪資=$%.2f",
                id, name, getEmployeeType(), calculatePay());
    }
}

class MonthlyEmployee extends Employee {
    private final double monthlySalary;
    private final double bonus;

    MonthlyEmployee(String id, String name, double monthlySalary, double bonus) {
        super(id, name);
        this.monthlySalary = Math.max(0, monthlySalary);
        this.bonus = Math.max(0, bonus);
    }

    @Override
    double calculatePay() {
        return monthlySalary + bonus;
    }

    @Override
    String getEmployeeType() {
        return "月薪員工";
    }
}

class HourlyEmployee extends Employee {
    private static final double REGULAR_HOURS = 160.0;
    private static final double OVERTIME_RATE = 1.5;

    private final double hourlyRate;
    private final double hoursWorked;

    HourlyEmployee(String id, String name, double hourlyRate,
                   double hoursWorked) {
        super(id, name);
        this.hourlyRate = Math.max(0, hourlyRate);
        this.hoursWorked = Math.max(0, hoursWorked);
    }

    @Override
    double calculatePay() {
        double regularHours = Math.min(hoursWorked, REGULAR_HOURS);
        double overtimeHours = Math.max(0, hoursWorked - REGULAR_HOURS);
        return regularHours * hourlyRate
                + overtimeHours * hourlyRate * OVERTIME_RATE;
    }

    @Override
    String getEmployeeType() {
        return "時薪員工";
    }
}

class SalesEmployee extends Employee {
    private final double baseSalary;
    private final double salesAmount;
    private final double commissionRate;

    SalesEmployee(String id, String name, double baseSalary,
                  double salesAmount, double commissionRate) {
        super(id, name);
        this.baseSalary = Math.max(0, baseSalary);
        this.salesAmount = Math.max(0, salesAmount);
        this.commissionRate = Math.max(0, Math.min(1, commissionRate));
    }

    @Override
    double calculatePay() {
        return baseSalary + salesAmount * commissionRate;
    }

    @Override
    String getEmployeeType() {
        return "業務員工";
    }
}

public class PayrollPolymorphismSystem {
    public static void main(String[] args) {
        Employee[] employees = {
            new MonthlyEmployee("E101", "Amy", 48000, 3000),
            new MonthlyEmployee("E102", "Ben", 52000, 5000),
            new HourlyEmployee("E103", "Cara", 220, 150),
            new HourlyEmployee("E104", "David", 250, 180),
            new SalesEmployee("E105", "Eva", 30000, 250000, 0.08)
        };

        double totalPay = 0;
        Employee highestPaid = null;

        System.out.println("員工薪資明細：");
        for (Employee employee : employees) {
            System.out.println(employee);
            totalPay += employee.calculatePay();
            if (highestPaid == null
                    || employee.calculatePay() > highestPaid.calculatePay()) {
                highestPaid = employee;
            }
        }

        System.out.printf("%n薪資總額：$%.2f%n", totalPay);
        System.out.println("最高薪資：" + highestPaid);
    }
}

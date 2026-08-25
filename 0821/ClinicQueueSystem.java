import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ClinicQueueSystem {
    static class Patient {
        private final String medicalRecordNumber;
        private final String name;

        Patient(String medicalRecordNumber, String name) {
            if (medicalRecordNumber == null || medicalRecordNumber.isBlank()) {
                throw new IllegalArgumentException("病歷號不可空白");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("姓名不可空白");
            }
            this.medicalRecordNumber = medicalRecordNumber;
            this.name = name;
        }

        String getMedicalRecordNumber() {
            return medicalRecordNumber;
        }

        @Override
        public String toString() {
            return medicalRecordNumber + "-" + name;
        }
    }

    private final Deque<Patient> waitingQueue = new ArrayDeque<>();
    private final List<Patient> completedToday = new ArrayList<>();
    private final Set<String> registeredNumbers = new HashSet<>();

    public boolean register(Patient patient) {
        if (!registeredNumbers.add(patient.getMedicalRecordNumber())) {
            System.out.println("掛號失敗：病歷號 " + patient.getMedicalRecordNumber() + " 已掛號");
            return false;
        }

        waitingQueue.offerLast(patient);
        System.out.println("掛號成功：" + patient);
        return true;
    }

    public boolean cancel(String medicalRecordNumber) {
        Iterator<Patient> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            Patient patient = iterator.next();
            if (patient.getMedicalRecordNumber().equals(medicalRecordNumber)) {
                iterator.remove();
                registeredNumbers.remove(medicalRecordNumber);
                System.out.println("取消成功：" + patient);
                return true;
            }
        }

        System.out.println("取消失敗：等候隊列中沒有病歷號 " + medicalRecordNumber);
        return false;
    }

    public Patient callNext() {
        Patient patient = waitingQueue.pollFirst();
        if (patient == null) {
            System.out.println("叫號失敗：目前沒有候診病人");
            return null;
        }

        completedToday.add(patient);
        System.out.println("請 " + patient + " 進入診間");
        return patient;
    }

    public Patient peekNext() {
        Patient patient = waitingQueue.peekFirst();
        System.out.println(patient == null
                ? "下一位：目前沒有候診病人"
                : "下一位：" + patient);
        return patient;
    }

    public void printSummary() {
        System.out.println("候診名單：" + waitingQueue);
        System.out.println("今日完成：" + completedToday);
        System.out.println("候診人數：" + waitingQueue.size());
    }

    public static void main(String[] args) {
        ClinicQueueSystem clinic = new ClinicQueueSystem();

        System.out.println("=== 診所掛號系統 ===");
        clinic.peekNext();                      
        clinic.callNext();                     
        clinic.register(new Patient("P001", "王小明"));
        clinic.register(new Patient("P002", "陳美玲"));
        clinic.register(new Patient("P003", "林志豪"));
        clinic.register(new Patient("P002", "重複測試"));
        clinic.peekNext();
        clinic.cancel("P002");                 
        clinic.cancel("P999");                 
        clinic.callNext();                     
        clinic.callNext();                     
        clinic.callNext();                     
        clinic.printSummary();
    }
}

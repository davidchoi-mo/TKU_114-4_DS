import java.util.Comparator;
import java.util.PriorityQueue;

public class EmergencyTriageQueue {
    public static final class Patient {
        private final String medicalRecordNumber;
        private final int severity;
        private final long arrivalOrder;

        private Patient(String medicalRecordNumber, int severity, long arrivalOrder) {
            this.medicalRecordNumber = medicalRecordNumber;
            this.severity = severity;
            this.arrivalOrder = arrivalOrder;
        }

        public String getMedicalRecordNumber() {
            return medicalRecordNumber;
        }

        public int getSeverity() {
            return severity;
        }

        public long getArrivalOrder() {
            return arrivalOrder;
        }

        @Override
        public String toString() {
            return medicalRecordNumber + "|" + severity + "|" + arrivalOrder;
        }
    }

    private final PriorityQueue<Patient> waitingPatients = new PriorityQueue<>(
            Comparator.comparingInt(Patient::getSeverity).reversed()
                    .thenComparingLong(Patient::getArrivalOrder)
                    .thenComparing(Patient::getMedicalRecordNumber));
    private long nextArrivalOrder = 1;

    public Patient checkIn(String medicalRecordNumber, int severity) {
        if (medicalRecordNumber == null || medicalRecordNumber.isBlank()) {
            throw new IllegalArgumentException("medicalRecordNumber cannot be blank");
        }

        Patient patient = new Patient(
                medicalRecordNumber.trim(), severity, nextArrivalOrder++);
        waitingPatients.offer(patient);
        return patient;
    }

    public Patient peekNext() {
        return waitingPatients.peek();
    }

    public Patient callNext() {
        return waitingPatients.poll();
    }

    public int size() {
        return waitingPatients.size();
    }

    public boolean isEmpty() {
        return waitingPatients.isEmpty();
    }

    private static void printCallResult(Patient patient) {
        if (patient == null) {
            System.out.println("CALL|EMPTY");
        } else {
            System.out.println("CALL|" + patient);
        }
    }

    public static void main(String[] args) {
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        triage.checkIn("MR-1001", 3);
        triage.checkIn("MR-1002", 5);
        triage.checkIn("MR-1003", 5);
        triage.checkIn("MR-1004", 2);
        triage.checkIn("MR-1005", 4);

        System.out.println("NEXT|" + triage.peekNext());
        System.out.println("COUNT|" + triage.size());

        while (!triage.isEmpty()) {
            printCallResult(triage.callNext());
        }
        printCallResult(triage.callNext());
        System.out.println("COUNT|" + triage.size());
    }
}

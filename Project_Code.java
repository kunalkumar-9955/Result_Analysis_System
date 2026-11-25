import java.util.*;
import java.io.*;

class Student {
    String name;
    int m1, m2, m3;
    int total;
    double percentage;
    String grade;

    Student(String name, int m1, int m2, int m3) {
        this.name = name;
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;

        total = m1 + m2 + m3;
        percentage = total / 3.0;

        if (percentage >= 90)
            grade = "A+";
        else if (percentage >= 75)
            grade = "A";
        else if (percentage >= 60)
            grade = "B";
        else if (percentage >= 40)
            grade = "C";
        else
            grade = "F";
    }
}

public class Project_Code {

    static ArrayList<Student> students = new ArrayList<>();
    static HashMap<String, Student> studentMap = new HashMap<>();

    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "results.txt";

    // Add student
    static void addStudent() {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }

        if (studentMap.containsKey(name.toLowerCase())) {
            Student s = studentMap.get(name.toLowerCase());
            System.out.println(s.name + " (" + s.total + ") is already added");
            return;
        }

        System.out.print("Enter marks of 3 subjects: ");
        String input = sc.nextLine().trim();
        String[] marks = input.split("\\s+");

        if (marks.length != 3) {
            System.out.println("Enter exactly 3 subject marks.");
            return;
        }

        try {
            int m1 = Integer.parseInt(marks[0]);
            int m2 = Integer.parseInt(marks[1]);
            int m3 = Integer.parseInt(marks[2]);

            if (m1 < 0 || m1 > 100 ||
                m2 < 0 || m2 > 100 ||
                m3 < 0 || m3 > 100) {

                System.out.println("Invalid marks. Enter marks between 0 and 100.");
                return;
            }

            Student s = new Student(name, m1, m2, m3);

            students.add(s);
            studentMap.put(name.toLowerCase(), s);

            System.out.printf(
                "Result added. %s | Total: %d | %.1f%% | Grade %s%n",
                s.name, s.total, s.percentage, s.grade
            );

        } catch (NumberFormatException e) {
            System.out.println("Invalid marks. Enter valid integers.");
        }
    }

    // Search student by name
    static void searchStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        Student s = studentMap.get(name.toLowerCase());

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.printf(
            "%s | Total: %d | %.1f%% | Grade %s%n",
            s.name, s.total, s.percentage, s.grade
        );
    }

    // Display rank list
    static void rankList() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        ArrayList<Student> sorted = new ArrayList<>(students);

        sorted.sort((a, b) -> Integer.compare(b.total, a.total));

        System.out.println("\n----- RANK LIST -----");

        int rank = 1;

        for (Student s : sorted) {
            System.out.printf(
                "%d. %s | %d | %.1f%%%n",
                rank++, s.name, s.total, s.percentage
            );
        }
    }

    // Find topper and class average
    static void topperAndAverage() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        Student topper = Collections.max(
            students,
            Comparator.comparingInt(s -> s.total)
        );

        double totalPercentage = 0;

        for (Student s : students) {
            totalPercentage += s.percentage;
        }

        double average = totalPercentage / students.size();

        System.out.printf(
            "Topper: %s (%d)%n",
            topper.name, topper.total
        );

        System.out.printf(
            "Class average: %.1f%%%n",
            average
        );
    }

    // Load records from file
    static void loadFromFile() {
        File file = new File(FILE_NAME);

        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length != 4)
                    continue;

                try {
                    String name = data[0].trim();

                    if (name.isEmpty() ||
                        studentMap.containsKey(name.toLowerCase()))
                        continue;

                    int m1 = Integer.parseInt(data[1]);
                    int m2 = Integer.parseInt(data[2]);
                    int m3 = Integer.parseInt(data[3]);

                    if (m1 < 0 || m1 > 100 ||
                        m2 < 0 || m2 > 100 ||
                        m3 < 0 || m3 > 100)
                        continue;

                    Student s = new Student(name, m1, m2, m3);

                    students.add(s);
                    studentMap.put(name.toLowerCase(), s);

                } catch (NumberFormatException e) {
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading records.");
        }
    }

    // Save records to file
    static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME))) {

            for (Student s : students) {
                bw.write(
                    s.name + "," + s.m1 + "," +
                    s.m2 + "," + s.m3
                );
                bw.newLine();
            }

            System.out.println("Results saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving records.");
        }
    }

    // Main method
    public static void main(String[] args) {

        loadFromFile();

        while (true) {
            System.out.println("\n===== RESULT ANALYSIS SYSTEM =====");
            System.out.println("1. Add Result");
            System.out.println("2. Search Student");
            System.out.println("3. Rank List");
            System.out.println("4. Topper and Class Average");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent();
                    break;

                case "2":
                    searchStudent();
                    break;

                case "3":
                    rankList();
                    break;

                case "4":
                    topperAndAverage();
                    break;

                case "5":
                    saveToFile();
                    System.out.println("Exiting Result Analysis System.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
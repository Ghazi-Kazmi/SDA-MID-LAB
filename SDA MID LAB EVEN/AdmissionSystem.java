import java.util.*;

// Applicant Class
class Applicant {
    String id;
    String name;
    int age;
    double testScore;
    double interviewScore;

    public Applicant(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "\nApplicant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", testScore=" + testScore +
                ", interviewScore=" + interviewScore +
                "}\n";
    }
}

// Observer Interface
interface Observer {
    void update(String message);
}

// Notification Manager (Observer Implementation)
class NotificationManager implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[Notification] " + message);
    }
}

// Abstract Filter (Pipe and Filter)
abstract class Filter {
    protected List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    protected void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public abstract List<Applicant> process(List<Applicant> applicants);
}

// Eligibility Filter
class EligibilityFilter extends Filter {
    @Override
    public List<Applicant> process(List<Applicant> applicants) {
        List<Applicant> eligible = new ArrayList<>();
        for (Applicant applicant : applicants) {
            if (applicant.age >= 18) { // Basic eligibility criteria
                eligible.add(applicant);
            }
        }
        notifyObservers(eligible.size() + " out of " + applicants.size() + " applicants passed the Eligibility Filter.");
        return eligible;
    }
}

// Test Filter
class TestFilter extends Filter {
    @Override
    public List<Applicant> process(List<Applicant> applicants) {
        List<Applicant> passed = new ArrayList<>();
        for (Applicant applicant : applicants) {
            applicant.testScore = Math.random() * 100; // Simulate test scores
            if (applicant.testScore >= 50) { // Pass criteria
                passed.add(applicant);
            }
        }
        notifyObservers(passed.size() + " out of " + applicants.size() + " applicants passed the Test Filter.");
        return passed;
    }
}

// Interview Filter
class InterviewFilter extends Filter {
    @Override
    public List<Applicant> process(List<Applicant> applicants) {
        List<Applicant> passed = new ArrayList<>();
        for (Applicant applicant : applicants) {
            applicant.interviewScore = Math.random() * 10; // Simulate interview scores
            if (applicant.interviewScore >= 5) { // Pass criteria
                passed.add(applicant);
            }
        }
        notifyObservers(passed.size() + " out of " + applicants.size() + " applicants passed the Interview Filter.");
        return passed;
    }
}

// Merit List Filter
class MeritList extends Filter {
    @Override
    public List<Applicant> process(List<Applicant> applicants) {
        applicants.sort((a, b) -> Double.compare(
                (b.testScore + b.interviewScore),
                (a.testScore + a.interviewScore)
        ));
        notifyObservers("Merit List generated for " + applicants.size() + " applicants.");
        return applicants;
    }
}

// Admission System (Manages Filters and Workflow)
public class AdmissionSystem {
    private List<Filter> filters = new ArrayList<>();
    private NotificationManager notificationManager = new NotificationManager();

    public void addFilter(Filter filter) {
        filter.addObserver(notificationManager);
        filters.add(filter);
    }

    public void start(List<Applicant> applicants) {
        Scanner scanner = new Scanner(System.in);
        List<Applicant> currentApplicants = applicants;

        for (Filter filter : filters) {
            System.out.println("\nProceed to the next filter? (yes/no)");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("no")) {
                System.out.println("Admission process terminated.");
                return;
            }

            currentApplicants = filter.process(currentApplicants);
            System.out.println("Applicants after this stage: " + currentApplicants);
        }

        System.out.println("\nFinal Merit List:");
        for (Applicant applicant : currentApplicants) {
            System.out.println(applicant);
        }
    }
}

// Main Class
class Main {
    public static void main(String[] args) {
        // List of Applicants
        List<Applicant> applicants = new ArrayList<>();
        applicants.add(new Applicant("1", "Alice", 20));
        applicants.add(new Applicant("2", "Bob", 17));
        applicants.add(new Applicant("3", "Charlie", 19));
        applicants.add(new Applicant("4", "David", 22));
        applicants.add(new Applicant("5", "Eve", 18));

        // Create Admission System
        AdmissionSystem system = new AdmissionSystem();

        // Add Filters
        system.addFilter(new EligibilityFilter());
        system.addFilter(new TestFilter());
        system.addFilter(new InterviewFilter());
        system.addFilter(new MeritList());

        // Start Admission Process
        system.start(applicants);
    }
}
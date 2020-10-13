import java.time.LocalDate;

public class Client {
    private String name;
    private String persNumber;
    private LocalDate paidMembershipOnDate;

    // Konstruktor
    public Client(String name, String persNumber, LocalDate paidMembershipOnDate){
        this.name = name;
        this.persNumber = persNumber;
        this.paidMembershipOnDate = paidMembershipOnDate;
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getPersNumber() {
        return persNumber;
    }
    public LocalDate getPaidMembershipOnDate() {
        return paidMembershipOnDate;
    }
}
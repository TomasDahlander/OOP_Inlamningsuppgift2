import java.time.LocalDate;

/**
 * Created by Tomas Dahlander <br>
 * Date: 2020-10-12 <br>
 * Time: 14:08 <br>
 * Project: OOPInlämningsuppgift2 <br>
 */
public class Client {
    private String name;
    private String persNumber;
    private LocalDate paidMembershipOnDate;
    private LocalDate lastWorkout;

    // Konstruktor
    public Client(String name, String persNumber, LocalDate paidMembershipOnDate){
        this.name = name;
        this.persNumber = persNumber;
        this.paidMembershipOnDate = paidMembershipOnDate;
    }

    // Getters and setters
    public LocalDate getLastWorkout() {
        return lastWorkout;
    }
    public void setLastWorkout(LocalDate lastWorkout) {
        if(lastWorkout != null) this.lastWorkout = lastWorkout;
        else throw new NullPointerException("Can't set lastworkout to null.");
    }
    public String getName() {
        return name;
    }
    public String getPersNumber() {
        return persNumber;
    }
    public LocalDate getPaidMembershipOnDate() {
        return paidMembershipOnDate;
    }

    @Override
    public String toString() {
        return lastWorkout.toString() + " " + name + persNumber;
    }
}

    // 2020-10-11 Diamanda Djedi 7608021234
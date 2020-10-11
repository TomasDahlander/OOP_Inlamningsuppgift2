import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Created by Tomas Dahlander <br>
 * Date: 2020-10-08 <br>
 * Time: 17:30 <br>
 * Project: OOPInlämningsuppgift2 <br>
 */
public class BestGymEver {
    // * Metod som läser in ett sökvärde från användaren XXX
    // * Metod som söker igenom filen XXX
    // * Metod som tar emot 3 stringar och returnerar en boolean true om namn eller personnummer stämmer XXX
    // * Metod som tar emot ett datum för senaste betalning och returnerar true om kunden har betalat inom det senaste året XXX
    // * Metod som skriver till en fil XXX


    public StatusMembership status;  // NUVARANDE_MEDLEM,GAMMAL_MEDLEM,EJ_MEDLEM
    public boolean test = false;



    public String readInputData(String prompt, String optionalTestParameter){
        Scanner keyboardIn;
        if(test){
            keyboardIn = new Scanner(optionalTestParameter);
        }
        else{
            keyboardIn = new Scanner(System.in);
        }

        while (true) {
            try {
                System.out.println(prompt);
                String input = keyboardIn.nextLine();
                input = input.trim();
                return input;

            } catch (NullPointerException e) {
                System.out.println("Indata saknas");
                keyboardIn.next();
            } catch (NoSuchElementException e) {
                System.out.println("Indata saknas!");
                keyboardIn.next();
            } catch (Exception e) {
                System.out.println("Ospecifierat fel inträffade, försök igen!");
                keyboardIn.next();
                e.printStackTrace();
            }
        }
    }

    public String searchInFile(String input, String fileName, LocalDate todayTest) throws IOException {
        String persNr;
        String name;
        boolean existsInFile;
        boolean paidWithinLastYear;

        try(Scanner scan = new Scanner(new File(fileName))){
            while(scan.hasNext()){
                // Skannar och tilldear personnummer samt namn
                String line = scan.nextLine();
                int index = line.indexOf(",");
                persNr = line.substring(0,index);
                name = line.substring(index+1).trim();

                // Skannar in datum för senaste betalning.
                line = scan.nextLine();
                LocalDate lastPaid = LocalDate.parse(line);

                // Kontrollerar om input matchar med första raden i filen
                existsInFile = searchForPerson(input,persNr,name);

                // Om personen finns så kontrolleras ifall personen har betalat inom det senaste året
                if(existsInFile){
                    paidWithinLastYear = paidWithinLastYear(lastPaid,todayTest);
                    if(paidWithinLastYear) {
                        status = StatusMembership.NUVARANDE_MEDLEM;
                        return LocalDate.now() + " " + name + " " + persNr;
                    }
                    else {
                        status = StatusMembership.GAMMAL_MEDLEM;
                        return "Medlemmen har ej ett aktivt medlemskap.";
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Oväntat fel med scannern.");
        }

        status = StatusMembership.EJ_MEDLEM;
        return "Ny medlem som ej finns i systemet.";
    }

    public boolean searchForPerson(String input, String persNr, String name){
        if(persNr.equalsIgnoreCase(input.replace("-",""))) return true;
        else if(input.equalsIgnoreCase(name)) return true;
        else return false;
    }

    public boolean paidWithinLastYear(LocalDate lastPaid, LocalDate todayTest){
        LocalDate today;

        if(test)today = todayTest;
        else today = LocalDate.now();

        LocalDate lastYear = today.minusYears(1);

        return lastPaid.isAfter(lastYear);
    }

    public void printToFile(String fileName, String lineToAddToFile)throws IOException{
        try(PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)))){
            output.println(lineToAddToFile);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Något fel inträffade vid skrivning till fil.");
        }
    }



    public void mainProgram()throws IOException{
        String file = "Customers.txt";
        String message = "Sök på fullständigt namn eller personnummer med 10 siffror. Ex. 5004153364\nAvsluta progammet genom att skriva \"Exit\"";
        while(true) {
            String userInput = readInputData(message, null);
            if(userInput.equalsIgnoreCase("Exit")) System.exit(0);

            String result = searchInFile(userInput,file,null);

            // Om kund finns och har betalt senaste året. Skicka till metod för skriva till fil
            if(status == StatusMembership.NUVARANDE_MEDLEM){
                String outFile = "Overlook Of Customers Training Schedule.txt";
                printToFile(outFile,result);
                System.out.println("Information tillagt i filen: " + outFile + "\n");
            }

            // Om kund finns men ej har betalt. Skriv ut ett meddelande om detta
            else if(status == StatusMembership.GAMMAL_MEDLEM){
                System.out.println(result+"\n");
            }
            // Om kund ej finns skriv ut ett meddelande om detta
            else System.out.println(result+"\n");
        }
    }

    public static void main(String[] args) throws IOException{
        BestGymEver bge = new BestGymEver();
        bge.mainProgram();
    }

}

/*
Gymet ”Best Gym Ever” har problem. Deras datasystem har kraschat och går inte att använda.
Konsulten som byggde systemet är bortrest och kan inte komma och laga systemet på flera veckor.
Dessutom har någon postat på Flashback att Best Gym Evers system är sönder och att alla kan komma dit och träna gratis,
vilket har gjort att en hel del skumma typer har dykt upp.
Nu ber receptionisten dig om hjälp. Hen har lyckats rota fram en fil från hårddisken med alla kunders
namn, personnummer och när de senast betalade årsavgiften till gymmet.
Hen undrar om du kan skriva ett program där man skriver in en kunds personnummer eller namn när personen dyker upp på gymet,
sen söker programmet i filen och visar upp om kunden är en nuvarande medlem
(dvs om årsavgiften betalades för mindre än ett år sedan),
en före detta kund (om årsavgiften betalades för mer än ett år sedan)
eller om personen inte finns i filen och sålunda aldrig har varit medlem och är obehörig.

Posterna i filen vi läser från ser ut enligt följande:
7502031234, Anna Andersson
2017-05-03
8505132345, Per Persson
2015-12-29
…osv…

Dessutom undrar gymets personlige tränare om du kan fixa att när en betalande kund dyker upp så sparas det ner att
kunden har varit och tränat, i en egen, annan fil.
Detta för att personlige tränaren ska kunna fortsätta tracka hur ofta klienterna tränar.
(Vi kan anta att om en kund kommer till gymmet tränar hen, även om personen egentligen bara sitter i loungen och käkar banan).
Tränaren behöver veta namn, personnummer och datum när kunden var på gymmet (så detta ska alltså sparas i hens fil).

Betygskriterier:
För att bli Godkänd (G) krävs att du hjälper både receptionisten och den personlige tränaren och uppfyller deras önskemål.
Din lösning ska kunna:
• Läsa personposter från fil.
• Skriva till fil.
• Läsa in och parsa datum (tips: LocalDate är en bra klass för hantering av datum)
• Ha bra felhantering (relevanta felmeddelanden, exceptionhantering och try-with-resources)
• Koden ska vara enkelt läsbar och prydligt skriven.

För att bli Väl Godkänd (VG) måste lösningen uppfylla följande:
• Alla krav för att få G vara uppfyllda.
• Jobba testdrivet när du löser uppgiften.
• Minst 3 enhetstester måste finnas i lösningen (alltså minst 3 test-metoder, som var och en innehåller ett antal asserts),
som uppfyller följande krav:
a. Dessa tester ska alltid kunna köras med samma resultat.
(De får alltså inte vara beroende på t.ex. att man kör koden ett visst datum för att de ska bli gröna)
b. Testerna ska vara relevanta för programmets funktionalitet och inte vara extremt
simplistiska. (Ni kan alltså inte testa att 1==1 eller liknande, och inte heller simpla
getters och setters)
c. De ska gå att köra testerna automatiskt (det är alltså inte ok att testerna väntar på
input från användaren)
d. Alla testfallen ska gå gröna när de körs.

Vidare måste programmet demonstreras på ett professionellt sätt. För att uppnå detta, innan du
demonstrerar, gå noga igenom och kontrollera att programmet verkligen fungerar. Tänk igenom vad
du ska säga under redovisningen och hur du ska visa upp din kod så att du framstår som den stabile
programmerare du är. Du kommer att behöva visa både att programmet funkar och visa din kod. För
att alla ska hinna demonstrera får din demonstration ta max 7 minuter.
Koden skall vara uppladdad på GitHub i ett publikt repo, som kursledaren har åtkomst till. Lägg upp
en länk till ditt repo i den inlämnings-katalog på Portalen som är skapad för detta ändamål.
Denna uppgift görs individuellt. Lycka till!
 */
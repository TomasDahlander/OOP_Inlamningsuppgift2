import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class BestGymEver {
    // * Metod som läser in från en fil och skapar en lista med objekt XXX
    // * Metod som läser in ett sökvärde från användaren XXX
    // * Metod som söker i listan med clienter ifall en match hittas. Nyttjar andra metoder.
    // * Metod som tar emot en string och en client och returnerar true om namn eller personnummer matchar med sökvärde XXX
    // * Metod som tar emot ett datum för senaste betalning och returnerar true om kunden har betalat inom det senaste året XXX
    // * Metod som skriver till en fil XXX


    public MembershipStatus status;  // NUVARANDE_MEDLEM,GAMMAL_MEDLEM,EJ_MEDLEM
    public boolean test = false;
    Path inFilePath = Paths.get("Files\\Customers.txt");
    Path outFilePath = Paths.get("Files\\Overlook Of Customers Training Schedule.txt");



    public List<Client> getListWithClients(Path fileToReadFrom){
        List<Client> list = new ArrayList<>();

        try(Scanner scan = new Scanner(fileToReadFrom)){
            while(scan.hasNext()){
                String line = scan.nextLine();
                int index = line.indexOf(",");
                String persNr = line.substring(0,index);
                String name = line.substring(index+1).trim();

                line = scan.nextLine();
                LocalDate lastPaid = LocalDate.parse(line);

                list.add(new Client(name,persNr,lastPaid));
            }
        }catch(IOException e){
           // e.printStackTrace();
            System.out.println("Det gick ej att läsa från fil: " + fileToReadFrom.getFileName());
        }catch(Exception e){
           // e.printStackTrace();
            System.out.println("Ospecifiserat fel inträffade.");
        }
        return list;
    }

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
            } catch (Exception e) {
                System.out.println("Ospecifierat fel inträffade, försök igen!");
                keyboardIn.next();
               // e.printStackTrace();
            }
        }
    }



    public String searchInListForClient(String input, Client client, LocalDate todayTest){
        boolean paidWithinLastYear = false;
        boolean existsInFile = checkIfPersonExistsInList(input,client);
        if(existsInFile){
            paidWithinLastYear = paidWithinLastYear(client.getPaidMembershipOnDate(),todayTest);
        }

        if(paidWithinLastYear) {
            status = MembershipStatus.NUVARANDE_MEDLEM;
            return "Date: " + LocalDate.now() + " Name: " + client.getName() + " (" + client.getPersNumber()+")";
        }
        else if(existsInFile) {
            status = MembershipStatus.GAMMAL_MEDLEM;
            return "Medlemmen har ej ett aktivt medlemskap.";
        }
        else {
            status = MembershipStatus.EJ_MEDLEM;
            return "Ej hittat i databasen.";
        }
    }

    public boolean checkIfPersonExistsInList(String input, Client client){
        if(client.getPersNumber().equalsIgnoreCase(input.replace("-",""))) return true;
        else if(client.getName().equalsIgnoreCase(input)) return true;
        else return false;
    }

    public boolean paidWithinLastYear(LocalDate lastPaid, LocalDate todayTest){
        LocalDate today;

        if(test)today = todayTest;
        else today = LocalDate.now();

        LocalDate lastYear = today.minusYears(1);

        return lastPaid.isAfter(lastYear);
    }


    public void printToFile(Path fileName, String lineToAddToFile)throws IOException{
        try(PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(fileName.toFile(),true)))){
            output.println(lineToAddToFile);
        }catch (Exception e){
          //  e.printStackTrace();
            System.out.println("Kunde inte skriva till fil: " + fileName.getFileName());
        }
    }



    public void mainProgram() throws IOException{
        List<Client> clients = getListWithClients(inFilePath);

        String promptMessage = "Sök på fullständigt namn eller personnummer med 10 siffror." +
                "\nAvsluta progammet genom att skriva \"Exit\"";

        while(true) {
            String userInput = readInputData(promptMessage, null);
            if (userInput.equalsIgnoreCase("Exit")) System.exit(0);

            String result = "";
            for(int i = 0; i < clients.size(); i++){
                result = searchInListForClient(userInput,clients.get(i),null);
                if(status != MembershipStatus.EJ_MEDLEM) break;
            }

            if(status == MembershipStatus.NUVARANDE_MEDLEM){
                printToFile(outFilePath,result);
                System.out.println("Information tillagt i filen: " + outFilePath.getFileName() + "\n");
            }
            else if(status == MembershipStatus.GAMMAL_MEDLEM){
                System.out.println(result+"\n");
            }
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
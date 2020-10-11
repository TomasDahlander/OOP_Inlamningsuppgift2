import java.io.*;
import java.time.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Tomas Dahlander <br>
 * Date: 2020-10-08 <br>
 * Time: 17:30 <br>
 * Project: OOPInlämningsuppgift2 <br>
 */
public class BestGymEverTest {
    // * Testa Exception nullpointer XXX
    // * Testa inläsning XXX
    // * Testa sökning i fil XXX
    // * Testa ifall namn eller personnummer stämmer XXX
    // * Testa om medlem är nuvarande, gammal eller ej medlem XXX

    BestGymEver bge = new BestGymEver();

    @Test
    public final void readInputDataInputMismatchExceptionTest(){
        bge.test = true;
        String error = null;
        Throwable exception = assertThrows(NullPointerException.class,
                () -> bge.readInputData("Hej", error));
    }

    @Test
    public final void readInputDataTest(){
        bge.test = true;
        String searchWord = "charlotta andersson";
        assertEquals(bge.readInputData("Mata in sökord", searchWord), "charlotta andersson");
        assertFalse(bge.readInputData("Mata in sökord", searchWord) == "något användaren inte skrev...");
        assertTrue(bge.readInputData("Mata in sökord", searchWord) != "charlotta andersson");
    }

    @Test
    public final void searchInFileTest()throws IOException {
        bge.test = true;
        LocalDate today = LocalDate.parse("2020-10-10");
        String fileName = "Customers.txt";
        LocalDate actualToday = LocalDate.now();

        String input = "7603021234";
        assertTrue(bge.searchInFile(input,fileName,today) == "Medlemmen har ej ett aktivt medlemskap.");
        assertFalse(bge.searchInFile(input,fileName,today) == actualToday.toString()+" Alhambra Aromes 7603021234");
        assertFalse(bge.searchInFile(input,fileName,today) == "Ny medlem som ej finns i systemet.");
        // Alhambra Aromes 7603021234 2019-07-01

        String input2 = "Diamanda Djedi";
        assertEquals(bge.searchInFile(input2,fileName,today), actualToday.toString()+" Diamanda Djedi 7608021234");
        assertFalse(bge.searchInFile(input2,fileName,today) == "Medlemmen har ej ett aktivt medlemskap.");
        assertFalse(bge.searchInFile(input2,fileName,today) == "Ny medlem som ej finns i systemet.");
        // Diamanda Djedi 7608021234 2020-01-30

        String input3 = "5501319955";
        assertTrue(bge.searchInFile(input3,fileName,today) == "Ny medlem som ej finns i systemet.");
        assertFalse(bge.searchInFile(input3,fileName,today) == "Medlemmen har ej ett aktivt medlemskap.");
    }

    @Test
    public final void searchForPersonTest(){
        String input = "800526-4514";
        String persNr = "8005264514";
        String name = "Kerstin Danielsson";
        assertTrue(bge.searchForPerson(input,persNr,name));

        String input2 = "Kerstin Danielsson";
        String persNr2 = "8005264515";
        String name2 = "Kerstin Danielsson";
        assertTrue(bge.searchForPerson(input2,persNr2,name2));

        String input3 = "8005264514";
        String persNr3 = "8005264515";
        String name3 = "Kerstin Danielsson";
        assertFalse(bge.searchForPerson(input3,persNr3,name3));
    }

    @Test
    public final void paidWithinLastYearTest(){
        bge.test = true;
        LocalDate paid = LocalDate.parse("2019-11-10");
        LocalDate todaysDate = LocalDate.parse("2020-08-10");
        assertTrue(bge.paidWithinLastYear(paid,todaysDate));

        LocalDate paid2 = LocalDate.parse("2019-09-10");
        LocalDate todaysDate2 = LocalDate.parse("2020-11-10");
        assertFalse(bge.paidWithinLastYear(paid2,todaysDate2));
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
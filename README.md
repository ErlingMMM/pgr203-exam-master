[![Java CI with Maven](https://github.com/kristiania-pgr203-2021/pgr203-exam-Tomstiani/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr203-2021/pgr203-exam-Tomstiani/actions/workflows/maven.yml)


# PGR203 Avansert Java eksamen

**Utviklet av Erling Moland(https://github.com/ErlingMMM) og Tom Stian Ingebretson(https://github.com/Tomstiani)**

## Beskriv hvordan programmet skal testes:
Index siden har en liste med alle spørreundersøkelser (surveys), som er lagret i databasen. 
Her kan man trykke seg inn på den enkelte survey, som tar deg videre til spørsmålene, som kan besvares. 
Her kan man velge antall spørsmål, og eventuelle svaralternativer, avhengig av om man ønsker at det enkelte spørsmål skal besvares med for svaralternativer.
Man kan også velge antallet svaralternativer.
Dersom man derrimot ønsker at spørsmålet skal besvares med fritekst, setter man en max value for antall tegn. 
Om man ønsker å test-svare på sin egen survey man har lagd, vil valgene man tok da man lagde survey, tas hensyn til, da de er lagret i databasen. 

Data på nettsiden kobles sammen ved hjelp av FKs. Se vårt vedlegg for en illustrasjon av databasen.  

På venstre side er det en søkeboks som lar deg søke på et keyword (eller deler av et keyword) av alle survey descriptions.
Man skal kunne få opp alle surveys som matcher søket. Dette er sql spørringer som henter kode fra controller og dao klasse. Her har vi ikke brukt mye tid på frontend løsning. 

Øverst til høyre er det en knapp som tar deg videre til en html page for å lage nye surveys. Disse vil dukke opp på forsiden. Men merk at denne knappen er diabled før man har oppretter brukernavn og logget inn med mail adressen med mailadressen som ble lagret i databasen da man lagde ny bruker på forsiden.  
Første gang man laster siden, skal alle surveys sorteres etter rekkefølge de ble opprettet. 
Det er en knapp man kan trykke på for å sortere alle surveys etter survey name. Trykker man igjen, vil den sortere alfabetisk i motsatt rekkefølge. 
Dersom man trykker refresh på nettsiden, vil surveys riktig nok bli sortert for hver refresh. På en ekte nettside ville dette kanskje blitt litt snodig. 
Men vi valgte å la den være som den er, da dette ikke er en eksamen i brukervennlighet eller frontend. 
Vi ønsket å leke oss mest mulig med java og kode vi følte var pensum-relevant, og gjerne gjøre visuelle ting i nettleser som fks denne funskjonaliteten. 
Selv om vi er klar over at en ekte http server ville vært satt opp forskjellig fra vår kode. 

Vi hadde også en funskjonalitet vi valgte å ikke ha med i main koden: 
I working branchen følger det med en funskjonalitet som sørger for at brukeren får en css stylet 404 melding, visuelt sett når brukeren får en "file not found" i nettleseren.
Da det ikke ble tid til å få denne til å fungere med jar (altså uavhengig av hvor 404.html er plassert), valgte vi å ikke ha den med i main koden som skal være den sikre produksjonskoden. 
Et annet potensielt seniaro for server krasj, er om serveren ikke skulle finne html filen. 
Like fullt ligger den i working branch, da vi ville vise at dette er en funskjonalitet vi har vært under utvikling av å implementere. 

Med samme tankegang, valgte vi å legge til så mange issues som mulig. Også de vi ikke så for oss å rekke å løse. Dette er ment for å illustere hva vi kunna ha gjort og hva vi har tenkt på. 
Samtidig som vi ser for oss å stadig oppdatere og forbedre koden, gitt at dette var et "ekte" prosjekt. 

Vi har også en mulighet for å redigere verdiene i surveys (spørsmål, svaralternativer, beskrivelse, tittel)

Vi har gjort så det ikke er mulig å lage to brukere med samme mail. Vi har satt en exception som catches om brukeren prøver å gjøre  dette. 
Man får en sout melding som printes ut i konsollen. Dette krevde litt problemløsning i testene. 

Vi har gjort så det skal ikke være mulig å legge inn nye surveys dersom man ikke er logget inn. 
Når man besvarer en survey, vil info fra cookies hente mail adressen som er fremmednøkkel i survey tbellen med releasjon til user tabellen hvor den er unik. 
Etter man har laget survey, vil first name og last name bli hentet fra user table. Survey title og description vil også hentes fra survey tabellen. Dette vil være visuelt synlig på fremsiden hvor den enkelte survey har sin egen informasjonsboks som tar deg videre til rediger survey/ta survey/se svarene. 
Man kan logge ut bruker og logge inn med en annen bruker med en annen mailadresse. Om man gjør dette, vil tidligere surveys fortsatt vises som med den som lagde survey. Da info hentes fra databasen.
Man kan da lage surveys med annen bruker som vil få brukerinformasjon på brukeren man er logget inn som. 

Når man går inn på svarene for den spesifikke survey, vil man se hvilken bruker som har besvart den spesifikke surveyen. 

Etter mye googling og problemsøking har vi kommet frem til at Coveralls har en bug i seg der GITHUB_TOKEN ikke blir sendt til yaml filen.
Github sier selv at secrets.GITHUB_TOKEN skal sendes med automatisk dersom en action spør om den.

Moderne nettlesere skal spørre etter favicon.ico automatisk når en html side lastes, men våres gjør ikke det. Vi har derfor lagt opp til at bildet skal sendes dersom det blir spurt etter.

**ER-MODELL AV DATABASE:**
![alt text](/src/main/resources/images/lucidChart.png)

## Korreksjoner av eksamensteksten i Wiseflow:

**DET ER EN FEIL I EKSEMPELKODEN I WISEFLOW:**

* I `addOptions.html` skulle url til `/api/tasks` vært `/api/alternativeAnswers` (eller noe sånt)

**Det er viktig å være klar over at eksempel HTML i eksamensoppgaven kun er til illustrasjon. Eksemplene er ikke tilstrekkelig for å løse alle ekstraoppgavene og kandidatene må endre HTML-en for å være tilpasset sin besvarelse**



## Notes:
Vi vet at SaveQuestiontypes klassen sikkert ikke er en relatistisk måte å gjøre det på i virkeligheten. Men ettersom de fire 4 question types må lagres i riktig rekkefølge for at koden skal fungere mest optimalt, valgte vi å legge de inn slik for denne eksamen. 
At de må legges inn i denne rekkefølgen har med javascript koden å gjøre. Javascript er slik vi har forstått det, ikke primært pensum i denne eksamen. Derfor så vi oss fornøyde med løsningen for denne gang. 
Kilder brukt: Vi har brukt forelesningskoden som inspirasjon. Når det gjelder javascript koden vi har brukt for cookies, så er denne ikke kodet opp fra bunnen. Da slik vi tolker oppgaven ikke dette er en del av pensum/direkte hva som er scope for denne eksamensoppgaven.
Vi har da sett på fremgangsmåten på https://www.w3schools.com/js/js_cookies.asp. Men forsøkt å gjøre koden til vår egen. Javakoden (også med tanke på cookies), er derimot, kodet med egen fremgangsmåte. 

Vi har brukt JSON i kontrollerene. Vi har brukt mye tid på å prøve å få dette til å kjøre med utf-8. Men har ikke lykkes med å finne en løsning på problemet. 

## Learning proses:
Dette har vært en fin oppgave hvor vi har lært mye java og faktisk en del javacript og generell problemløsning. 
Vi har lært mye om oss selv og å jobbe som et team hvor man parprogammerer og hjelper hverandre mot et felles mål. 
Det har også vært en fin læringsprosess å få github enda mer inn i fingrene enn vi hadde fra før. Det har stort sett gått veldig fint, selv om vi har vært borti et par merge konflikter da vi har lagt ned en del tid i denne eksamen. 
Men også dette har vært lærerikt. Vi har brukt flere verktøy. For eksempel code coverage i Intellij for å se hvor stor del av koden vår som dekkes av tester. 


## Sjekkliste

## Vedlegg: Sjekkliste for innlevering

* [x] Dere har lest eksamensteksten
* [x] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [x] Koden er sjekket inn på github.com/pgr203-2021-repository
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [x] `README.md` inneholder en korrekt link til Github Actions
* [x] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
* [x] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [x] `README.md` inneholder et diagram som viser datamodellen

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [x] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [x] Serveren leser HTML-filer fra JAR-filen slik at den ikke er avhengig av å kjøre i samme directory som kildekoden
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [x] Programmet kan opprette spørsmål og lagrer disse i databasen (påkrevd for bestått)
* [x] Programmet kan vise spørsmål (påkrevd for D)
* [x] Programmet kan legge til alternativ for spørsmål (påkrevd for D)
* [x] Programmet kan registrere svar på spørsmål (påkrevd for C)
* [x] Programmet kan endre tittel og tekst på et spørsmål (påkrevd for B)

### Ekstraspørsmål (dere må løse mange/noen av disse for å oppnå A/B)

* [x] Før en bruker svarer på et spørsmål hadde det vært fint å la brukeren registrere navnet sitt. Klarer dere å implementere dette med cookies? Lag en form med en POST request der serveren sender tilbake Set-Cookie headeren. Browseren vil sende en Cookie header tilbake i alle requester. Bruk denne til å legge inn navnet på brukerens svar
* [ ] Når brukeren utfører en POST hadde det vært fint å sende brukeren tilbake til dit de var før. Det krever at man svarer med response code 303 See other og headeren Location
* [ ] Når brukeren skriver inn en tekst på norsk må man passe på å få encoding riktig. Klarer dere å lage en <form> med action=POST og encoding=UTF-8 som fungerer med norske tegn? Klarer dere å få æøå til å fungere i tester som gjør både POST og GET?
* [ ] Å opprette og liste spørsmål hadde vært logisk og REST-fult å gjøre med GET /api/questions og POST /api/questions. Klarer dere å endre måten dere hånderer controllers på slik at en GET og en POST request kan ha samme request target?
* [x] Vi har sett på hvordan å bruke AbstractDao for å få felles kode for retrieve og list. Kan dere bruke felles kode i AbstractDao for å unngå duplisering av inserts og updates?
* [ ] Dersom noe alvorlig galt skjer vil serveren krasje. Serveren burde i stedet logge dette og returnere en status code 500 til brukeren
* [x] Dersom brukeren går til http://localhost:8080 får man 404. Serveren burde i stedet returnere innholdet av index.html
* [ ] Et favorittikon er et lite ikon som nettleseren viser i tab-vinduer for en webapplikasjon. Kan dere lage et favorittikon for deres server? Tips: ikonet er en binærfil og ikke en tekst og det går derfor ikke an å laste den inn i en StringBuilder
* [ ] I forelesningen har vi sett på å innføre begrepet Controllers for å organisere logikken i serveren. Unntaket fra det som håndteres med controllers er håndtering av filer på disk. Kan dere skrive om no.kristiania.http.HttpServer til å bruke en FileController for å lese filer fra disk?
* [ ] Kan dere lage noen diagrammer som illustrerer hvordan programmet deres virker?
* [ ] JDBC koden fra forelesningen har en feil ved retrieve dersom id ikke finnes. Kan dere rette denne?
* [x] I forelesningen fikk vi en rar feil med CSS når vi hadde `<!DOCTYPE html>`. Grunnen til det er feil content-type. Klarer dere å fikse det slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer?
* [ ] Klarer dere å lage en Coverage-rapport med GitHub Actions med Coveralls? (Advarsel: Foreleser har nylig opplevd feil med Coveralls så det er ikke sikkert dere får det til å virke)
* [x] FARLIG: I løpet av kurset har no.kristiania.http.HttpServer og tester fått funksjonalitet som ikke lenger er nødvendig. Klarer dere å fjerne alt som er overflødig nå uten å også fjerne kode som fortsatt har verdi? (Advarsel: Denne kan trekke ned dersom dere gjør det feil!)
  

package com.test.util;

import com.test.entity.Person;

import java.util.*;

public class RandomPersonGeneratorImpl implements RandomPersonGenerator {
    
    private static final List<String> firstNamesMale;
    private static final List<String> firstNamesFemale;
    private static final List<String> lastNamesMale;
    private static final List<String> lastNamesFemale;
    private static final List<String> webAddresses;
    private final Random r = new Random();

    public Person generateRandomPerson() {
        Person person = new Person();
        person.setPersonId(fakePersonId());
        boolean male = r.nextBoolean();
        person.setFirstName(randomFirstName(male));
        person.setLastName(randomLastName(male));
        person.setMobile(randomPhoneNumber());
        person.setPesel(fakePesel());
        person.setEmail(fakeEmail(person));
        //todo check for unique pesel
        return person;
    }

    public List<Person> generateRandomPersons(int quantity) {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Person person = generateRandomPerson();
            personList.add(person);
        }
        //todo check for unique pesel
        return personList;
    }

    private String fakePersonId() {
        return Long.toString(++Person.nextPersonId);
    }

    private String randomFirstName(boolean male) {
        if (!male) return firstNamesFemale.get(r.nextInt(firstNamesFemale.size()));
        return firstNamesMale.get(r.nextInt(firstNamesMale.size()));
    }

    private String randomLastName(boolean male) {
        if (!male) return lastNamesFemale.get(r.nextInt(lastNamesFemale.size()));
        return lastNamesMale.get(r.nextInt(lastNamesMale.size()));
    }

    private String fakePesel() {
//      https://en.wikipedia.org/wiki/PESEL
        long peselFirstPart = r.nextInt(10000, 100000);
        long peselSecondPart = r.nextInt(100000, 1000000);
        return String.valueOf(peselFirstPart) + peselSecondPart;
    }

    private String randomPhoneNumber() {
        int minPhoneNumber = 500_000_001;
        int maxPhoneNumber = 900_000_000;
        return Integer.toString(r.nextInt(minPhoneNumber, maxPhoneNumber));
    }

    private String fakeEmail(Person person) {
        StringBuilder stringBuilder = new StringBuilder();
        String address = webAddresses.get(r.nextInt(webAddresses.size()));
        return stringBuilder.append(person.getFirstName().toLowerCase()).append(".").append(person.getLastName().toLowerCase())
                .append("@").append(address).toString();
    }


    static {
        firstNamesMale = List.of("Adam", "Bartosz", "Cezary", "Dariusz", "Emil", "Filip", "Grzegorz", "Henryk", "Igor", "Jacek", "Kamil",
                "Łukasz", "Marek", "Norbert", "Oskar", "Piotr", "Rafał", "Sebastian", "Tomasz", "Wojciech", "Zbigniew", "Albert", "Bogdan",
                "Czesław", "Dawid", "Edward", "Franciszek", "Grzegorz", "Henryk", "Ignacy", "Jerzy", "Kazimierz", "Lech", "Marian", "Natan",
                "Olek", "Patryk", "Radosław", "Szymon", "Tadeusz", "Waldemar", "Zygmunt", "Andrzej", "Bartłomiej", "Cezary", "Damian", "Eryk",
                "Feliks", "Gabriel", "Henryk", "Igor", "Jacek", "Karol", "Łukasz", "Maciej", "Nikodem", "Oskar", "Paweł", "Rafał", "Stanisław",
                "Tadeusz", "Wiktor", "Zdzisław", "Albert", "Borys", "Cyprian", "Daniel", "Emil", "Fryderyk", "Grzegorz", "Hubert", "Ireneusz",
                "Janusz", "Kamil", "Leon", "Mikołaj", "Olivier", "Piotr", "Ryszard", "Sebastian", "Tomasz", "Wojciech", "Zbigniew", "Aleksander",
                "Bogumił", "Cezary", "Dominik", "Edward", "Franciszek", "Grzegorz", "Henryk", "Igor", "Jakub", "Kacper", "Łukasz", "Mateusz");

        firstNamesFemale = List.of("Ada", "Agnieszka", "Alicja", "Aneta", "Angelika", "Anna", "Barbara", "Beata", "Bogna", "Dagmara", "Danuta",
                "Dominika", "Dorota", "Edyta", "Eliza", "Elżbieta", "Ewa", "Gabriela", "Grażyna", "Halina", "Hanna", "Irena", "Iwona", "Jadwiga", "Joanna",
                "Jolanta", "Justyna", "Kamila", "Karolina", "Katarzyna", "Kinga", "Klaudia", "Krystyna", "Katarzyna", "Lucyna", "Magdalena", "Małgorzata",
                "Maja", "Maria", "Marlena", "Martyna", "Monika", "Natalia", "Natasza", "Olga", "Patrycja", "Paulina", "Renata", "Sylwia", "Teresa",
                "Urszula", "Wanda", "Weronika", "Zofia", "Zuzanna", "Aleksandra", "Agata", "Adrianna", "Anita", "Blanka", "Celina", "Dorota", "Daria",
                "Diana", "Ewelina", "Emilia", "Ewa", "Helena", "Ilona", "Izabela", "Jolanta", "Kaja", "Kalina", "Lidia", "Magdalena", "Malwina", "Nadia",
                "Nina", "Ola", "Oliwia", "Pola", "Renata", "Roksana", "Roza", "Sabina", "Sabrina", "Sara", "Sonia", "Tamara", "Tola", "Urszula", "Wiktoria",
                "Zuzanna");

        lastNamesMale = List.of("Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński", "Lewandowski", "Zieliński", "Szymański",
                "Woźniak", "Dąbrowski", "Kozłowski", "Jankowski", "Mazur", "Wojciechowski", "Kwiatkowski", "Krawczyk", "Kaczmarek", "Piotrowski",
                "Grabowski", "Zając", "Pawłowski", "Michalski", "Król", "Nowakowski", "Wieczorek", "Wróbel", "Jabłoński", "Dudek", "Adamczyk",
                "Majewski", "Nowicki", "Olszewski", "Stępień", "Jaworski", "Malinowski", "Pawlak", "Witkowski", "Walczak", "Stasiak", "Zalewski",
                "Sikora", "Włodarczyk", "Sawicki", "Szewczyk", "Sobczak", "Kubiak", "Baran", "Duda", "Klimek", "Ostrowski", "Borkowski",
                "Baranowski", "Markowski", "Brzeziński", "Zawadzki", "Sadowski", "Kołodziej", "Gajewski", "Sikorski", "Zakrzewski", "Szulc",
                "Wesołowski", "Kucharski", "Małecki", "Marek", "Bednarek", "Kasprzak", "Makowski", "Sobolewski", "Marciniak", "Włodarczyk",
                "Krajewski", "Błaszczyk", "Górecki", "Urban", "Kaczmarczyk", "Wasilewski", "Sikorski", "Tomczak", "Domagała", "Wrona", "Jarosz",
                "Piątek", "Walewski", "Nowicki", "Olejniczak", "Łukasik", "Wilczyński", "Kalinowski", "Lis", "Mazurek", "Wysocki", "Adamski",
                "Kaźmierczak", "Wasik");

        lastNamesFemale = List.of("Nowak", "Kowalska", "Wiśniewska", "Dąbrowska", "Lewandowska", "Wójcik", "Kamińska", "Kowalczyk", "Zielińska",
                "Szymańska", "Woźniak", "Kozłowska", "Jankowska", "Wojciechowska", "Kwiatkowska", "Kaczmarek", "Mazur", "Krawczyk", "Piotrowska", "Grabowska",
                "Pawłowska", "Michalska", "Nowakowska", "Adamczyk", "Dudek", "Zając", "Wieczorek", "Jabłońska", "Król", "Majewska", "Olszewska", "Jaworska",
                "Wróbel", "Malinowska", "Pawlak", "Witkowska", "Walczak", "Stępień", "Górka", "Baran", "Rutkowska", "Michalak", "Sikora", "Ostrowska",
                "Tomaszewska", "Pietrzak", "Zalewska", "Wróblewska", "Marciniak", "Jasińska", "Zawadzka", "Jakubowska", "Sadowska", "Bak", "Włodarczyk",
                "Wilk", "Chmielewska", "Lis", "Mazurek", "Wysocka", "Kubiak", "Orłowska", "Wasilewska", "Sobczak", "Czarnecka", "Kucharska", "Andrzejewska",
                "Kaczmarczyk", "Borkowska", "Czerwińska", "Rożek", "Marcinkowska", "Domańska", "Nowicka", "Szymczak", "Kowalik", "Kos", "Urban", "Dębska",
                "Czech", "Janik", "Pająk", "Cieślak", "Leszczyńska", "Lipińska", "Wesołowska", "Bednarek", "Madej", "Milewska", "Stefańska", "Jastrzębska",
                "Wilczyńska", "Olszewski", "Ciesielska", "Klimczak", "Różańska", "Madej", "Rogowska", "Olejnik", "Konieczna", "Sowa");

        webAddresses = List.of("gmail.com", "yahoo.com", "outlook.com", "aol.com", "icloud.com", "zohomail.com", "protonmail.com",
                "gmx.com", "mail.com", "fastmail.com", "live.com", "hushmail.com", "zoho.eu", "hotmail.com", "posteo.de", "migadu.com",
                "mailfence.com", "aolmail.com");
    }
}//end of class

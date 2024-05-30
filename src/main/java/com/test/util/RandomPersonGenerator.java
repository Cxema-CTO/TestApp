package com.test.util;

import com.test.entity.Person;

import java.util.List;

public interface RandomPersonGenerator {

    public Person generateRandomPerson();

    public List<Person> generateRandomPersons(int quantity);

}

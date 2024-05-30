package com.test.util;

import com.test.entity.Person;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class RandomPersonGeneratorImplTest {

    RandomPersonGenerator rpg = new RandomPersonGeneratorImpl();

    @Test
    public void generateRandomPerson() {
        Person person = rpg.generateRandomPerson();
        assertEquals(person.getClass(), Person.class);
    }

    @Test
    public void generateRandomPersons() {
        List<Person> items = rpg.generateRandomPersons(5);
        assertFalse(items.isEmpty());
    }
}
package com.test.service;

import com.test.entity.Person;

import java.util.List;

public interface PersonService {

    // todo for future check collisions

//    boolean ifPersonExistInDb(String id);

    List<Person> getAllPersonFromDB(String db);

    Person getPersonById(String db, long id);

    void savePerson(String db, Person person);

    void deletePerson(String db, Person person);


}

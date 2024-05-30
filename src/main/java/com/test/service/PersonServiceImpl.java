package com.test.service;

import com.test.entity.Person;
import com.test.repository.FileManager;
import com.test.repository.FileManagerImpl;
import com.test.util.XMLConverter;
import com.test.util.XMLConverterImpl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PersonServiceImpl implements PersonService {

    /**
     * @param db EXTERNAL or INTERNAL
     * @return list of Persons from db
     */
    public List<Person> getAllPersonFromDB(String db) {
        List<Person> result = new ArrayList<>();
        FileManager fileManager = new FileManagerImpl();
        List<Path> pathList = fileManager.getListFilePathsInFolder(db);
        XMLConverter xmlConverter = new XMLConverterImpl();
        for (Path path : pathList) {
            Person person = new Person();// don't touch this
            person = xmlConverter.convertXMLDataToPerson(fileManager.loadFile(path));
            result.add(person);
        }
        return result;
    }

    public Person getPersonById(String db, long id) {
        FileManager fileManager = new FileManagerImpl();
        Path path = fileManager.getPathToFile(db, id);
        XMLConverter xmlConverter = new XMLConverterImpl();
        return xmlConverter.convertXMLDataToPerson(fileManager.loadFile(path));
    }

    public void savePerson(String db, Person person) {
        FileManager fileManager = new FileManagerImpl();
        Path path = fileManager.getPathToFile(db, person.getPersonId());
        fileManager.saveFile(new XMLConverterImpl().convertPersonToXMLData(person), path);
    }

    public void deletePerson(String db, Person person) {
        FileManager fileManager = new FileManagerImpl();
        Path path = fileManager.getPathToFile(db, person.getPersonId());
        fileManager.removeFile(path);
    }


}//end of class

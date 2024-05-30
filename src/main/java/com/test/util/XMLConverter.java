package com.test.util;

import com.test.entity.Person;

public interface XMLConverter {

    public String convertPersonToXMLData(Person person);
    public Person convertXMLDataToPerson(String string);

}

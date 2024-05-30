package com.test.util;

import com.test.entity.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class XMLConverterImplTest {
    Person person = new Person("1", "Sara", "Connor", "555777999",
            "sara.connor@terminator.com", "12345678901");
    String xml = "<Person><personId>1</personId><firstName>Sara</firstName><lastName>Connor</lastName>" +
            "<mobile>555777999</mobile><email>sara.connor@terminator.com</email><pesel>12345678901</pesel></Person>";


    @Test
    public void testConvertPersonToXMLData() {
        XMLConverter xmlConverter = new XMLConverterImpl();
        String toXML = xmlConverter.convertPersonToXMLData(person);
        assertEquals(xml, toXML);
    }

    @Test
    public void testConvertXMLDataToPerson() {
        XMLConverter xmlConverter = new XMLConverterImpl();
        Person toPerson = xmlConverter.convertXMLDataToPerson(xml);
        assertEquals(person, toPerson);
    }


}
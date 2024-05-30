package com.test.util;

import com.test.entity.Person;

public class XMLConverterImpl implements XMLConverter {
    public String convertPersonToXMLData(Person person) {
//        Simple way :)
        String start = "<Person>";
        String end = "</Person>";
        StringBuilder answer = new StringBuilder();//Don't touch this, StringBuilder is efficient solution
        answer.append(start)
                .append("<personId>" + person.getPersonId() + "</personId>")
                .append("<firstName>" + person.getFirstName() + "</firstName>")
                .append("<lastName>" + person.getLastName() + "</lastName>")
                .append("<mobile>" + person.getStringMobile() + "</mobile>")
                .append("<email>" + person.getEmail() + "</email>")
                .append("<pesel>" + person.getStringPesel() + "</pesel>")
                .append(end);
        return answer.toString();
    }


    public Person convertXMLDataToPerson(String string) {
        //        I read so much documentation :) StringBuilder methods 'delete' and 'substring' is the fastest solution
        //        Perhaps a more elegant and versatile solution could be devised,
        //        but for this task, what we have is more than sufficient :)
        Person person = new Person();
        StringBuilder buffer = new StringBuilder().append(string);
        buffer.delete(0, buffer.indexOf("<personId>") + 10);
        person.setPersonId(buffer.substring(0, buffer.indexOf("</personId>")));
        buffer.delete(0, buffer.indexOf("<firstName>") + 11);
        person.setFirstName(buffer.substring(0, buffer.indexOf("</firstName>")));
        buffer.delete(0, buffer.indexOf("<lastName>") + 10);
        person.setLastName(buffer.substring(0, buffer.indexOf("</lastName>")));
        buffer.delete(0, buffer.indexOf("<mobile>") + 8);
        person.setMobile(buffer.substring(0, buffer.indexOf("</mobile>")));
        buffer.delete(0, buffer.indexOf("<email>") + 7);
        person.setEmail(buffer.substring(0, buffer.indexOf("</email>")));
        buffer.delete(0, buffer.indexOf("<pesel>") + 7);
        person.setPesel(buffer.substring(0, buffer.indexOf("</pesel>")));

        return person;
    }
}

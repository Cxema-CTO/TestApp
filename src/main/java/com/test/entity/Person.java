package com.test.entity;

import java.util.Objects;

public class Person {

    public static long nextPersonId = 0;
    private String personId;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String pesel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personId, person.personId) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(mobile, person.mobile) && Objects.equals(email, person.email) && Objects.equals(pesel, person.pesel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, firstName, lastName, mobile, email, pesel);
    }

    public Person() {
    }

    public Person(String personId, String firstName, String lastName, String mobile, String email, String pesel) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.pesel = pesel;
    }

    public Long getPersonId() {
        return Long.parseLong(personId);
    }

    public String getStringPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile.replaceAll("^(.{3})(.{3})(.{3})$", "+48 $1 $2 $3");
    }// don't touch this for correct show in tableView

    public long getLongMobile() {
        return Long.parseLong(mobile);
    }

    public String getStringMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStringPesel() {
        return pesel;
    }

    public Long getPesel() {
        if (!Objects.equals(pesel, null)) {// protect from empty strings in the PESEL
            try {
                return Long.parseLong(pesel);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }


    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }

    public String toStringToShowInDb() {
        return firstName + " " + lastName + " " + email + " " +
                mobile.replaceAll("^(.{3})(.{3})(.{3})$", "+48 $1 $2 $3")
                + " " + pesel;
    }

}//end of class

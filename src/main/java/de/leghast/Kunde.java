package de.leghast;

import javafx.beans.property.SimpleStringProperty;

public class Kunde {
    private final SimpleStringProperty firstName = new SimpleStringProperty("");
    private final SimpleStringProperty lastName = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleStringProperty telefon = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");

    public Kunde(String firstName, String lastName, String email, String telefon, String date) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setTelefon(telefon);
        setDate(date);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String fName) {
        lastName.set(fName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String tel) {
        email.set(tel);
    }

    public String getTelefon() {
        return telefon.get();
    }

    public void setTelefon(String tel) {
        telefon.set(tel);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String datum) {
        date.set(datum);
    }
}
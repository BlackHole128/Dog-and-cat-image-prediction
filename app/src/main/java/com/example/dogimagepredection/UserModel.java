package com.example.dogimagepredection;

public class UserModel {
    private String name,number,BirthDay,eamil;

    public UserModel() {
    }

    public UserModel(String name, String number,String BirthDay ,String eamil) {
        this.name = name;
        this.number = number;
        this.eamil = eamil;
        this.BirthDay = BirthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEamil() {
        return eamil;
    }

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }

    public String getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(String birthDay) {
        BirthDay = birthDay;
    }
}

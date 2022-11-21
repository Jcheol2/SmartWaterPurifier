package com.example.smartpurifier;

public class SingerDTO {
    String name;
    String phoneNum;
    int age;
    int resId;
    String med;
    String sex;

    public SingerDTO() {
    }

    public SingerDTO(String name, String phoneNum, int age, String sex, int resId, String med) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.age = age;
        this.sex = sex;
        this.resId = resId;
        this.med = med;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String name) {
        this.med = med;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
package br.com.cardapia.company.acessmap.bean;

import java.io.Serializable;

/**
 * Created by tvtios-01 on 12/11/17.
 */

public class FacesBean  implements Serializable{

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "FacesBean{" +
                "age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

    private int age;
    private String gender;

}

package com.example.springapp83.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonDTO {

    // В этом классе будут описаны те поля, которые мы будем получать от клиента и отправлять ему.

    // Пользователь вручную указываем поля name, age и email. Их сюда и скопируем. Все остальные поля он
    // не видит и они назначаются на сервере.
    // У полей не нужно указывать аннотацию @Column, так как он не связан с БД.
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    @Min(value = 0, message = "Age should be not less than 0")
    private int age;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email shouldn't be empty")
    private String email;


    // Создаем для этих полей геттеры и сеттеры.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

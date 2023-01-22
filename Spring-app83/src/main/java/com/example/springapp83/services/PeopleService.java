package com.example.springapp83.services;

import com.example.springapp83.models.Person;
import com.example.springapp83.repositories.PeopleRepository;
import com.example.springapp83.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Persistent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> allPeople() {
        return peopleRepository.findAll();
    }


    public Person person (int id) {
        return peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void savePerson(Person person) {

        // Назначаем другие поля человеку.
        enrichPerson(person);
        peopleRepository.save(person);
    }


    // В этом методе будут назначаться поля со стороны сервера, то есть которые не назначает
    // пользователь.
    private void enrichPerson(Person person) {

        // Назначаем текущее время при создании и обновлении с помощью метода now.
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());

        // Имя пользователя можно было получать с помощью Spring Security.
        person.setCreatedWho("ADMIN");
    }
}

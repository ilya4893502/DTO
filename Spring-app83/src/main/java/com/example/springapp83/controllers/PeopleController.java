package com.example.springapp83.controllers;

import com.example.springapp83.dto.PersonDTO;
import com.example.springapp83.models.Person;
import com.example.springapp83.services.PeopleService;
import com.example.springapp83.util.PersonError;
import com.example.springapp83.util.PersonNotCreatedException;
import com.example.springapp83.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    // Внедрим созданный бин ModelMapper.
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }


    @GetMapping()
    public List<PersonDTO> allpeople() {

        // Тут будем возвращать список из PersonDTO.
        return peopleService.allPeople().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
        // Для каждого объекта Person вызовем метод convertToPersonDTO, чтобы преобразовать его в
        // PersonDTO. И соберем эти объекты в список.
    }


    @GetMapping("/{id}")
    public PersonDTO person(@PathVariable("id") int id) {

        // Используем метод convertToPersonDTO, чтобы отправить пользователю PersonDTO.
        return convertToPersonDTO(peopleService.person(id));
    }


    @PostMapping()
    // Изменим то, что мы принимаем от клиента. Вместо модели нужно принимать DTO. Это DTO мы будем
    // конвертировать в модель в самом контроллере.
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid PersonDTO personDTO,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError fieldError : errors) {
                errorMessage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }

            throw new PersonNotCreatedException(errorMessage.toString());
        }

        // Мы в методе savePerson создадим метод convertToPerson, который будет принимать personDTO
        // и возвращать человека.
        peopleService.savePerson(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<PersonError> notFoundPerson(PersonNotFoundException e) {

        PersonError personError = new PersonError(
                "Такой страницы не существует",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(personError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonError> notCreatedPerson(PersonNotCreatedException e) {

        PersonError personError = new PersonError(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(personError, HttpStatus.BAD_REQUEST);
    }


//    private Person convertToPerson(PersonDTO personDTO) {

        // В этом методе мы создаем объект Person.
//        Person person = new Person();

        // И все поля, которые пришли в DTO мы копируем в Person.
//        person.setName(personDTO.getName());
//        person.setAge(personDTO.getAge());
//        person.setEmail(personDTO.getEmail());

        // Далее в этого человека положим данные, которые назначаются на сервере. Это будет
        // происходить в методе enrichPerson. Этот метод будет храниться в сервисе.
//      //  enrichPerson(person);
        // Этот метод также указывается в сервисе при сохранении человека.

//        return person;
//    }
    // Проблема этого метода в том, что мы назначаем поля вручную. Это нормально, когда всего
    // несколько полей. Но когда полей много это не удобно.
    // Чтобы автоматизировать используется зависимость @ModelMapper. Для этого добавим зависимость
    // ModelMapper.


    // Теперь перепишем метод convertToPerson.
    private Person convertToPerson(PersonDTO personDTO) {

        // Создадим новый объект ModelMapper.
        ModelMapper mapper = new ModelMapper();
        // В этом объекте мы задаем исходный объект и целевой класс (тот класс, в объект которого мы
        // хотим преобразовать).

        // Создание ModelMapper можно делегировать Spring'у. Spring создаст этот объект, который можно
        // внедрять во все контроллеры и использовать один и тот же объект ModelMapper.
        // Создадим его в конфигурационном классе SpringApp83Application.

        // Мы хотим смапить из PersonDTO в Person.
        Person person = mapper.map(personDTO, Person.class);
        // ModelMapper найдет все поля PersonDTO и положит их в Person.

        return person;

        // При создании бина ModelMapper.
//        return modelMapper.map(personDTO, Person.class);
    }


    // Для метода person создадим метод convertToPersonDTO.
    private PersonDTO convertToPersonDTO(Person person) {

        // Тут мы наоборот из Person переводим в PersonDTO, так как мы отдаем данные.
        return modelMapper.map(person, PersonDTO.class);
    }
}

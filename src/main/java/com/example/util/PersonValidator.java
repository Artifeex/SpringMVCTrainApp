package com.example.util;

import com.example.config.dao.PersonDAO;
import com.example.config.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    //Этим методом мы определяем, может ли для валидации сущности использоваться наш класс
    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass); //валидатор будет вызываться только для валидации Person
    }

    //Spring будет вызывать этот метод самосотоятельно, когда с формы будет приходить объект Person
    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        //проверить, есть ли человек с таким же email в нашей БД
        if(personDAO.show(person.getEmail()).isPresent()) {
            //добавляем ошибку(1-на каком поле произошла, 2-код ошибки, 3-сообщение об ошибки для пользователя)
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}

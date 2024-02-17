package com.example.config.dao;

import com.example.config.models.Person;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM PERSON", new BeanPropertyRowMapper<>(Person.class));
    }

    //Т.к. query возвращает список, но мы хотим вернуть только 1 элемент, то возвращаем если список пустой null, либо первого встретив
    //шегося человека с id = id
    //new Object[]{id} - таким образом передаются параметры в SQL запрос ?
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM PERSON WHERE id=?", new Object[] {id}, new PersonMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO PERSON(name, age, email, address) VALUES(?, ?, ?, ?)",
                person.getName(),
                person.getAge(),
                person.getEmail(),
                person.getAddress());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE PERSON SET name=?, age=?, email=?, address=? WHERE id=?",
                updatedPerson.getName(),
                updatedPerson.getAge(),
                updatedPerson.getEmail(),
                updatedPerson.getAddress(),
                updatedPerson.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM PERSON WHERE id=?", id);
    }

    //Для теста производительности пакетной вставки
    public void testMultipleUpdate() {
       List<Person> people = create1000People();
       long before = System.currentTimeMillis();

       for(Person person : people) {
           jdbcTemplate.update("INSERT INTO PERSON VALUES(?, ?, ?, ?)", person.getId(), person.getName(),
                   person.getAge(), person.getEmail());
       }
        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO PERSON VALUES(?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    //здесь должны передать все объекты, которые нужно добавить. i - указывает на текущего человека(индекс)
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        Person person = people.get(i);
                        preparedStatement.setInt(1, person.getId());
                        preparedStatement.setString(2, person.getName());
                        preparedStatement.setInt(3, person.getAge());
                        preparedStatement.setString(4, person.getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size(); //благодаря этому метод setValues будет вызываться 1000 раз, передавая i от 0 до people.size
                    }
                }
        );

        long afer = System.currentTimeMillis();

        System.out.println("Time: " + (afer - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name" + i, 30, "test" + i + "gmail.com", "some address"));
        }

        return people;
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM PERSON where email=?",
                new Object[]{email},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

}

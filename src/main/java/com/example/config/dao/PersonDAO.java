package com.example.config.dao;

import com.example.config.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();
        //здесь будет обычный hibernate код
        List<Person> people = session.createQuery("select p from Person p", Person.class).getResultList();
        return people;
    }

    @Transactional
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        updatedPerson.setId(id);
        session.update(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from Person where id=:id").setParameter("id", id).executeUpdate();
    }

    @Transactional
    public Optional<Person> show(String email) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Person p where p.email=:email ").setParameter("email", email).getResultList().stream().findAny();
    }

}

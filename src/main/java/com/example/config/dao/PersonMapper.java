package com.example.config.dao;

import com.example.config.models.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {

    //Это метод, который делает то, что мы делали постоянно при использовании jdbc чистого. Т.е. мы получили в результате SQL запроса
    //строку и потом с помощью ResultSet проходили по этой строке и создавали объект Person. А теперь мы сделали целый класс
    //который будет заниматься этим,т.е. маппить результаты SQL запроса в объект(что-то типо ORM)
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Person(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age"),
                resultSet.getString("email"),
                resultSet.getString("address")
        );
    }
}

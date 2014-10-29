/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.aws.sample.rds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Agim Emruli
 */
@Service
public class JdbcPersonService implements PersonService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcPersonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> all() {
        return this.jdbcTemplate.query("SELECT * FROM Person", new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new Person(resultSet.getString("firstName"), resultSet.getString("lastName"));
            }
        });
    }

    @Override
    @Transactional
    public void store(Person person) {
        this.jdbcTemplate.update("INSERT INTO Person(firstName, lastName) VALUES (?,?)",
                person.getFirstName(), person.getLastName());
    }
}
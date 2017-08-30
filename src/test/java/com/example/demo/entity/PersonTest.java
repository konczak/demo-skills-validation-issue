package com.example.demo.entity;

import com.example.demo.DemoApplication;
import com.example.demo.repository.PersonRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class PersonTest {
    @Autowired
    private PersonRepository personRepository;
    private static final String NAME = "gucio",
            JAVA = "java",
            SQL = "sql";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @Test
    public void shouldSave() {
        //given
        Collection<String> skills = Arrays.asList(JAVA, SQL);

        Person person = new Person(NAME, skills);

        //when
        Person result = personRepository.save(person);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getId())
                .isNotNull();
        assertThat(result.getName())
                .isEqualTo(NAME);
        assertThat(result.getSkills())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(JAVA, SQL);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailWhenNameIsBlank() {
        //given
        Person person = new Person("", Arrays.asList(JAVA));

        //when
        personRepository.save(person);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailWhenSkillsAreEmpty() {
        //given
        Person person = new Person("gucio", Collections.EMPTY_LIST);

        //when
        personRepository.save(person);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailWhenUpdateNameIsBlank() {
        //given
        Person person = new Person(NAME, Arrays.asList(JAVA));
        person = personRepository.save(person);

        person.updateName("");
        //when
        personRepository.save(person);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailWhenUpdateSkillsToEmpty() {
        //given
        Collection<String> skills = Arrays.asList(JAVA, SQL);

        Person person = new Person("gucio", skills);
        person = personRepository.save(person);

        person.updateSkills(Collections.EMPTY_LIST);
        //when
        personRepository.save(person);

        //then
    }

    @Test
    public void should() {
        //given
        Collection<String> skills = Arrays.asList(JAVA, SQL);

        Person person = new Person("gucio", skills);
        person = personRepository.save(person);

        person.updateSkills(Collections.EMPTY_LIST);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        //when
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        //then
        assertThat(constraintViolations)
                .hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("size must be between 1 and 5");
    }

}

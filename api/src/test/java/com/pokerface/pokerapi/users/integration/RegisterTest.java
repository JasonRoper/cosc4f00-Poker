package com.pokerface.pokerapi.users.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokerface.pokerapi.users.RegistrationFields;
import com.pokerface.pokerapi.users.User;
import com.pokerface.pokerapi.users.UserRepository;
import com.pokerface.pokerapi.users.UserTransport;
import com.pokerface.pokerapi.util.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test to see if user registration works correctly
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class RegisterTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void clearUsers() {
        userRepository.deleteAll();
    }

    @Before
    public void setUp() {
        restTemplate = restTemplate.withBasicAuth("admin", "admin");
    }

    @Test
    public void registerSuccessful() {
        RegistrationFields[] testCases = new RegistrationFields[]{
                new RegistrationFields("Jason", "afsjkd;afk", "jkmroper@gmail.com")
        };

        for (RegistrationFields testCase : testCases) {
            ResponseEntity<UserTransport> responseEntity = restTemplate.postForEntity("/users",
                    testCase, UserTransport.class);

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

            UserTransport response = responseEntity.getBody();
            assertEquals(testCase.getUsername(), response.getUsername());
            assertEquals(testCase.getEmail(), response.getEmail());

            User record = userRepository.findOne(response.getId());
            assertNotNull(record);
            assertEquals(testCase.getUsername(), record.getUsername());
            assertEquals(testCase.getEmail(), record.getEmail());
        }
    }

    @Test
    public void fieldValidation() throws IOException {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>("Username is too short",
                        new RegistrationFields("", "asdkfl;aas", "t1@gmail.com"),
                        "size must be between 3 and 20"
                ),
                new TestCase<>("Username is too long",
                        new RegistrationFields("thisisareallylongusername", "asdkfl;aas", "t2@gmail.com"),
                        "size must be between 3 and 20"),
                new TestCase<>("Username does not exist",
                        new RegistrationFields(null, "asdkfl;aas", "t3@gmail.com"),
                        "may not be null"),
                new TestCase<>("Password is too short",
                        new RegistrationFields("password1", "", "t4@gmail.com"),
                        "size must be between 5 and 2147483647"
                ),
                new TestCase<>("Password does not exist",
                        new RegistrationFields("[password2", null, "t5@gmail.com"),
                        "may not be null"),
                new TestCase<>("Email malformed",
                        new RegistrationFields("email1", "validPassword", "asdf"),
                        "not a well-formed email address"),
                new TestCase<>("Email empty",
                        new RegistrationFields("email2", "validPassword", ""),
                        "may not be empty"),
                new TestCase<>("Email does not exist",
                        new RegistrationFields("email3", "validPassword", null),
                        "may not be empty"),
        };
        for (TestCase<RegistrationFields, String> testCase : testCases) {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "/users",
                    testCase.getInput(),
                    String.class);

            Assert.assertEquals(testCase.getMessage() + ": status code incorrect", HttpStatus.BAD_REQUEST, response.getStatusCode());

            JsonNode root = objectMapper.readTree(response.getBody());

            Assert.assertEquals(testCase.getMessage() + ": Validation error didn't occur",
                    "Validation failed for object='registrationFields'. Error count: 1",
                    root.get("message").asText()
            );

            JsonNode errors = root.get("errors");
            Assert.assertEquals(testCase.getMessage() + " : more than one error", 1, errors.size());
            Assert.assertEquals(testCase.getMessage(),
                    testCase.getCorrectResult(), errors.get(0).get("defaultMessage").asText());
        }
    }

    @Test
    public void usernameAlreadyExists() throws IOException {
        User existingRecord = new User("user1", "aHashedPassword", "user1@gmail.com");
        userRepository.save(existingRecord);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/users",
                new RegistrationFields("user1", "somePassword", "uniqueemail@gmail.com"),
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode root = objectMapper.readTree(response.getBody());
        assertEquals("com.pokerface.pokerapi.users.UsernameAlreadyExistsException", root.get("exception").asText());
    }

    @Test
    public void emailAlreadyExists() throws IOException {
        User existingRecord = new User("user1", "aHashedPassword", "user1@gmail.com");
        userRepository.save(existingRecord);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/users",
                new RegistrationFields("randomuser", "somePassword", "user1@gmail.com"),
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode root = objectMapper.readTree(response.getBody());
        assertEquals("com.pokerface.pokerapi.users.EmailAlreadyExistsException", root.get("exception").asText());
    }
}
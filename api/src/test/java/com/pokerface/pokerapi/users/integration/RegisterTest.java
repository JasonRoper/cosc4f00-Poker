package com.pokerface.pokerapi.users.integration;

import com.pokerface.pokerapi.users.RegistrationFields;
import com.pokerface.pokerapi.users.UserTransport;
import com.pokerface.pokerapi.util.BadRequestError;
import com.pokerface.pokerapi.util.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUpDatabase() {

    }


    @Test
    public void registerSuccessful(){
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
        }
    }

    @Test
    public void invalidUsername(){
        TestCase[] testCases = new TestCase[] {
                new TestCase<RegistrationFields, BadRequestError>("invalid user name",
                        new RegistrationFields("a", "asdkfl;aas", "jkmroper@gmail.com"),
                        new BadRequestError("Error"))
        };

        for (TestCase<RegistrationFields,BadRequestError> testCase : testCases) {
            String res = restTemplate.postForObject(
                    "/users",
                    testCase.getInput(),
                    String.class);
            ResponseEntity<BadRequestError> response = restTemplate.postForEntity(
                    "/users",
                    testCase.getInput(),
                    BadRequestError.class);
            Assert.assertEquals(testCase.getMessage(), testCase.getCorrectResult(), response.getBody());
        }
    }

    @Test
    public void invalidPassword(){
        TestCase[] testCases = new TestCase[] {
                new TestCase<RegistrationFields, BadRequestError>("No password",
                        new RegistrationFields("Jason1", "", "jkmroper@gmail.com"),
                        new BadRequestError("Error")),
                new TestCase<RegistrationFields, BadRequestError>(
                        new RegistrationFields("Jason2", "asdkfl;aas", "jkmroper@gmail.com"),
                        new BadRequestError("Error"))
        };
    }

    @Test
    public void invalidEmail(){
        TestCase[] testCases = new TestCase[] {
                new TestCase<RegistrationFields, BadRequestError>(
                        new RegistrationFields("a", "asdkfl;aas", "jkmroper@gmail.com"),
                        new BadRequestError("Error")),
                new TestCase<RegistrationFields, BadRequestError>(
                        new RegistrationFields("a", "asdkfl;aas", "jkmroper@gmail.com"),
                        new BadRequestError("Error"))
        };
    }
}

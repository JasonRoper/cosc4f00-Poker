package com.pokerface.pokerapi.users.users_itegration;

import com.pokerface.pokerapi.users.RegistrationFields;
import com.pokerface.pokerapi.users.UserTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.util.Pair;
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
}

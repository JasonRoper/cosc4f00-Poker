package com.pokerface.pokerapi.users;

import com.pokerface.pokerapi.util.BadRequestError;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;

public class RegisterTest {
    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository mockUserRepository;
    private UserService userService;

    @Before
    public void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.mockUserRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserService(this.mockUserRepository, this.passwordEncoder);
    }

    @Test
    public void testSuccessfulRegistration() throws BadRequestError {
        RegistrationTestCase[] testCases = new RegistrationTestCase[]{
                new RegistrationTestCase(
                        new RegistrationFields("Jason", "afasdkf3ip", "jkmroper@gmail.com")),
                new RegistrationTestCase(
                        new RegistrationFields("Javon", "kdfl;klk3", "jl14ps@brocku.ca"))
        };

        for (RegistrationTestCase testCase : testCases) {
            Mockito.when(mockUserRepository.existsByEmailIgnoreCase(testCase.registrationFields.getUsername()))
                    .thenReturn(false);
            Mockito.when(mockUserRepository.existsByEmailIgnoreCase(testCase.registrationFields.getEmail()))
                    .thenReturn(false);
            Mockito.doAnswer(returnsFirstArg()).when(mockUserRepository).save(any(User.class));

            UserTransport user = userService.register(testCase.registrationFields);
            Assert.assertEquals(user, testCase.result.toTransfer());

        }
    }

    @Test
    public void testInvalidUsername() {

    }

    @Test
    public void testInvalidEmail() {

    }

    @Test
    public void testInvalidPassword() {

    }

    @Test
    public void testEmailAlreadyExists() {

    }

    @Test
    public void testUsernameAlreadyExists() {

    }

    @Test
    public void testIncompleteForm() {

    }

    class RegistrationTestCase {
        RegistrationFields registrationFields;
        User result;

        public RegistrationTestCase(RegistrationFields registrationFields){
            this.registrationFields = registrationFields;
            this.result = new User(registrationFields.getUsername(),
                    passwordEncoder.encode(registrationFields.getPassword()),
                    registrationFields.getEmail());
        }
    }
}

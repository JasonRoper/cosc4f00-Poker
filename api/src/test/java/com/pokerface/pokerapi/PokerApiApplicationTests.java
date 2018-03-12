package com.pokerface.pokerapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * {@link PokerApiApplicationTests} preforms generic sanity checks.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PokerApiApplicationTests {

	@Test
	public void contextLoads() {
	}


    @Test
    public void worldStillWorks() {
        assertEquals(2, 1 + 1);
    }
}

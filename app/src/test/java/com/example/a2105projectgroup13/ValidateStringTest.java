package com.example.a2105projectgroup13;

import org.junit.Test;

import static com.example.a2105projectgroup13.ValidateString.validateEmail;
import static java.lang.System.out;
import static org.junit.Assert.*;

/**
 * Tests methods from the ValidateString class.
 */
public class ValidateStringTest {

    @Test
    public void validateName() { // first and last name are both validated using the same function validateName()
        // capital first letter
        assertEquals("Bob", ValidateString.validateName("Bob"));
        // all lowercase
        assertEquals("Bob", ValidateString.validateName("bob"));
        // all uppercase
        assertEquals("Bob", ValidateString.validateName("BOB"));

        // contains numbers
        assertEquals("-1", ValidateString.validateName("123"));
        assertEquals("-1", ValidateString.validateName("Bob123"));
        assertEquals("-1", ValidateString.validateName("123BOB"));
    }

    @Test
    public void validateUsername() {
        // deprecated (we will not be using this function), so no tests are required

    }

    @Test
    public void validateEmail() {

        // test valid emails
        assertEquals("bob@gmail.com", ValidateString.validateEmail("bob@gmail.com"));
        assertEquals("sponge@bob.edu", ValidateString.validateEmail("sponge@bob.edu"));
        assertEquals("emilyEthier@hotmail.ca", ValidateString.validateEmail("emilyEthier@hotmail.ca"));
        assertEquals("hotRod@hotRodGuitars.co", ValidateString.validateEmail("hotRod@hotRodGuitars.co"));

        // no @ or .[whatever]
        assertEquals("-1", ValidateString.validateEmail("bob"));
        assertEquals("-1", ValidateString.validateEmail("jordan"));
        assertEquals("-1", ValidateString.validateEmail("bobgmailcom"));
        assertEquals("-1", ValidateString.validateEmail("randomstringhere"));

        //uppercase, no @ or .[whatever]
        assertEquals("-1", ValidateString.validateEmail("BOB"));
        assertEquals("-1", ValidateString.validateEmail("123"));
        assertEquals("-1", ValidateString.validateEmail("456abc789"));
        assertEquals("-1", ValidateString.validateEmail("what"));

        // no text before @
        assertEquals("-1", ValidateString.validateEmail("@gmail.com"));
        assertEquals("-1", ValidateString.validateEmail("@gmail.ca"));
        assertEquals("-1", ValidateString.validateEmail("@hotmail.edu"));
        assertEquals("-1", ValidateString.validateEmail("@L.ron"));

        // no .[whatever]
        assertEquals("-1", ValidateString.validateEmail("bob@gmail"));
        assertEquals("-1", ValidateString.validateEmail("sponge@bob"));
        assertEquals("-1", ValidateString.validateEmail("emilyEthier@hotmail"));
        assertEquals("-1", ValidateString.validateEmail("hotRod@hotRodGuitars"));

        //no @

        assertEquals("-1", ValidateString.validateEmail("bob.com"));
        assertEquals("-1", ValidateString.validateEmail("sponge.bob"));
        assertEquals("-1", ValidateString.validateEmail("emilyEthier.hotmail"));
        assertEquals("-1", ValidateString.validateEmail("hotRod.Guitars"));
    }

    @Test
    public void validatePassword() {
        // a "valid password" must contain 1+ capitals, 1+ numbers, and be 8+ characters long
        // special characters are not allowed
        // TODO: after ValidateString is updated to allow special characters in passwords, add more tests

        // "good" passwords
        assertEquals("Password123", ValidateString.validatePassword("Password123"));
        assertEquals("08Digits", ValidateString.validatePassword("08Digits"));
        assertEquals("123Password", ValidateString.validatePassword("123Password"));
        assertEquals("Pass1word", ValidateString.validatePassword("Pass1word"));

        // too short
        assertEquals("-1", ValidateString.validatePassword("5Long"));
        assertEquals("-1", ValidateString.validatePassword("7Digits"));
        assertEquals("-1", ValidateString.validatePassword("03L"));
        assertEquals("-1", ValidateString.validatePassword("L03"));

        // contains special characters
        assertEquals("-1", ValidateString.validatePassword("!!Password123"));
        assertEquals("-1", ValidateString.validatePassword("08??Digits"));
        assertEquals("-1", ValidateString.validatePassword("123Pa{ssword"));
        assertEquals("-1", ValidateString.validatePassword("Pas$s$wo$rd!"));

        // no uppercase
        assertEquals("-1", ValidateString.validatePassword("password123"));
        assertEquals("-1", ValidateString.validatePassword("08digits"));
        assertEquals("-1", ValidateString.validatePassword("123password"));
        assertEquals("-1", ValidateString.validatePassword("pass1word"));

        // no digits
        assertEquals("-1", ValidateString.validatePassword("Passwordooo"));
        assertEquals("-1", ValidateString.validatePassword("ooDigits"));
        assertEquals("-1", ValidateString.validatePassword("oooPassword"));
        assertEquals("-1", ValidateString.validatePassword("Passoword"));

    }
}
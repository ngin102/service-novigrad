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

    @Test
    public void validateServiceName() {
//        assertEquals("Apply For A Loan", ValidateString.validateServiceName("apply for a loan"));
//        assertEquals("Apply For A Loan", ValidateString.validateServiceName("APPly fOr a loan"));
//        assertEquals("Apply For A Loan", ValidateString.validateServiceName("apply for a loan "));
//        assertEquals("Apply F0r A L0an", ValidateString.validateServiceName("apply f0r a L0an "));
//
//        assertEquals("-1", ValidateString.validateServiceName("apply for 1 loan"));
//        assertEquals("-1", ValidateString.validateServiceName("apply for a  loan"));
//        assertEquals("-1", ValidateString.validateServiceName(" apply for 1 loan"));
//
//        //special characters
//        assertEquals("Driver's License", ValidateString.validateServiceName("Driver's License"));
//        assertEquals("Mm/dd/yy", ValidateString.validateServiceName("MM/DD/YY"));
//        assertEquals("What Is Your Driver's License?", ValidateString.validateServiceName("what is your driver's license?"));
        assertEquals("Driver's License (MM/DD/YY):", ValidateString.validateServiceName("Driver's License (MM/DD/YY):"));


    }

    @Test
    public void validatePrice() {
        // incorrect formats
        assertEquals("-1", ValidateString.validatePrice("01"));
        assertEquals("-1", ValidateString.validatePrice("01.0"));
        assertEquals("-1", ValidateString.validatePrice("1.0"));
        assertEquals("-1", ValidateString.validatePrice("borb"));

        // 0.00 form
        assertEquals("0.00", ValidateString.validatePrice("0"));
        assertEquals("0.00", ValidateString.validatePrice("0.00"));

        //[num] form
        assertEquals("1.00", ValidateString.validatePrice("1"));
        assertEquals("2.00", ValidateString.validatePrice("2"));
        assertEquals("3.00", ValidateString.validatePrice("3"));

        //[num].[num][num] form
        assertEquals("1.00", ValidateString.validatePrice("1.00"));
        assertEquals("2.00", ValidateString.validatePrice("2.00"));
        assertEquals("3.00", ValidateString.validatePrice("3.00"));
    }

    @Test
    public void validateAddressOrCity() {
        // incorrect formats
        assertEquals("-1", ValidateString.validateAddressOrCity("1"));

        // allowable formats
        assertEquals("Novigrad", ValidateString.validateAddressOrCity("NOVIGRAD"));
        assertEquals("Novigrad", ValidateString.validateAddressOrCity("novigrad"));
        assertEquals("Novigrad", ValidateString.validateAddressOrCity("Novigrad"));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("OTTawa-Vanier"));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("OTTawa -Vanier"));
        assertEquals("Novigrad", ValidateString.validateAddressOrCity(" NoVIgrad"));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("OTTawa  -Vanier"));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("OTTawa  -    Vanier"));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("OTTawa  -    Vanier  "));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("   OTTawa       -    Vanier  "));
        assertEquals("Avocado-Novigrad-Taco", ValidateString.validateAddressOrCity("  Avocado  -      NoVIgrad    -   Taco"));
        assertEquals("Novigrad-City City", ValidateString.validateAddressOrCity("NOVIGrad  -       City   City"));
        assertEquals("Novigrad City", ValidateString.validateAddressOrCity("Novigrad city" ));
        assertEquals("Novigrad--City",  ValidateString.validateAddressOrCity("Novigrad  -  -  city" ));
        assertEquals("Novigrad-City City-", ValidateString.validateAddressOrCity("   Novigrad  -  city     City    -" ));
        assertEquals("Ottawa-Vanier", ValidateString.validateAddressOrCity("OTTawa -       VaniEr    "));
        assertEquals("Avocado-Novigrad-Taco City", ValidateString.validateAddressOrCity("  AvOcado  -      NoVIgrad    -   Taco       City"));
        assertEquals("Avocado-Novigrad-Taco-Chicken City", ValidateString.validateAddressOrCity("  AvOcado  -      NoVIgrad    -   Taco      -     cHICKEN      CitY"));
        assertEquals("Macho--Juice-Cool", ValidateString.validateAddressOrCity(" Macho  -- Juice - Cool"));
        assertEquals("Macho---Juice-Cool", ValidateString.validateAddressOrCity(" Macho  -- -  Juice - Cool"));
    }

    @Test
    public void validatePostalCode() {
        // incorrect formats
        assertEquals("-1", ValidateString.validatePostalCode("1"));
        assertEquals("-1", ValidateString.validatePostalCode("H0H0HH"));
        assertEquals("-1", ValidateString.validatePostalCode("ABCDEFGHIJK"));
        assertEquals("-1", ValidateString.validatePostalCode("H0H0H0H0H0H0"));
        assertEquals("-1", ValidateString.validatePostalCode("H000H0"));

        // allowable formats
        assertEquals("H0H0H0", ValidateString.validatePostalCode("h0h0h0"));
        assertEquals("H0H0H0", ValidateString.validatePostalCode("H0H0H0"));
        assertEquals("H0H0H0", ValidateString.validatePostalCode("h0H0H0"));
    }

    @Test
    public void validateTime() {
        // incorrect formats
        assertEquals("-1", ValidateString.validateTime("12 am"));
        assertEquals("-1", ValidateString.validateTime("1212"));
        assertEquals("-1", ValidateString.validateTime("40:40"));
        assertEquals("-1", ValidateString.validateTime("ab:cd"));
        assertEquals("-1", ValidateString.validateTime("01:90"));

        // allowable formats
        assertEquals("01:00", ValidateString.validateTime("01:00"));
        assertEquals("12:30", ValidateString.validateTime("12:30"));
        assertEquals("04:00", ValidateString.validateTime("4:00"));
    }


    //validate address of city, postal code, time
}
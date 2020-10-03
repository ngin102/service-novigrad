package com.example.a2105projectgroup13;

public class ValidateStringTest {
    public static void main(String[] args){
        //ValidateName only works when names are inputted in all lowercase.
        System.out.println("Testing validateName:");
        System.out.println(ValidateString.validateName("bob")); //This works.
        System.out.println(ValidateString.validateName("BOB")); //This works.
        System.out.println(ValidateString.validateName("Bob")); //This works.
        System.out.println("\n");

        //ValidateEmail seems to work!
        System.out.println("Testing validateEmail:");
        System.out.println(ValidateString.validateEmail("bob@gmail.com"));
        System.out.println(ValidateString.validateEmail("BOB"));    //Good! This was said to be wrong form.
        System.out.println(ValidateString.validateEmail("BOB@GMAIL.COM"));
        System.out.println(ValidateString.validateEmail("@gMAIL.COM"));
        System.out.println("\n");

        //validatePassword seems to work properly, except for when you use a special character like "!" or "@" in your password.
        System.out.println("Testing validatePassword:");
        System.out.println(ValidateString.validatePassword("bob")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("Bob")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("bob1")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("1234")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("Bob1")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("12345678")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("bob12345")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("bobbbbbbbbbb")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("BOBBOBOBOBOBOBOBOBBOBOOB")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("bBOBBOBOBOBOBOBO1343285")); //Good! This was said to be right form.
        System.out.println(ValidateString.validatePassword("bob!")); //Good! This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("bob1234!")); //This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("bob12345@")); //This was said to be wrong form.
        System.out.println(ValidateString.validatePassword("Bob12345")); //Good! This was said to be right form.

    }
}

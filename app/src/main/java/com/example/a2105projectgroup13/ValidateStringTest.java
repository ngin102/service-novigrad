package com.example.a2105projectgroup13;

import static com.example.a2105projectgroup13.ValidateString.*;
import static java.lang.System.*;

// TODO: convert into JUnit test
public class ValidateStringTest {
    public static void main(String[] args){
        //ValidateName only works when names are inputted in all lowercase.
        out.println("Testing validateName:");
        out.println(validateName("bob")); //This works.
        out.println(validateName("BOB")); //This works.
        out.println(validateName("Bob")); //This works.
        out.println("\n");

        //ValidateEmail seems to work!
        out.println("Testing validateEmail:");
        out.println(validateEmail("bob@gmail.com"));
        out.println(validateEmail("BOB"));    //Good! This was said to be wrong form.
        out.println(validateEmail("BOB@GMAIL.COM"));
        out.println(validateEmail("@gMAIL.COM"));
        out.println("\n");

        //validatePassword seems to work properly, except for when you use a special character like "!" or "@" in your password.
        out.println("Testing validatePassword:");
        out.println(validatePassword("bob")); //Good! This was said to be wrong form.
        out.println(validatePassword("Bob")); //Good! This was said to be wrong form.
        out.println(validatePassword("bob1")); //Good! This was said to be wrong form.
        out.println(validatePassword("1234")); //Good! This was said to be wrong form.
        out.println(validatePassword("Bob1")); //Good! This was said to be wrong form.
        out.println(validatePassword("12345678")); //Good! This was said to be wrong form.
        out.println(validatePassword("bob12345")); //Good! This was said to be wrong form.
        out.println(validatePassword("bobbbbbbbbbb")); //Good! This was said to be wrong form.
        out.println(validatePassword("BOBBOBOBOBOBOBOBOBBOBOOB")); //Good! This was said to be wrong form.
        out.println(validatePassword("bBOBBOBOBOBOBOBO1343285")); //Good! This was said to be right form.
        out.println(validatePassword("bob!")); //Good! This was said to be wrong form.
        out.println(validatePassword("bob1234!")); //This was said to be wrong form.
        out.println(validatePassword("bob12345@")); //This was said to be wrong form.
        out.println(validatePassword("Bob12345")); //Good! This was said to be right form.

    }
}

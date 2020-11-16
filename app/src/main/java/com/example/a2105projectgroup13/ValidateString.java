package com.example.a2105projectgroup13;

import java.util.regex.Pattern; // use the Pattern library to utilize regular expressions ("regex")

/**
 This class is a helper class providing methods for front-end form validation. These methods (excluding the private
 method validateField return String "-1" by convention when a textField has an invalid input, or a validated String
 which may be used by the calling method if the textField follows the rules for formatting.

 Possible FAQ:
 !IMPORTANT Q1: So if a method in this class returns anything other than "-1", it's safe to store in the server?
 A1: NO! It only means that the input is in the proper format to *try* storing it. If a field contains
 information which should be saved on the server only once -- such as a username -- then it will still pass these tests.
 Validation that the

 Q2: Why return a string instead of returning True/False?
 A2: Returning a string allows us to sanitize inputs (i.e.: make it harder to inject malicious code through the textfield),
 and also for us to ensure that inputs which are *technically* correct (e.g. firstName "JoHNaTHan") are stored in a proper
 format (i.e. "Johnathan"). validateFirstName("JoHNaTHAn") will return String "Johnathan".

 Q3: You just said methods return a String! Why does validateField() return a boolean?
 A3: validateField() validates regex, but:
 1. It isn't meant to be called outside of this class.
 2. The meaning of the result of that validation is interpretable. Did you pass the right expression? Does failing validation mean
 that the expression is

 Q4: Why return *anything*? You could just change the original String!
 A4: In Java, Strings are immutable objects. Therefore this is impossible. However, even if it was possible, changing the original String
 could cause unexpected behaviour on the client's side such as changing what they have typed in a textField to "-1" if their input is invalid.
 Now that might get the point across that they messed up, but it's a little aggressive!

 Q5: Why is validateString() private? I need a validation method that isn't defined here.
 A5: Forcing you to make your own new validation method which calls validateField() as a helper methd makes code more maintainable
 for three reasons:
 1. Calls to validateString() are incomprehensible unless you comment your code, or name your regex appropriately and pass that
 to validateString() instead. Yes, you *could* make a comment when you use it outside of this class, but it still wont be as
 readable as calling a function with a name that has an immediate meaning.
 2. Per the reasoning of A3: validateString() returns True/False, which is interpretable based on what you pass it.
 3. Per the reasoning of A1: Methods in this class are exclusively meant to return either "-1", signifying that a String could not be validated.

 Q6: How should I implement a new validation method?
 A6: In general, follow these steps:
 1. Declare a method with the form "public static string [validate_____]() {...}
 2. Declare as many variables containing your regex as you need; e.g. String [YOUR_REGEX] = "[a-z]{1,10}"
 3. Make a call to validateField() such as validateField(YOUR_REGEX, [String to be validated]) and interpret the result.
 4. Return String "-1" if the validation is unsuccessful, or a String containing the validated input (i.e. it is sanitized and
 of the right format).

 Q7: Why don't we sanitize our inputs?
 A7: We use Firebase; SQL injections can't be used because ... well, Firebase isn't SQL. Security to prevent code injection
 into Firebase needs to be prevented using Firebase Authentication instead of sanitized inputs.

 @author Jared Wagner

 TODO: update validateName() to validate hyphenated names and accented names (next Deliverable).
 */
public class ValidateString {

    // private class methods
    /*
     Validates a given String word against a given String regular expression.
     Returns True if the word is valid (per the regex), or False if it is invalid.

     !Important: this method is only as good as the regex you pass it and how you
     interpret the result of the validation. See Q1/A1.
     */
    private static boolean validateField(String regularExpression, String word) {
        boolean patternPassesValidation = Pattern.matches(regularExpression, word); // checks String word against the regex regularExpression
        return patternPassesValidation;
    }

    // public class methods (intended for convenience and clarity)

    /**
    Validates a name.
    Returns "-1" if the name cannot be validated.
    If the name can be validated (i.e. it is composed of 2-20 alphabetical characters), then
    return the validated form (Uppercase first letter, lowercase for all other letters).

    Note: hyphenated names like Robinson-Ethier and Jean-Luc would fail validation.
    Note: accented names like Bélanger and Amélie would fail validation.
    */
    public static String validateName(String name) {
        String nameRegex = "^[a-zA-Z]{2,20}$"; // regex to check that a name has alphabetical characters and correct length (2-20 chars)
        if (!validateField(nameRegex, name)) { // check if the user has given us an invalid name
            return "-1"; // notify the caller that this is an invalid input
        } else { // two possibilities remain: that the user has given us a valid name like "Xing", or a technically valid name like "XING"
            // regex has the possibility to be computationally expensive, so it's less expensive in aggregate to assume the user has used bad formatting
            // and return a new string with the appropriate formatting
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }

    /**
    Validates a username.
    Returns "-1" if the username cannot be validated.
    If the name can be validated (i.e. it is composed of 2-22 word characters: [a-zA-Z_0-9), then
    return the original username.
    */
    public static String validateUsername(String username) {
        String usernameCharactersLength = "?[\\w]{4,22}$"; // regex to check that a username only has word characters [a-zA-Z_0-9], and is correct length (4-22 chars)
        if (!validateField(usernameCharactersLength, username)) { // check if the user has given us an invalid username
            return "-1"; // notify the caller that this is an invalid input
        } else { // if validateField() returns True, then it's safe to use the given username
            return username;
        }
    }

    /**
    Validates an email.
    Returns "-1" if the username cannot be validated.
    If the email can be validated, then assume the email is real and return the email.
    */
    public static String validateEmail(String email) {
        String emailFormat = "^[\\w]+[@][\\w]+[.][\\w]+$"; // regex to check that an email is the proper format
        if (!validateField(emailFormat, email)) { // check if the user has given us a valid email format
            return "-1"; // notify the caller that this is an invalid input
        } else { // if validateField() returns True, then we assume the email is real
            return email;
        }
    }

    /**
    Validates a password.
    Returns "-1" if the password cannot be validated.
    If the password can be validated, then the password is returned.
    Passwords must contain 1+ numbers, 1+ uppercase letters, and 8+ characters total.
    Passwords must be alphanumeric (this will change in future Deliverables).
    */
    public static String validatePassword(String password) {
        String passwordFormat = "^(?=.*[A-Z])(?=.*\\d)[\\w]{8,}$"; // regex to check that the password is the proper format: 1+ nums, 1+ uppercase, 8+ characters
        if (!validateField(passwordFormat, password)) { // check if the password is invalid
            return "-1"; // notify the caller that this is an invalid input
        } else { // if validateField() returns True, then we assume the password has a proper format
            return password;
        }
    }

    /**
     Validates a service name.
     Any service name is valid, as this is the admin.
     */
    public static String validateServiceName(String serviceName) {

//        String serviceNameFormat = "^([a-zA-Z]+[']?[ a-zA-Z0-9_@.#&+-?/]?)*$"; // regex to check that the service name is a valid format (i.e. alphanumeric, special characters, spaces -- EXCEPT the first character of any word must be a letter)
//        String[] splitSentence;
//        String formattedName;
//        if (!validateField(serviceNameFormat, serviceName)) { // check if the service name is invalid
//            return "-1"; // notify the caller that this is an invalid input
//        } else { // serviceName is in a "valid" layout
//            splitSentence = serviceName.split(" ");
//            formattedName = "";
//
//            for(String word : splitSentence) {
//                if (word.length()>0) {
//                    formattedName = formattedName + word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
//                }
//            }
//
//            return (formattedName.substring(0, formattedName.length()-1));
//        }

        return serviceName;
    }

    /**
     Validates a price.
     Prices are valid if they are of the forms:
     1) 0.00
     2) [num] (e.g. 3)
     3) [num].[num][num] (e.g. 3.12)
     Returns "-1" if the price cannot be validated.
     Returns the second form if the num is valid.
     */
    public static String validatePrice(String price) {
        String priceFormat = "^(([1-9]+[0-9]*)|[0])([.][0-9]{2})?$";
        if (!validateField(priceFormat, price)) { // check if the password is invalid
            return "-1"; // notify the caller that this is an invalid input
        } else { // if validateField() returns True, then the price is in one of our acceptable formats
            if (price.contains(".")) { // first or third form
                return price;
            } else { // second form, so just add a decimal
                return price + ".00";
            }
        }
    }

    /**
     Validates an address.
     Returns "-1" if the address is an invalid format.
     Returns the given string if the format is allowed.

     An address is valid if it contains only letters, spaces, and hyphens.
     */
    public static String validateAddressOrCity(String addressOrCity){
        String streetAddressFormat = "^[a-zA-Z\\-\\ ]+$";
        if (!validateField(streetAddressFormat, addressOrCity)){
            return "-1";
        } else {
            return addressOrCity;
        }
    }

    /**
     Validates a postal code, returning "-1" if the string is in an invalid format,
     or returning the postal code (with all capitals) if the format is correct.
     */
    public static String validatePostalCode(String postalCode){
        String postalCodeFormat = "^([a-zA-Z][0-9]){3}$";
        if (! validateField(postalCodeFormat, postalCode)){
            return "-1";
        } else {
            return postalCode.substring(0,1).toUpperCase() + postalCode.substring(1,2) + postalCode.substring(2,3).toUpperCase() + postalCode.substring(3,4) + postalCode.substring(4,5).toUpperCase() + postalCode.substring(5);
        }
    }
}

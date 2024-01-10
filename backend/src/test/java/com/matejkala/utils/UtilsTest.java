package com.matejkala.utils;

import com.matejkala.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class UtilsTest {
    @Test
    void Generate_Activation_Code_Is_Successful_When_Input_48() {
        String code = Utils.GenerateActivationCode(48);
        assert code != null;
        assert code.length() == 48;
    }

    @Test
    void Generate_Activation_Code_Is_Successful_When_Input_999999() {
        String code = Utils.GenerateActivationCode(999999);
        assert code != null;
        assert code.length() == 999999;
    }

    @Test
    void Generate_Activation_Code_Is_Successful_When_Input_0() {
        String code = Utils.GenerateActivationCode(0);
        assert code != null;
        assert code.isEmpty();
    }

    @Test
    void Generate_Activation_Code_Is_Successful_When_Input_Minus999() {
        String code = Utils.GenerateActivationCode(-999);
        assert code != null;
        assert code.isEmpty();
    }

    @Test
    void Generate_Activation_Code_Is_Successful_When_Input_Null() {
        String code = Utils.GenerateActivationCode(null);
        assert code != null;
        assert code.isEmpty();
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Successful_When_Input_Valid_Password() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid("Password1.");
        assert passwordValid != null;
        assert passwordValid.equals(true);
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Successful_When_Input_Valid_Random_1000_Times() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            String lower = random.ints('a', 'z' + 1)
                    .limit(random.nextInt(3, 4))
                    .mapToObj(x -> String.valueOf((char) x))
                    .collect(Collectors.joining());
            String upper = random.ints('A', 'Z' + 1)
                    .limit(random.nextInt(3, 4))
                    .mapToObj(x -> String.valueOf((char) x))
                    .collect(Collectors.joining());
            String numeric = random.ints(0, 10)
                    .limit(random.nextInt(1, 4))
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining());
            String specials = "!\"#$%&'()*+,-.:;<=>?@[]\\^_`{|}~";
            String special = random.ints(0, specials.length())
                    .limit(random.nextInt(1, 4))
                    .mapToObj(x -> String.valueOf(specials.charAt(x)))
                    .collect(Collectors.joining());
            List<String> chars= Arrays.stream((lower + upper + numeric + special).split("")).collect(Collectors.toList());
            Collections.shuffle(chars);
            Boolean passwordValid = Utils.IsUserPasswordFormatValid(String.join("", chars));
            assert passwordValid != null;
            assert passwordValid.equals(true);
        }
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Unsuccessful_When_Input_Without_Uppercase_Character() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid("password1.");
        assert passwordValid != null;
        assert passwordValid.equals(false);
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Unsuccessful_When_Input_Without_Lowercase_Character() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid("PASSWORD1.");
        assert passwordValid != null;
        assert passwordValid.equals(false);
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Unsuccessful_When_Input_Without_Numeric_Character() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid("Password.");
        assert passwordValid != null;
        assert passwordValid.equals(false);
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Unsuccessful_When_Input_Without_Special_Character() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid("Password1");
        assert passwordValid != null;
        assert passwordValid.equals(false);
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Unsuccessful_When_Input_Empty() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid("");
        assert passwordValid != null;
        assert passwordValid.equals(false);
    }

    @Test
    void Is_User_Password_Format_Valid_Is_Unsuccessful_When_Input_Null() {
        Boolean passwordValid = Utils.IsUserPasswordFormatValid(null);
        assert passwordValid != null;
        assert passwordValid.equals(false);
    }

    @Test
    void Has_Admin_Role_Is_Successful_When_User_Admin() {
        final List<Role> list = new ArrayList<>();
        list.add(new Role("ADMIN"));
        Authentication authentication=new UsernamePasswordAuthenticationToken(null, null, list);
        Boolean result=Utils.hasAdminRole(authentication);

        assert result != null;
        assert result.equals(true);
    }

    @Test
    void Has_Admin_Role_Is_Unsuccessful_When_User_Not_Admin() {
        final List<Role> list = new ArrayList<>();
        list.add(new Role("USER"));
        Authentication authentication=new UsernamePasswordAuthenticationToken(null, null, list);
        Boolean result=Utils.hasAdminRole(authentication);

        assert result != null;
        assert result.equals(false);
    }

    @Test
    void Has_Admin_Role_Is_Unsuccessful_When_User_Has_No_Roles() {
        final List<Role> list = new ArrayList<>();
        Authentication authentication=new UsernamePasswordAuthenticationToken(null, null, list);
        Boolean result=Utils.hasAdminRole(authentication);

        assert result != null;
        assert result.equals(false);
    }

    @Test
    void Is_Not_Null_Or_Empty_Is_Successful_When_Input_Not_Null_Or_Empty() {
        Boolean object = Utils.isNotNullOrEmpty("test");
        assert object != null;
        assert object.equals(true);
    }
    @Test
    void Is_Not_Null_Or_Empty_Is_Unsuccessful_When_Input_Null() {
        Boolean object = Utils.isNotNullOrEmpty(null);
        assert object != null;
        assert object.equals(false);
    }
    @Test
    void Is_Not_Null_Or_Empty_Is_Unsuccessful_When_Input_Empty() {
        Boolean object = Utils.isNotNullOrEmpty("");
        assert object != null;
        assert object.equals(false);
    }
}
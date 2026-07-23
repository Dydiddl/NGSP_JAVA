package validator;

public class PersonValidator {

    private PersonValidator() {

    }

    public static String validateName(String name){
        // 검사사항(name):
        // null인지?, 글자수가 2~5글자인지, 숫자가 포함되어 있는지?, 특수문자가 포함되어 있는지?
            return name;
    }
    public static String validatePhone(String phone){
        return phone;
    }
    public static String validateGenderId(String genderId){
        return genderId;
    }
    public static String validateAddress(String address){
        return address;
    }



}

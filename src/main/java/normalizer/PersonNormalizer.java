package normalizer;

public final class PersonNormalizer {
    // Person 표준화
    // 표준화 해야 하는 부분
    // name --> given_name(이름): 2~5글자, family_name(성): 1~2글자
    // phone --> "010"-"1234"-"1234" : phone_front, phone_middle, phone_back
    //          phone_front == "010"
    //          len(phone_middle) = 4
    //          lne(phone_back) = 4
    // gender --> gender_id == 1 --> "남자" or "Male"
    //        --> gender_id == 2 --> "여자" or "Female"
    // bank --> input("농협") -> bank_id table에서 검색해서 매칭되는 값 출력 -> ex) 1 -> 1으로 저장
    //                 └--> "농협", "NH농협", "농협은행" 등 으로 입력될 수 있음, 이것을 어떻게 일반화 할 것인가 ?
    //                        방법 1, bank table에 여러 농협 은행의 이름을 저장 해 놓는다 ?
    // account_number --> "1234-12345-1234", "123415-1234-12341516" 등 숫자의 자리수가 은행마다 다름,
    //                     └--> 은행에서 검색해서 찾아놓는다?? -> 너무 번거로움, 그냥 텍스트로 들어오는 대로 저장
    //                      └--> 검증은 통장 사본이랑 비교해서 잘못 기입되었는지 확인, 또한 이체 실행 할때 잘못된 번호면 송금 불가
    //                        └--> 따라서, 우선 텍스트 그자체로 저장 하는 방향
    // address --> 시/도 + 시/군/구 + 읍/면 + 도로명 + 건물번호 + 상세주소(동/층/호) + (참고항목-> 법정동, 공동주택 명칭 등)
    //           address_do, address_si, address_dong, address_ 등 명칭 선택 필요
    private PersonNormalizer() {
    }

    public static String normalizeName(String name) {
        // 어디서든 접근 가능한 static -> 클래스 자체에 속해서 객체를 만들 필요가 없음 normalizeName() 가능, 문자열 리턴,
        // 입력은 name에 해당하는 입력값을 받음
        // 함수만 모아놓은 클래스

        if (name == null) {
            return null;
        }
        return name.trim()
                .replaceAll("\\s+", "");
    }

    public static String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        return phone.replaceAll("[^0-9]", "");
    }

    public static String normalizeAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }
        return accountNumber.replaceAll("[^0-9]", "");
    }

    public static String normalizeAddress(String address) {
        if (address == null) {
            return null;
        }
        return address.trim()
                .replaceAll("\\s+", " ");
    }
}
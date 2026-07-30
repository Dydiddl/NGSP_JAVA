package converter;

import model.Bank;

import java.util.Arrays;

public class BankConverter {
    private BankConverter(){}

    public static Bank from(String input){
        String normalizedInput = normalize(input);

        return Arrays.stream(Bank.values())
            .filter(bank -> bank.getAliases().stream()
                .map(BankConverter::normalize)
                .anyMatch(normalizedInput::equals))
            .findFirst()
            .orElseThrow(()->
                    new IllegalArgumentException(
                                "지원하지 않는 은행입니다: "
                                + input
                                )
                    );
        }
}

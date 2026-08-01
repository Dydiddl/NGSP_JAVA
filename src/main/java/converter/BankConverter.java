package converter;

import model.Bank;
import normalizer.PersonNormalizer;

import java.util.Arrays;

public class BankConverter {
    private BankConverter(){}

    public static Bank from(String input){
        String normalizedInput = PersonNormalizer.normalizeBank(input);

        return Arrays.stream(Bank.values())
        .filter(bank -> bank.getAliases().stream()
            .map(PersonNormalizer::normalizeBank)
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

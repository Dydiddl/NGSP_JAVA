package converter;

import model.Bank;
import normalizer.TextNormalizer;

import java.util.Arrays;

import static java.util.stream.StreamSupport.stream;

public class BankConverter {

    private BankConverter() {
    }

    public static Bank toBank(String input) {
        String normalizedInput = 
        TextNormalizer.normalizeKeyword(input);

        return Arrays.stream(Bank.values())
        .filter(bank -> matches(bank, normalizedInput))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(
                "지원하지 않는 은행입니다: " + input
            )
        );

    }

    private static boolean matches(
        Bank bank, 
        String normalizedInput
    ) {
        return bank.getAliases().stream()
        .map(TextNormalizer::normalizeKeyword)
        .anyMatch(normalizedInput::equalsIgnoreCase);
    }
}

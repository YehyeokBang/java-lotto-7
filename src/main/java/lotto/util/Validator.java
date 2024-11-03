package lotto.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lotto.rule.LottoRule;
import lotto.util.error.ErrorMessage;
import lotto.util.error.LottoErrorMessage;

public class Validator {

    private Validator() {
    }

    public static void checkLottoNumbersCount(List<Integer> numbers) {
        validateCount(numbers, LottoErrorMessage.LOTTO_NUMBERS_COUNT);
    }

    public static void checkWinningNumbersCount(List<Integer> numbers) {
        validateCount(numbers, LottoErrorMessage.WINNING_NUMBERS_COUNT);
    }

    private static void validateCount(List<Integer> numbers, ErrorMessage message) {
        if (numbers.size() != LottoRule.LOTTO_NUMBERS_COUNT) {
            throw new IllegalArgumentException(message.get());
        }
    }

    public static void checkLottoNumbersDuplicate(List<Integer> numbers) {
        validateDuplicate(numbers, LottoErrorMessage.LOTTO_NUMBERS_DUPLICATE);
    }

    public static void checkWinningNumbersDuplicate(List<Integer> numbers) {
        validateDuplicate(numbers, LottoErrorMessage.WINNING_NUMBERS_DUPLICATE);
    }

    private static void validateDuplicate(List<Integer> numbers, ErrorMessage message) {
        Set<Integer> uniqueNumbers = new HashSet<>(numbers);
        if (uniqueNumbers.size() != numbers.size()) {
            throw new IllegalArgumentException(message.get());
        }
    }

    public static void checkLottoNumbersRange(List<Integer> numbers) {
        validateRange(numbers, LottoErrorMessage.LOTTO_NUMBER_OUT_OF_RANGE);
    }

    public static void checkWinningNumbersRange(List<Integer> numbers) {
        validateRange(numbers, LottoErrorMessage.WINNING_NUMBERS_OUT_OF_RANGE);
    }

    private static void validateRange(List<Integer> numbers, ErrorMessage message) {
        long validNumbersCount = numbers.stream()
                .filter(Validator::isValidRangeInLotto)
                .count();
        if (validNumbersCount != LottoRule.LOTTO_NUMBERS_COUNT) {
            throw new IllegalArgumentException(message.get());
        }
    }

    private static boolean isValidRangeInLotto(Integer number) {
        return LottoRule.MIN_LOTTO_NUMBER <= number
                && number <= LottoRule.MAX_LOTTO_NUMBER;
    }

    public static void checkPurchaseQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(LottoErrorMessage.MIN_QUANTITY_LOTTO_ISSUE.get());
        }
    }

    public static void checkAboveBaseAmount(int purchaseAmount) {
        if (purchaseAmount < LottoRule.PURCHASE_AMOUNT_UNIT) {
            throw new IllegalArgumentException(LottoErrorMessage.PURCHASE_AMOUNT_UNDER_BASE_LIMIT.get());
        }
    }

    public static void checkPurchaseAmountUnit(int purchaseAmount) {
        if (purchaseAmount % LottoRule.PURCHASE_AMOUNT_UNIT != 0) {
            throw new IllegalArgumentException(LottoErrorMessage.PURCHASE_AMOUNT_UNIT_INVALID.get());
        }
    }

    public static void checkBonusNumberRange(int number) {
        if (!isValidRangeInLotto(number)) {
            throw new IllegalArgumentException(LottoErrorMessage.LOTTO_NUMBER_OUT_OF_RANGE.get());
        }
    }

    public static void checkWinningSetDuplicate(List<Integer> winningNumbers, int bonusNumber) {
        Set<Integer> uniqueNumbers = new HashSet<>(winningNumbers);
        uniqueNumbers.add(bonusNumber);
        if (uniqueNumbers.size() != LottoRule.LOTTO_NUMBERS_COUNT + 1) {
            throw new IllegalArgumentException(LottoErrorMessage.WINNING_SET_NUMBERS_DUPLICATE.get());
        }
    }
}

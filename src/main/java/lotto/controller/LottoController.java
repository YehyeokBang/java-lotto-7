package lotto.controller;

import java.util.List;
import lotto.dto.WinningResult;
import lotto.util.input.InputParser;
import lotto.model.evaluate.LottoResultEvaluator;
import lotto.model.win.BonusNumber;
import lotto.model.shop.LottoShop;
import lotto.model.ticket.LottoTickets;
import lotto.model.win.LottoWinningSet;
import lotto.model.shop.TicketSeller;
import lotto.model.win.WinningNumbers;
import lotto.util.input.InputUtil;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {

    private final LottoShop lottoShop;
    private final InputView inputView;
    private final OutputView outputView;

    public LottoController(InputView inputView, OutputView outputView) {
        lottoShop = LottoShop.openShop();
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        LottoTickets lottoTickets = purchaseLotto();
        LottoWinningSet winningSet = createWinningSetFromUserInput();
        evaluateAndDisplayResults(winningSet, lottoTickets);
    }

    private LottoTickets purchaseLotto() {
        LottoTickets lottoTickets = InputUtil.retryIfError(this::purchaseLottoTicketsByUserRequest);
        displayLotto(lottoTickets);
        return lottoTickets;
    }

    private LottoTickets purchaseLottoTicketsByUserRequest() {
        int purchaseAmount = readPurchaseAmount();
        return purchaseLottoTickets(purchaseAmount);
    }

    private int readPurchaseAmount() {
        String rawInputPurchaseAmount = inputView.requestPurchaseAmount();
        return InputParser.validateAndParsePurchaseAmount(rawInputPurchaseAmount);
    }

    private LottoTickets purchaseLottoTickets(int purchaseAmount) {
        TicketSeller ticketSeller = lottoShop.findTicketSeller();
        return ticketSeller.exchangeMoneyForTickets(purchaseAmount);
    }

    private void displayLotto(LottoTickets lottoTickets) {
        outputView.printPurchasedQuantity(lottoTickets.getCount());
        outputView.printLottoTickets(lottoTickets.getAllNumbers());
    }

    private LottoWinningSet createWinningSetFromUserInput() {
        WinningNumbers winningNumbers = InputUtil.retryIfError(this::readWinningNumbers);
        return InputUtil.retryIfError(() -> {
            BonusNumber bonusNumber = readBonusNumber();
            return new LottoWinningSet(winningNumbers, bonusNumber);
        });
    }

    private WinningNumbers readWinningNumbers() {
        String rawInputWinningNumbers = inputView.requestWinningNumbers();
        List<Integer> numbers = InputParser.validateAndParseWinningNumbers(rawInputWinningNumbers);
        return new WinningNumbers(numbers);
    }

    private BonusNumber readBonusNumber() {
        String rawInputBonusNumber = inputView.requestBonusNumber();
        int bonusNumber = InputParser.validateAndParseBonusNumber(rawInputBonusNumber);
        return new BonusNumber(bonusNumber);
    }

    private void evaluateAndDisplayResults(LottoWinningSet winningSet, LottoTickets lottoTickets) {
        WinningResult winningResult = evaluateLotto(winningSet, lottoTickets);
        displayResult(winningResult);
    }

    private WinningResult evaluateLotto(LottoWinningSet winningSet, LottoTickets lottoTickets) {
        LottoResultEvaluator lottoResultEvaluator = crateLottoResultEvaluator(winningSet);
        return lottoResultEvaluator.evaluate(lottoTickets);
    }

    private LottoResultEvaluator crateLottoResultEvaluator(LottoWinningSet winningSet) {
        List<Integer> winningNumbers = winningSet.getWinningNumbers();
        int bonusNumber = winningSet.getBonusNumber();
        return new LottoResultEvaluator(winningNumbers, bonusNumber);
    }

    private void displayResult(WinningResult winningResult) {
        outputView.printWinningResult(winningResult);
    }
}

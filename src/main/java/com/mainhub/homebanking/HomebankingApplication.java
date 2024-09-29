package com.mainhub.homebanking;

import com.mainhub.homebanking.models.*;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.*;
import com.mainhub.homebanking.utils.LoanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoanUtil loanUtil;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			LocalDate today = LocalDate.now();

			// Client Melba
			Client melba = new Client("Melba", "Morel", "melbamorel@mindhub.com", passwordEncoder.encode("Melba123."));
			clientRepository.save(melba);

			// Accounts for Client Melba
			Account account1 = new Account("VIN001", LocalDate.now(), 5000.0);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);
			melba.addAccount(account1);
			melba.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);

			// Transactions for Client Melba
			Transaction paymentForElectricityMelba = new Transaction(TransactionType.DEBIT, -3000, "DEBIN PAYMENT SERVICE", LocalDateTime.now(), account1);
			Transaction receivedTransfer1Melba = new Transaction(TransactionType.CREDIT, 1500, "CR INTERBANK", LocalDateTime.now().plusDays(1), account1);
			Transaction paymentForInternetMelba = new Transaction(TransactionType.DEBIT, -1000, "DEBIN PAYMENT SERVICE", LocalDateTime.now().plusDays(2), account2);
			Transaction receivedTransfer2Melba = new Transaction(TransactionType.CREDIT, 1000, "CR INTERBANK", LocalDateTime.now().plusDays(3), account2);
			account1.addTransaction(paymentForElectricityMelba);
			account1.addTransaction(receivedTransfer1Melba);
			account2.addTransaction(receivedTransfer2Melba);
			account2.addTransaction(paymentForInternetMelba);
			transactionRepository.save(paymentForElectricityMelba);
			transactionRepository.save(receivedTransfer1Melba);
			transactionRepository.save(receivedTransfer2Melba);
			transactionRepository.save(paymentForInternetMelba);

			//Loans
			Loan mortgage = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60, 72));
			Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24, 36));
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			// Loans for Client Melba
			double totalAmountMortgageMelba = loanUtil.calculateTotalAmountWithInterest(400000, 48);  // Usa el método para calcular el monto con interés
			ClientLoan mortgageLoanMelba = new ClientLoan(400000, 48, melba, mortgage, account1);
			mortgageLoanMelba.setTotalAmount(totalAmountMortgageMelba);  // Establece el total con interés
			clientLoanRepository.save(mortgageLoanMelba);

			double totalAmountPersonalMelba = loanUtil.calculateTotalAmountWithInterest(100000, 24);  // Otro ejemplo
			ClientLoan personalLoanMelba = new ClientLoan(100000, 24, melba, personal, account1);
			personalLoanMelba.setTotalAmount(totalAmountPersonalMelba);
			clientLoanRepository.save(personalLoanMelba);

			double totalAmountAutomotiveMelba = loanUtil.calculateTotalAmountWithInterest(250000, 24);  // Otro ejemplo
			ClientLoan automotiveLoanMelba = new ClientLoan(250000, 24, melba, automotive, account2);
			automotiveLoanMelba.setTotalAmount(totalAmountAutomotiveMelba);
			clientLoanRepository.save(automotiveLoanMelba);

			// Cards
			// DEBIT CARDS
			Card debitCardGold = new Card(CardType.DEBIT, CardColor.GOLD, LocalDate.now(), LocalDate.now().plusYears(5));
			String cardNumber1 = debitCardGold.generateCardNumber();
			String cardNumber2 = debitCardGold.generateCardNumber();
			String cardNumber3 = debitCardGold.generateCardNumber();
			String cardNumber4 = debitCardGold.generateCardNumber();
			debitCardGold.setNumber(cardNumber1 + " " + cardNumber2 + " " + cardNumber3 + " " + cardNumber4);
			debitCardGold.setCvv(debitCardGold.generateCVV());

			Card debitCardSilver = new Card(CardType.DEBIT, CardColor.SILVER, LocalDate.now(), LocalDate.now().plusYears(5));
			debitCardSilver.setNumber(debitCardSilver.generateCardNumber());
			debitCardSilver.setCvv(debitCardSilver.generateCVV());

			Card debitCardTitanium = new Card(CardType.DEBIT, CardColor.TITANIUM, LocalDate.now(), LocalDate.now().plusYears(5));
			debitCardTitanium.setNumber(debitCardTitanium.generateCardNumber());
			debitCardTitanium.setCvv(debitCardGold.generateCVV());

			// CREDIT CARDS
			Card creditCardGold = new Card(CardType.CREDIT, CardColor.GOLD, LocalDate.now(), LocalDate.now().plusYears(5));
			creditCardGold.setNumber(creditCardGold.generateCardNumber());
			creditCardGold.setCvv(creditCardGold.generateCVV());

			Card creditCardSilver = new Card(CardType.CREDIT, CardColor.SILVER, LocalDate.now(), LocalDate.now().plusYears(5));
			creditCardSilver.setNumber(creditCardSilver.generateCardNumber());
			creditCardSilver.setCvv(creditCardGold.generateCVV());

			Card creditCardTitanium = new Card(CardType.CREDIT, CardColor.TITANIUM, LocalDate.now(), LocalDate.now().plusYears(5));
			String cardNumber5 = creditCardTitanium.generateCardNumber();
			String cardNumber6 = creditCardTitanium.generateCardNumber();
			String cardNumber7 = creditCardTitanium.generateCardNumber();
			String cardNumber8 = creditCardTitanium.generateCardNumber();
			creditCardTitanium.setNumber(cardNumber5 + " " + cardNumber6 + " " + cardNumber7 + " " + cardNumber8);
			creditCardTitanium.setCvv(creditCardTitanium.generateCVV());

			// Add cards to Melba
			melba.addCard(debitCardGold);
			cardRepository.save(debitCardGold);
			melba.addCard(creditCardTitanium);
			cardRepository.save(creditCardTitanium);

			// Client Ana
			Client ana = new Client("Ana", "Gonzalez", "anagonzalez@mindhub.com", passwordEncoder.encode("Ana1234."));
			clientRepository.save(ana);

			// Accounts for Client Ana
			Account account3 = new Account("VIN003", LocalDate.now(), 12000.0);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(2), 13000.0);
			ana.addAccount(account3);
			ana.addAccount(account4);
			accountRepository.save(account3);
			accountRepository.save(account4);

			// Transactions for Client Ana
			Transaction paymentForWaterAna = new Transaction(TransactionType.DEBIT, -2000, "DEBIN PAYMENT SERVICE", LocalDateTime.now(), account3);
			Transaction receivedTransfer1Ana = new Transaction(TransactionType.CREDIT, 1000, "CR INTERBANK", LocalDateTime.now().plusDays(2), account3);
			Transaction paymentForInternetAna = new Transaction(TransactionType.DEBIT, -1000, "DEBIN PAYMENT SERVICE", LocalDateTime.now().plusDays(1), account4);
			Transaction receivedTransfer2Ana = new Transaction(TransactionType.CREDIT, 1000, "CR INTERBANK", LocalDateTime.now().plusDays(2), account4);
			account3.addTransaction(paymentForWaterAna);
			account3.addTransaction(receivedTransfer1Ana);
			account4.addTransaction(receivedTransfer2Ana);
			account4.addTransaction(paymentForInternetAna);
			transactionRepository.save(paymentForWaterAna);
			transactionRepository.save(receivedTransfer1Ana);
			transactionRepository.save(receivedTransfer2Ana);
			transactionRepository.save(paymentForInternetAna);

			// Loans for Client Ana
			double totalAmountMortgageAna = loanUtil.calculateTotalAmountWithInterest(350000, 60);  // Usa el método para calcular el monto con interés
			ClientLoan mortgageLoanAna = new ClientLoan(350000, 60, ana, mortgage, account3);
			mortgageLoanAna.setTotalAmount(totalAmountMortgageAna);  // Establece el total con interés
			clientLoanRepository.save(mortgageLoanAna);

			double totalAmountPersonalAna = loanUtil.calculateTotalAmountWithInterest(50000, 6);  // Otro ejemplo
			ClientLoan personalLoanAna = new ClientLoan(50000, 6, ana, personal, account4);
			personalLoanAna.setTotalAmount(totalAmountPersonalAna);
			clientLoanRepository.save(personalLoanAna);

			double totalAmountAutomotiveAna = loanUtil.calculateTotalAmountWithInterest(250000, 36);  // Otro ejemplo
			ClientLoan automotiveLoanAna = new ClientLoan(250000, 36, ana, automotive, account4);
			automotiveLoanAna.setTotalAmount(totalAmountAutomotiveAna);
			clientLoanRepository.save(automotiveLoanAna);

			// Add cards to Ana
			ana.addCard(debitCardSilver);
			cardRepository.save(debitCardSilver);
			ana.addCard(creditCardGold);
			cardRepository.save(creditCardGold);

			// Client Luz
			Client luz = new Client("Luz", "Mieres", "luzmieres@mindhub.com", passwordEncoder.encode("Luz1234."));
			clientRepository.save(luz);

			// Accounts for Client Luz
			Account account5 = new Account("VIN005", LocalDate.now(), 14000.0);
			Account account6 = new Account("VIN006", LocalDate.now().plusDays(3), 15000.0);
			luz.addAccount(account5);
			luz.addAccount(account6);
			accountRepository.save(account5);
			accountRepository.save(account6);

			// Transactions for Client Luz
			Transaction sentTransferLuz = new Transaction(TransactionType.DEBIT, -3000, "DEBIN PAYMENT SERVICE", LocalDateTime.now(), account5);
			Transaction receivedTransfer1Luz = new Transaction(TransactionType.CREDIT, 2000, "CR INTERBANK", LocalDateTime.now().plusDays(3), account5);
			Transaction paymentForInternetLuz = new Transaction(TransactionType.DEBIT, -1000, "DEBIN PAYMENT SERVICE", LocalDateTime.now().plusDays(1), account6);
			Transaction receivedTransfer2Luz = new Transaction(TransactionType.CREDIT, 1000, "CR INTERBANK", LocalDateTime.now().plusDays(2), account6);
			account5.addTransaction(sentTransferLuz);
			account5.addTransaction(receivedTransfer1Luz);
			account6.addTransaction(receivedTransfer2Luz);
			account6.addTransaction(paymentForInternetLuz);
			transactionRepository.save(sentTransferLuz);
			transactionRepository.save(receivedTransfer1Luz);
			transactionRepository.save(receivedTransfer2Luz);
			transactionRepository.save(paymentForInternetLuz);

			// Loans for Client Luz
			double totalAmountMortgageLuz = loanUtil.calculateTotalAmountWithInterest(450000, 72);  // Usa el método para calcular el monto con interés
			ClientLoan mortgageLoanLuz = new ClientLoan(450000, 72, luz, mortgage, account5);
			mortgageLoanLuz.setTotalAmount(totalAmountMortgageLuz);  // Establece el total con interés
			clientLoanRepository.save(mortgageLoanLuz);

			double totalAmountPersonalLuz = loanUtil.calculateTotalAmountWithInterest(100000, 6);  // Otro ejemplo
			ClientLoan personalLoanLuz = new ClientLoan(100000, 6, luz, personal, account6);
			personalLoanLuz.setTotalAmount(totalAmountPersonalLuz);
			clientLoanRepository.save(personalLoanLuz);

			double totalAmountAutomotiveLuz = loanUtil.calculateTotalAmountWithInterest(200000, 36);  // Otro ejemplo
			ClientLoan automotiveLoanLuz = new ClientLoan(200000, 12, luz, automotive, account5);
			automotiveLoanLuz.setTotalAmount(totalAmountAutomotiveLuz);
			clientLoanRepository.save(automotiveLoanLuz);

			// Add cards to Luz
			luz.addCard(debitCardTitanium);
			cardRepository.save(debitCardTitanium);
			luz.addCard(creditCardSilver);
			cardRepository.save(creditCardSilver);

			// Print details of the 3 clients
			System.out.println(melba);
			System.out.println(ana);
			System.out.println(luz);
		};
	}
}

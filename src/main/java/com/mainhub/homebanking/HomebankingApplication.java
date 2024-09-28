package com.mainhub.homebanking;

import com.mainhub.homebanking.models.*;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.*;
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

			// ***** Cliente Melba *****
			Client melba = new Client("Melba", "Morel", "melbamorel@mindhub.com", passwordEncoder.encode("Melba123."));
			clientRepository.save(melba);  // Guarda el cliente antes de añadir las cuentas

			// Crear y guardar cuentas para Melba
			Account cuenta1Melba = new Account("VIN001", today, 5000);
			Account cuenta2Melba = new Account("VIN002", today, 7500);

			// Asignar cliente y guardar cuentas
			cuenta1Melba.setClient(melba);
			cuenta2Melba.setClient(melba);
			accountRepository.save(cuenta1Melba);
			accountRepository.save(cuenta2Melba);

			// Crear y guardar transacciones para la cuenta 1 de Melba
			Transaction transaccion1Melba1 = new Transaction(TransactionType.CREDIT, 200, "Salary");
			Transaction transaccion2Melba1 = new Transaction(TransactionType.DEBIT, -100, "Groceries");
			Transaction transaccion3Melba1 = new Transaction(TransactionType.DEBIT, -50, "Utilities");
			cuenta1Melba.addTransaction(transaccion1Melba1);
			cuenta1Melba.addTransaction(transaccion2Melba1);
			cuenta1Melba.addTransaction(transaccion3Melba1);
			transactionRepository.save(transaccion1Melba1);
			transactionRepository.save(transaccion2Melba1);
			transactionRepository.save(transaccion3Melba1);

			// Crear y guardar transacciones para la cuenta 2 de Melba
			Transaction transaccion1Melba2 = new Transaction(TransactionType.CREDIT, 300, "Bonus");
			Transaction transaccion2Melba2 = new Transaction(TransactionType.DEBIT, -200, "Rent");
			Transaction transaccion3Melba2 = new Transaction(TransactionType.DEBIT, -150, "Utilities");
			cuenta2Melba.addTransaction(transaccion1Melba2);
			cuenta2Melba.addTransaction(transaccion2Melba2);
			cuenta2Melba.addTransaction(transaccion3Melba2);
			transactionRepository.save(transaccion1Melba2);
			transactionRepository.save(transaccion2Melba2);
			transactionRepository.save(transaccion3Melba2);

			// ***** Cliente Luz *****
			Client luz = new Client("Luz", "Mieres", "luzmieres@mindhub.com", passwordEncoder.encode("Luz1234."));
			clientRepository.save(luz);  // Guarda el cliente antes de añadir las cuentas

			// Crear y guardar cuentas para Luz
			Account cuenta1Luz = new Account("VIN004", today, 10000);
			Account cuenta2Luz = new Account("VIN005", today, 20000);

			// Asignar cliente y guardar cuentas
			cuenta1Luz.setClient(luz);
			cuenta2Luz.setClient(luz);
			accountRepository.save(cuenta1Luz);
			accountRepository.save(cuenta2Luz);

			// Crear y guardar transacciones para la cuenta 1 de Luz
			Transaction transaccion1Luz1 = new Transaction(TransactionType.CREDIT, 5000, "Freelance Work");
			Transaction transaccion2Luz1 = new Transaction(TransactionType.DEBIT, -2000, "New Laptop");
			Transaction transaccion3Luz1 = new Transaction(TransactionType.DEBIT, -100, "Groceries");
			cuenta1Luz.addTransaction(transaccion1Luz1);
			cuenta1Luz.addTransaction(transaccion2Luz1);
			cuenta1Luz.addTransaction(transaccion3Luz1);
			transactionRepository.save(transaccion1Luz1);
			transactionRepository.save(transaccion2Luz1);
			transactionRepository.save(transaccion3Luz1);

			// Crear y guardar transacciones para la cuenta 2 de Luz
			Transaction transaccion1Luz2 = new Transaction(TransactionType.CREDIT, 3000, "Consulting");
			Transaction transaccion2Luz2 = new Transaction(TransactionType.DEBIT, -1500, "Vacation");
			Transaction transaccion3Luz2 = new Transaction(TransactionType.DEBIT, -500, "Groceries");
			cuenta2Luz.addTransaction(transaccion1Luz2);
			cuenta2Luz.addTransaction(transaccion2Luz2);
			cuenta2Luz.addTransaction(transaccion3Luz2);
			transactionRepository.save(transaccion1Luz2);
			transactionRepository.save(transaccion2Luz2);
			transactionRepository.save(transaccion3Luz2);

			// Préstamos y clientes pidiendo préstamos
			Loan hipotcario = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60));
			loanRepository.save(hipotcario);

			Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			loanRepository.save(personal);

			Loan automotriz = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24,36));
			loanRepository.save(automotriz);

			// Préstamos para Melba
			ClientLoan clientLoan1 = new ClientLoan(400000, 60, melba, hipotcario, cuenta1Melba);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan(50000, 12, melba, personal, cuenta2Melba);
			clientLoanRepository.save(clientLoan2);

			// Préstamos para Luz
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, luz, personal, cuenta1Luz);
			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4 = new ClientLoan(200000, 36, luz, automotriz, cuenta2Luz);
			clientLoanRepository.save(clientLoan4);

			// Tarjetas
			Card cardLuz = new Card(LocalDateTime.now().plusYears(5), CardType.DEBIT, CardColor.SILVER, luz);
			cardRepository.save(cardLuz);

			Card card1 = new Card(LocalDateTime.now().plusYears(5), CardType.DEBIT, CardColor.GOLD, melba);
			Card card2 = new Card(LocalDateTime.now().plusYears(10), CardType.CREDIT, CardColor.TITANIUM, melba);
			cardRepository.save(card1);
			cardRepository.save(card2);
		};
	}

}

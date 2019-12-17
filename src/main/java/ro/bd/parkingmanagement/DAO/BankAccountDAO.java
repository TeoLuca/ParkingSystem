package ro.bd.parkingmanagement.DAO;

import java.util.List;

import javax.swing.JTable;

import ro.bd.parkingmanagement.model.BankAccount;

public interface BankAccountDAO {
	BankAccount createBankAccount(BankAccount bankAccount);

	BankAccount getBankAccount(int id);

	BankAccount updateBankAccount(int id, BankAccount bankAccount);

	List<BankAccount> getAll();

	boolean isCardNumberAlreadyUsed(String cardNumber);

	boolean deleteBankAccount(int id);

	JTable populateJTableFromDB(int userId);

	String[] getAllCardNumbers(int userId);
}

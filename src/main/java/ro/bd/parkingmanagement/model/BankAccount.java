package ro.bd.parkingmanagement.model;

import java.sql.Date;

public class BankAccount {
	private int bankAccountId;
	private int userId;
	private String cardNumber;
	private Date expiringDate;
	private int cvvCode;
	private double accountBalance;

	// this constructor is needed
	public BankAccount() {
		super();
	}

	public BankAccount(int userId, String cardNumber, Date expiringDate, int cvvCode, double accountBalance) {
		super();
		this.userId = userId;
		this.cardNumber = cardNumber;
		this.expiringDate = expiringDate;
		this.cvvCode = cvvCode;
		this.accountBalance = accountBalance;
	}

	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getExpiringDate() {
		return expiringDate;
	}

	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}

	public int getCvvCode() {
		return cvvCode;
	}

	public void setCvvCode(int cvvCode) {
		this.cvvCode = cvvCode;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@Override
	public String toString() {
		return "BankAccount [bankAccountId=" + bankAccountId + ", userId=" + userId + ", cardNumber=" + cardNumber
				+ ", expiringDate=" + expiringDate + ", cvvCode=" + cvvCode + ", accountBalance=" + accountBalance + "]";
	}

}

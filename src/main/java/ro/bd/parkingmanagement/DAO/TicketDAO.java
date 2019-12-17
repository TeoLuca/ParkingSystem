package ro.bd.parkingmanagement.DAO;

import java.util.List;

import javax.swing.JTable;

import ro.bd.parkingmanagement.model.Ticket;

public interface TicketDAO {
	Ticket insertTicket(Ticket ticket);

	Ticket getTicket(int id);

	Ticket getTicketByCode(String code);

	Ticket updateTicket(int id, Ticket ticket);

	boolean isValid(String ticketCode);

	List<Ticket> getAll();

	boolean deleteTicket(int id);

	JTable populateJTableFromDB();

	boolean payTicket(String ticketCode, String cardNumber, double bankAccountValue, double fee);
}

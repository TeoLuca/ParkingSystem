package ro.bd.parkingmanagement.DAO;

import java.util.List;

import javax.swing.JTable;

import ro.bd.parkingmanagement.model.Price;

public interface PriceDAO {
	Price insertPrice(Price price);

	Price getPrice(int id);

	Price updatePrice(int id, Price price);

	List<Price> getAll();

	boolean deletePrice(int id);

	JTable populateJTableFromDB();
}

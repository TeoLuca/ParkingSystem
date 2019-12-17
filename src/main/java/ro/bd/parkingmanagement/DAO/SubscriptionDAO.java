package ro.bd.parkingmanagement.DAO;

import java.sql.Date;
import java.util.List;

import javax.swing.JTable;

import ro.bd.parkingmanagement.model.ParkingSubscription;

public interface SubscriptionDAO {
	ParkingSubscription insertSubscription(ParkingSubscription subscription);
	
	ParkingSubscription subscribe(ParkingSubscription subscription, String cardNumber, double fee);

	ParkingSubscription getSubscription(int id);

	ParkingSubscription updateSubscription(int id, ParkingSubscription subscription);

	List<ParkingSubscription> getAll();

	boolean deleteSubscription(int id);

	JTable populateJTableFromDB();

	JTable populateJTableFromDB(int userId);

	Date getExpiringDate(int months);

	boolean paySubscription(String cardNumber, double fee);
}

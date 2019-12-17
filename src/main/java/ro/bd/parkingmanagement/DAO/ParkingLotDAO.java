package ro.bd.parkingmanagement.DAO;

import java.util.List;

import javax.swing.JTable;

import ro.bd.parkingmanagement.model.ParkingLot;

public interface ParkingLotDAO {
	ParkingLot insertParkingLot(ParkingLot parkingLot);

	ParkingLot getParkingLot(int lotId);
	
	ParkingLot getFirstAvailable();
	
	List<ParkingLot> getAll();

	int getAvailableLots();

	int getOccupiedLots();

	ParkingLot updateParkingAvailability(int lotId, boolean availability);

	boolean deleteParkingLot(int lotId);

	JTable populateJTableFromDB();

}

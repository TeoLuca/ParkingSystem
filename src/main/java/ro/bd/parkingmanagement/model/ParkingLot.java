package ro.bd.parkingmanagement.model;

public class ParkingLot {
	private int lotNo;
	private int floorNo;
	private boolean availability;

	public ParkingLot() {
		super();
	}

	public ParkingLot(int lotNo, int floorNo, boolean availability) {
		this.lotNo = lotNo;
		this.floorNo = floorNo;
		this.availability = availability;
	}

	public int getLotNo() {
		return lotNo;
	}

	public void setLotNo(int lotNo) {
		this.lotNo = lotNo;
	}

	public int getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(int floorNo) {
		this.floorNo = floorNo;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	@Override
	public String toString() {
		return "ParkingLot [lotNo=" + lotNo + ", floorNo=" + floorNo
				+ ", availability=" + availability + "]";
	}
}

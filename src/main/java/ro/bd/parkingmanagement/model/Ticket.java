package ro.bd.parkingmanagement.model;

public class Ticket {
	private int ticketId;
	private String ticketCode;
	private int lotNo;
	private int floorNo;

	public Ticket() {
		super();
	}

	public Ticket(String ticketCode, int lotNo, int floorNo) {
		super();
		this.ticketCode = ticketCode;
		this.lotNo = lotNo;
		this.floorNo = floorNo;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
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

	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", ticketCode=" + ticketCode
				+ ", lotNo=" + lotNo + ", floorNo=" + floorNo + "]";
	}

}

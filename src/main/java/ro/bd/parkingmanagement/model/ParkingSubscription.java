package ro.bd.parkingmanagement.model;

import java.sql.Date;

public class ParkingSubscription {
	private int subscriptionId;
	private int userId;
	private int lotNo;
	private Date expiringDate;

	public ParkingSubscription() {
		super();
	}

	public ParkingSubscription(int userId, int lotNo, Date expiringDate) {
		super();
		this.userId = userId;
		this.lotNo = lotNo;
		this.expiringDate = expiringDate;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLotNo() {
		return lotNo;
	}

	public void setLotNo(int lotNo) {
		this.lotNo = lotNo;
	}

	public Date getExpiringDate() {
		return expiringDate;
	}

	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}

	@Override
	public String toString() {
		return "ParkingSubscription [subscriptionId=" + subscriptionId
				+ ", userId=" + userId + ", lotNo=" + lotNo + ", expiringDate="
				+ expiringDate + "]";
	}
}

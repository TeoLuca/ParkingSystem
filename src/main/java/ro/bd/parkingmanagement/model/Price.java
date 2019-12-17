package ro.bd.parkingmanagement.model;

public class Price {
	private int priceId;
	private double dayPrice;
	private double nightPrice;

	public Price() {
		super();
	}

	public Price(double dayPrice, double nightPrice) {
		super();
		this.dayPrice = dayPrice;
		this.nightPrice = nightPrice;
	}

	public int getPriceId() {
		return priceId;
	}

	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}

	public double getDayPrice() {
		return dayPrice;
	}

	public void setDayPrice(double dayPrice) {
		this.dayPrice = dayPrice;
	}

	public double getNightPrice() {
		return nightPrice;
	}

	public void setNightPrice(double nightPrice) {
		this.nightPrice = nightPrice;
	}

	@Override
	public String toString() {
		return "Price [priceId=" + priceId + ", dayPrice=" + dayPrice
				+ ", nightPrice=" + nightPrice + "]";
	}
	
}

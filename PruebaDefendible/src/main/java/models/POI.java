package models;

import java.util.Date;

public class POI {
	
	private final int poiId;
	private final Double latitude;
	private final Double longitude;
	private final String country;
	private final String city;
	private final String description;
	private final Date update_date;
	
	
	//Generamos el construtor.
	public POI(int poiId, Double latitude, Double longitude, String country, String city, String description,
			Date update_date) {
		this.poiId = poiId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.city = city;
		this.description = description;
		this.update_date = update_date;
	}
	
	// Generamos los getters.
	public int getPoiId() {
		return poiId;
	}


	public Double getLatitude() {
		return latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public String getCountry() {
		return country;
	}


	public String getCity() {
		return city;
	}


	public String getDescription() {
		return description;
	}


	public Date getUpdate() {
		return update_date;
	}

	// Generamos el toString().
	@Override
	public String toString() {
		return "Poi [poiId=" + poiId + ", latitude=" + latitude + ", longitude=" + longitude + ", country=" + country
				+ ", city=" + city + ", description=" + description + ", update=" + update_date + "]";
	}
	
	
}

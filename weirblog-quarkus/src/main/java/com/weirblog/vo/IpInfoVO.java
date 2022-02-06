package com.weirblog.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 淘宝IP解析
 * @author weir
 *
 */
public class IpInfoVO {
	private String ip;
	private String country;
	private String area;
	private String region;
	private String city;
	private String county;
	private String isp;
	@JsonProperty("country_id")
	private Integer countryId;
	@JsonProperty("area_id")
	private Integer areaId;
	@JsonProperty("region_id")
	private Integer regionId;
	@JsonProperty("city_id")
	private Integer cityId;
	@JsonProperty("county_id")
	private Integer countyId;
	@JsonProperty("isp_id")
	private Integer ispId;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getIsp() {
		return isp;
	}
	public void setIsp(String isp) {
		this.isp = isp;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public Integer getIspId() {
		return ispId;
	}
	public void setIspId(Integer ispId) {
		this.ispId = ispId;
	}
	@Override
	public String toString() {
		return "IpInfoVO [ip=" + ip + ", country=" + country + ", area=" + area + ", region=" + region + ", city="
				+ city + ", county=" + county + ", isp=" + isp + ", countryId=" + countryId + ", areaId=" + areaId
				+ ", regionId=" + regionId + ", cityId=" + cityId + ", countyId=" + countyId + ", ispId=" + ispId + "]";
	}
}

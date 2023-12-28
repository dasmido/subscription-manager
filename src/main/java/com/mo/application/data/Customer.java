package com.mo.application.data;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Customer extends AbstractEntity {
	private String name;
	private String phone;
	private String address;
	private String bios;
	private String macaddress;
	private String hddinfo;
	private String extra;
	private String windowsversion;
	private LocalDate activatedate;
	private String serialnumber;
	private boolean revoke;
	private String appversion;
	private String activate;
	private String lastseen;
	private String activationcode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBios() {
		return bios;
	}
	public void setBios(String bios) {
		this.bios = bios;
	}
	public String getMacaddress() {
		return macaddress;
	}
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
	public String getHddinfo() {
		return hddinfo;
	}
	public void setHddinfo(String hddinfo) {
		this.hddinfo = hddinfo;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getWindowsversion() {
		return windowsversion;
	}
	public void setWindowsversion(String windowsversion) {
		this.windowsversion = windowsversion;
	}
	public LocalDate getActivatedate() {
		return activatedate;
	}
	public void setActivatedate(LocalDate activatedate) {
		this.activatedate = activatedate;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	
	public String getAppversion() {
		return appversion;
	}
	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getLastseen() {
		return lastseen;
	}
	public void setLastseen(String lastseen) {
		this.lastseen = lastseen;
	}
	public String getActivationcode() {
		return activationcode;
	}
	public void setActivationcode(String activationcode) {
		this.activationcode = activationcode;
	}
	public boolean isRevoke() {
		return revoke;
	}
	public void setRevoke(boolean revoke) {
		this.revoke = revoke;
	}
	
	
	

}

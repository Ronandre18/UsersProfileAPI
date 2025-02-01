package com.system.API.model;

import java.sql.Date;

public class Users {
	private String userName;
	private String fullName;
	private String emailAddress;
	private String gender;
	private Date birthDate;
	private int age;
	private String role;
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Users(String userName, String fullName, String emailAddress, String gender, Date birthDate, int age,
			String role) {
		super();
		this.userName = userName;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.gender = gender;
		this.birthDate = birthDate;
		this.age = age;
		this.role = role;
	}
}

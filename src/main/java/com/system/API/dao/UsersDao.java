package com.system.API.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.system.API.Interface.UsersInterface;
import com.system.API.model.GenericResponse;
import com.system.API.model.Users;
import com.system.API.requestModel.UsersBody;

@Repository
public class UsersDao implements UsersInterface {
 
	@Autowired 
	JdbcTemplate template;
	
	@Autowired
	Gson gson;	
	
    private static final Logger logger = LoggerFactory.getLogger(UsersDao.class);

	private class GenericResponseMapper implements RowMapper<GenericResponse> {
		@Override
		public GenericResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
			GenericResponse res = new GenericResponse();
				res.setResponseCode(rs.getInt("responseCode"));
				res.setResponseMessage(rs.getString("responseMsg"));
			return res;
		}
	}
	
	private class UsersInfoMapper implements RowMapper<Users> {

		@Override
		public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
			Users users = new Users(null, null, null, null, null, rowNum, null);
			 users.setUserName(rs.getString("userName"));
			 users.setFullName(rs.getString("fullName"));
			 users.setEmailAddress(rs.getString("emailAddress"));
			 users.setGender(rs.getString("genderName"));
			 users.setBirthDate(rs.getDate("birthDate"));
			 users.setAge(rs.getInt("age"));
			 users.setRole(rs.getString("roleName"));
			return users;
		}
		
	}
	
	@Override
	public GenericResponse addNewUser(UsersBody user) {
		GenericResponse response = new GenericResponse();
		String SQL = "exec sp_addUsers ?,?,?,?,?,?,?,?";
		try {
			response = this.template.queryForObject(SQL, new GenericResponseMapper(),
					user.getUserName(), user.getFirstName(), user.getMiddleName(), user.getLastName(), 
					user.getEmail(), user.getBirthDate(), user.getGenderId(), user.getRoleId());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("AddNewUser Response: " + gson.toJson(response));
		return response;
	}
	
	@Override
	public List<Users> getAllUsers() {
		List<Users> users = new ArrayList<>();
		try {
			String sql = "exec sp_GetUsersInfo null";
			users = this.template.query(sql, new UsersInfoMapper());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("GetAllUsers response: " + gson.toJson(users));
		return users;
	}
	
	@Override
	public Users getUser(String userName) {
		Users users = new Users(null, null, null, null, null, 0, null);
		try {
			String sql = "exec sp_GetUsersInfo ?";
			users = this.template.queryForObject(sql, new UsersInfoMapper(), userName);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		logger.info("GetUser response: " + gson.toJson(users));
		return users;
	}
	
	@Override
	public GenericResponse updateUser(UsersBody user) {
		GenericResponse response = new GenericResponse();
		try {
			String sql =  "exec sp_UpdateUser ?,?,?,?,?,?,?,?";
			response = this.template.queryForObject(sql, new GenericResponseMapper(),
												    user.getUserName(), user.getFirstName(),
												    user.getMiddleName(), user.getLastName(),
												    user.getEmail(), user.getBirthDate(),
												    user.getGenderId(), user.getRoleId());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		logger.info("UpdateUser response: " + gson.toJson(response));
		return response;
	}
	
	@Override
	public GenericResponse deleteUser(UsersBody user) {
		GenericResponse response = new GenericResponse();
		try {
			String sql =  "exec sp_DeleteUser ?";
			response = this.template.queryForObject(sql, new GenericResponseMapper(), user.getUserName());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		logger.info("DeleteUser response: " + gson.toJson(response));
		return response;
	}
 }

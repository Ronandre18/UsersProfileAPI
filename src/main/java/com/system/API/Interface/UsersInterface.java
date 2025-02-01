package com.system.API.Interface;

import java.sql.SQLException;

import java.util.List;

import com.system.API.model.GenericResponse;
import com.system.API.model.Users;
import com.system.API.requestModel.UsersBody;

public interface UsersInterface {

	GenericResponse addNewUser(UsersBody user) throws SQLException;

	List<Users> getAllUsers();

	GenericResponse updateUser(UsersBody user);

	GenericResponse deleteUser(UsersBody user);

	Users getUser(String userName);

	

}

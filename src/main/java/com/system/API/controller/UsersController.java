package com.system.API.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.API.dao.UsersDao;
import com.system.API.model.GenericResponse;
import com.system.API.model.Users;
import com.system.API.requestModel.UsersBody;

@RestController
@RequestMapping("/api") 
public class UsersController {
	
	@Autowired
	UsersDao userDao;	
	
	@PostMapping("/AddNewUser")
	public GenericResponse addNewUser(@RequestBody UsersBody user){
		GenericResponse response= new GenericResponse();
		 response = userDao.addNewUser(user);
		 return response;
		 
	}
	
	@GetMapping("/GetAllUsers")
	public List<Users> getAllUsers() {
		List<Users> users = new ArrayList<>();
		users = userDao.getAllUsers();
		return users;
	} 
	
	@GetMapping("/GetUser/{userName}")
	public Users getUser(@PathVariable String userName) {
		Users users = new Users(null, null, null, null, null, 0, null);
		users = userDao.getUser(userName);
		return users;
	} 
	
	@PostMapping("/UpdateUser")
	public GenericResponse updateUser(@RequestBody UsersBody user){
		GenericResponse response= new GenericResponse();
		 response = userDao.updateUser(user);
		 return response;
		 
	}
	
	@PostMapping("/DeleteUser")
	public GenericResponse deleteUser(@RequestBody UsersBody user){
		GenericResponse response= new GenericResponse();
		 response = userDao.deleteUser(user);
		 return response;
		 
	}
	
}

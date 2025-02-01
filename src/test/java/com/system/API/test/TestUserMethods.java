package com.system.API.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;
import com.system.API.MyApiApplication;
import com.system.API.dao.UsersDao;
import com.system.API.model.GenericResponse;
import com.system.API.model.Users;
import com.system.API.requestModel.UsersBody;

@SpringBootTest(classes = MyApiApplication.class)
public class TestUserMethods {

    private static final Logger logger = LoggerFactory.getLogger(TestUserMethods.class);

    @Autowired
    private UsersDao userDao;
    
    @Autowired
    Gson gson;
    
    private GenericResponse expectedResults(int responseCode, String responseMsg) {
        GenericResponse expected = new GenericResponse();
        expected.setResponseCode(responseCode);
        expected.setResponseMessage(responseMsg);
        return expected;
    }

    @BeforeEach
    public void setUp() {
        logger.info("Set Up");
    }

    @AfterEach
    public void tearDown() {
        logger.info("Tear Down");
    }

    @Test
    public void testAddUser() {
        logger.info("Starting to test AddUser");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilBirthDate = sdf.parse("1997-03-14");
            java.sql.Date birthDate = new java.sql.Date(utilBirthDate.getTime());
            
            GenericResponse[] expected = new GenericResponse[] {
                    expectedResults(409, "Username already exists"),
                    expectedResults(201, "User added successfully")
            };
            UsersBody user = new UsersBody("ron", "Ron Andre", "Mitra", "Libit", "libitrm@gmail.com", birthDate, 1, 1);
            GenericResponse response = userDao.addNewUser(user);

            boolean expectedResult = false;
            for (GenericResponse exp : expected) {
                if (response.getResponseCode() == exp.getResponseCode() &&
                        response.getResponseMessage().equals(exp.getResponseMessage())) {
                    expectedResult = true;
                    break;
                }
            }
            assertTrue(expectedResult, "Expected AddUser response found in the actual result");

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
    
    @Test
	public void TestGetAllUsers() {
    	logger.info("Starting to test GetAllUsers");
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilBirthDate = sdf.parse("1997-03-14");
            java.sql.Date birthDate = new java.sql.Date(utilBirthDate.getTime());
			Users expected = new Users("ron", "Libit, Ron Andre M.", "libitrm@gmail.com", "Male", birthDate, 27, "Backend Developer");
			
			List<Users> resultList = userDao.getAllUsers();
			String expectedResult = gson.toJson(expected);
			assertTrue(resultList.stream().map(gson::toJson).anyMatch(expectedResult::equals),
					 "Expected user not found in the list of actual.");
		}catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}
    
    @Test
	public void TestGetUser() {
    	logger.info("Starting to test GetUser");
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilBirthDate = sdf.parse("1997-03-14");
            java.sql.Date birthDate = new java.sql.Date(utilBirthDate.getTime());
			Users expected = new Users("ron", "Libit, Ron Andre M.", "libitrm@gmail.com", "Male", birthDate, 27, "Backend Developer");
			Users actual = userDao.getUser("ron");
		
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}
    
    @Test
    public void testUpdateUser() {
        logger.info("Starting to test UpdateUser");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilBirthDate = sdf.parse("1997-03-14");
            java.sql.Date birthDate = new java.sql.Date(utilBirthDate.getTime());
            
            GenericResponse[] expected = new GenericResponse[] {
                    expectedResults(409, "Username does not exists"),
                    expectedResults(201, "User updated successfully")
            };
            UsersBody user = new UsersBody("ron", "Ron Andre", "Mitra", "Libit", "libitrm@gmail.com", birthDate, 1, 1);
            GenericResponse response = userDao.updateUser(user);

            boolean expectedResult = false;
            for (GenericResponse exp : expected) {
                if (response.getResponseCode() == exp.getResponseCode() &&
                        response.getResponseMessage().equals(exp.getResponseMessage())) {
                    expectedResult = true;
                    break;
                }
            }
            assertTrue(expectedResult, "Expected UpdateUser response found in the actual result");

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Test
    public void testDeleteUser() {
        logger.info("Starting to test DeleteUser");
        try {
        	GenericResponse[] expected = new GenericResponse[] {
                    expectedResults(409, "User does not exists"),
                    expectedResults(201, "User deleted successfully")
            };
            UsersBody user = new UsersBody("ron1", null, null, null, null, null, 0, 0);
            GenericResponse response = userDao.deleteUser(user);

            boolean expectedResult = false;
            for (GenericResponse exp : expected) {
                if (response.getResponseCode() == exp.getResponseCode() &&
                        response.getResponseMessage().equals(exp.getResponseMessage())) {
                    expectedResult = true;
                    break;
                }
            }
            assertTrue(expectedResult, "Expected DeleteUser response found in the actual result");

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
    

	


}

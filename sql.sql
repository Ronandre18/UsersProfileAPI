--MSSQL DATABASE
USE MASTER
GO
IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'UsersProfile')
BEGIN
 CREATE DATABASE UsersProfile
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE NAME='Genders')
BEGIN 
   CREATE TABLE Genders
   (
    genderID INT PRIMARY KEY IDENTITY(1,1), 
	genderName VARCHAR(55) UNIQUE NOT NULL
   );

   	INSERT INTO GENDERS
	VALUES('Male'), ('Female')
END
GO	

IF NOT EXISTS (SELECT * FROM sys.tables WHERE NAME='Roles')
BEGIN 
   CREATE TABLE Roles
   (
    roleID INT PRIMARY KEY IDENTITY(1,1), 
	roleName VARCHAR(255) UNIQUE NOT NULL
   );

   	INSERT INTO Roles
	VALUES('Backend Developer'), ('Frontend Developer'), ('FullStack Developer')
END
GO


IF NOT EXISTS (SELECT * FROM sys.tables WHERE NAME='Users')
BEGIN 
   CREATE TABLE Users
   (
    userName VARCHAR(55) PRIMARY KEY NOT NULL,
	[password] NVARCHAR(MAX) NOT NULL,
    firstName VARCHAR(55) NOT NULL,
	middleName VARCHAR(55) NOT NULL,
	lastName VARCHAR(55) NOT NULL,
	emailAddress VARCHAR(255) NOT NULL,
	birthDate DATE NOT NULL,
	age INT NOT NULL,
	genderID INT NOT NULL,
	roleID INT NOT NULL,
	FOREIGN KEY (genderID) REFERENCES Genders(genderID),
	FOREIGN KEY (roleID) REFERENCES Roles(roleID)
   )
END
GO

CREATE OR ALTER PROCEDURE sp_addUsers
(@userName VARCHAR(55),
 @firstName VARCHAR(55),
 @middleName VARCHAR(55),
 @lastName VARCHAR(55),
 @email VARCHAR(255),
 @birthDate DATE,
 @genderId INT, 
 @roleId INT)
AS
BEGIN
 DECLARE @password NVARCHAR(255) = @username + CAST(@birthdate AS VARCHAR(55))
 DECLARE @encryptedPassword VARBINARY(64)
 SET @encryptedPassword = HASHBYTES('SHA2_256', @password)
 DECLARE @count INT, @age INT, @sampleAge INT
 DECLARE @currentDate DATE = CAST(GETDATE() AS DATE)
  SELECT @count = COUNT(*) FROM Users WHERE userName = @userName
 IF @count > 0 
  BEGIN
   SELECT 409 responseCode, 'Username already exists' responseMsg
  END
 ELSE
  BEGIN
   SELECT @sampleAge = DATEDIFF(YEAR, @birthDate, @currentDate)
   SELECT @age = @sampleAge - CASE WHEN DATEADD(YEAR, @sampleAge, @birthDate) > @currentDate THEN 1 ELSE 0 END 

   INSERT INTO Users
   VALUES(@userName, @encryptedPassword, @firstName, @middleName, @lastName, @email, @birthDate, @age, @genderid, @roleId)

   SELECT 201 responseCode, 'User added successfully' responseMsg
  END
END
GO

CREATE OR ALTER PROCEDURE sp_GetUsersInfo(@userName VARCHAR(55))
AS
BEGIN
 IF @userName IS NOT NULL
  BEGIN
	 SELECT
	  userName,
	  CASE 
	   WHEN middleName = '' THEN lastName + ', ' + firstName 
	  ELSE
	   lastName + ', ' + firstName + ' ' + LEFT(middleName, 1) + '.'
	  END fullName,
	  emailAddress,
	  genderName,
	  birthDate,
	  age,
	  rolename
	 FROM Users u
	 INNER JOIN Genders g ON g.genderID = u.genderID
	 INNER JOIN Roles r ON r.roleID = u.roleID
	 WHERE userName = @userName
  END
 ELSE
  BEGIN
	SELECT
	 userName,
	  CASE 
	   WHEN middleName = '' THEN lastName + ', ' + firstName 
	  ELSE
	   lastName + ', ' + firstName + ' ' + LEFT(middleName, 1) + '.'
	  END fullName,
	  emailAddress,
	  genderName,
	  birthDate,
	  age,
	  rolename
	 FROM Users u
	 INNER JOIN Genders g ON g.genderID = u.genderID
	 INNER JOIN Roles r ON r.roleID = u.roleID
  END
END
GO

CREATE OR ALTER PROCEDURE sp_UpdateUser
(@userName VARCHAR(55),
 @firstName VARCHAR(55),
 @middleName VARCHAR(55),
 @lastName VARCHAR(55),
 @email VARCHAR(255),
 @birthDate DATE,
 @genderId INT, 
 @roleId INT)
AS
BEGIN
 DECLARE @count INT
  SELECT @count = COUNT(*) FROM Users WHERE userName = @userName

 IF @count = 0
  BEGIN
   SELECT 409 responseCode, 'Username does not exists' responseMsg 
  END
 ELSE
  BEGIN
   DECLARE @currentBirthdate DATE
    SELECT @currentBirthdate = birthdate FROM Users WHERE userName = @userName
   UPDATE Users SET
    firstName = @firstName,
	middleName = @middleName,
	lastName = @lastName,
	emailAddress = @email,
	birthDate = @birthDate,
	genderId = @genderId,
	roleID = @roleid
	WHERE userName = @userName

	SELECT 201 responseCode, 'User updated successfully' responseMsg 
  END
END
GO
CREATE OR ALTER TRIGGER trg_UpdateUserInfo
ON USERS
AFTER UPDATE
AS
 DECLARE @age INT, @sampleAge INT, @birthDate DATE
 DECLARE @password VARCHAR(255), @encryptedPassword VARBINARY(64)
 DECLARE @username VARCHAR(55)
  SELECT @username = deleted.username, @birthDate = inserted.birthDate FROM deleted
  INNER JOIN inserted ON inserted.userName = deleted.userName

 DECLARE @currentDate DATE = CAST(GETDATE() AS DATE)
 SELECT @sampleAge = DATEDIFF(YEAR, @birthDate, @currentDate)
   SELECT @age = @sampleAge - CASE WHEN DATEADD(YEAR, @sampleAge, @birthDate) > @currentDate THEN 1 ELSE 0 END 


 SET @password = @username + CAST(@birthdate AS VARCHAR(55))
 SET @encryptedPassword = HASHBYTES('SHA2_256', @password)
 UPDATE Users SET age = @age, [password] = @encryptedPassword WHERE userName = @username
GO

CREATE OR ALTER PROCEDURE sp_DeleteUser(@username VARCHAR(55))
AS
BEGIN
 DECLARE @count INT
  SELECT @count = COUNT(*) FROM Users WHERE userName = @username
 IF @count = 0
  BEGIN
   SELECT 409 responseCode, 'User does not exists' responseMsg 
  END
 ELSE
  BEGIN
   DELETE FROM Users WHERE userName = @username

   SELECT 201 responseCode, 'User deleted successfully' responseMsg  
  END
END
GO

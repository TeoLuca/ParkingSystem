package ro.bd.parkingmanagement.DAO;

import java.util.List;

import javax.swing.JTable;

import ro.bd.parkingmanagement.model.User;

public interface UserDAO {
	User insertUser(User user);

	User getUser(int id);

	User updateUser(int id, User user);

	User getUserByEmail(String email);

	User checkCredentials(String username, String password);

	boolean isAdmin(String username);

	boolean isUsernameInDB(String username);

	boolean isEmailInDB(String email);

	List<User> getAll();

	boolean deleteUser(int id);

	JTable populateJTableFromDB();
}

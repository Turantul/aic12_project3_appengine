package aic12.project3.dao;

import java.util.List;

import aic12.project3.dto.UserDTO;
import static aic12.project3.dao.OfyService.ofy;

public class GoogleUserDAO implements IUserDAO{

	
	public GoogleUserDAO(){
	}
	
	public void storeUser(UserDTO user) {
		ofy().save().entity(user).now();
	}

	public void storeUser(List<UserDTO> users) {
		ofy().save().entities(users).now();		
	}

	public UserDTO searchUser(String userName) {
		return ofy().load().type(UserDTO.class).filter("companyName", userName).first().get();
	}

	public List<UserDTO> getAllUser() {
		List<UserDTO> userList = ofy().load().type(UserDTO.class).list();
		return userList;
	}

}

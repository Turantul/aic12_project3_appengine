package aic12.project3.dao;

import aic12.project3.dto.UserDTO;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {

	static{
		factory().register(UserDTO.class);
	}
	
	public static Objectify ofy(){
		return ObjectifyService.ofy();
	}
	
	public static ObjectifyFactory factory(){
		return ObjectifyService.factory();
	}
}

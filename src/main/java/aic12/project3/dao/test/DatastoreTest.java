package aic12.project3.dao.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aic12.project3.dao.GoogleUserDAO;
import aic12.project3.dto.UserDTO;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DatastoreTest {

	private GoogleUserDAO userdao = new GoogleUserDAO();
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }


	
	@Test
	public void testStoreUser(){
		UserDTO u = new UserDTO();
		u.setCompanyName("test");
		userdao.storeUser(u);
		List<UserDTO> ulist = userdao.getAllUser();
		assertTrue(ulist.size()==1);
		assertTrue(ulist.contains(u));
	}
	
	@Test
	public void testStoreMultipleUser(){
		UserDTO u = new UserDTO();
		u.setCompanyName("test");
		UserDTO u2 = new UserDTO();
		u.setCompanyName("test2");
		List<UserDTO> ulist = new ArrayList<UserDTO>();
		ulist.add(u);
		ulist.add(u2);
		userdao.storeUser(ulist);
		List<UserDTO> retList = userdao.getAllUser();
		assertTrue(ulist.size()==2);
		assertTrue(ulist.contains(u));
		assertTrue(ulist.contains(u2));
	}
	
	@Test
	public void testSearchUser(){
		UserDTO u = new UserDTO();
		u.setCompanyName("test");
		userdao.storeUser(u);
		UserDTO u2 = new UserDTO();
		u2.setCompanyName("test2");
		userdao.storeUser(u2);
		List<UserDTO> ulist = new ArrayList<UserDTO>();
		ulist.add(u);
		ulist.add(u2);
		
		UserDTO u3 = userdao.searchUser("test2");
		assertEquals(u3, u2);
	}
}

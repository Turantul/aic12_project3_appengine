package aic12.project3.dao;

import static aic12.project3.dao.OfyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import aic12.project3.dto.SentimentRequestDTO;

public class GoogleRequestDAO implements IRequestDAO {


	public static GoogleRequestDAO instance = new GoogleRequestDAO();


	private GoogleRequestDAO(){
	}

	@Override
	public Key<SentimentRequestDTO> saveRequest(SentimentRequestDTO request) {
		return ofy().save().entity(request).now();
	}

	@Override
	public List<SentimentRequestDTO> getAllRequestForCompany(String company) {
		return ofy().load().type(SentimentRequestDTO.class).filter("companyName =", company).list();
	}

	@Override
	public SentimentRequestDTO getRequest(String id) {
		return ofy().load().type(SentimentRequestDTO.class).filterKey(Key.create(id)).first().get();
	}

    @Override
    public List<SentimentRequestDTO> getAllRequests()
    {
    	return ofy().load().type(SentimentRequestDTO.class).list();
    }
}
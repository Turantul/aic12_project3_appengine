package aic12.project3.dao;

import static aic12.project3.dao.OfyService.ofy;

import java.util.List;


import aic12.project3.dto.SentimentRequest;
import aic12.project3.dto.TweetDTO;

public class GoogleRequestDAO implements IRequestDAO {


	private static GoogleRequestDAO instance = new GoogleRequestDAO();


	private GoogleRequestDAO(){
	}

	@Override
	public void saveRequest(SentimentRequest request) {
		ofy().save().entity(request).now();
	}

	@Override
	public List<SentimentRequest> getAllRequestForCompany(String company) {
		return ofy().load().type(SentimentRequest.class).filter("companyName =", company).list();
	}

	@Override
	public SentimentRequest getRequest(String id) {
		return ofy().load().type(SentimentRequest.class).filter("id =", id).first().get();
	}

    @Override
    public List<SentimentRequest> getAllRequests()
    {
    	return ofy().load().type(SentimentRequest.class).list();
    }
}
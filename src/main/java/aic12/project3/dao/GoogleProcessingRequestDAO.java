package aic12.project3.dao;

import static aic12.project3.dao.OfyService.ofy;

import java.util.List;

import aic12.project3.dto.SentimentProcessingRequestDTO;

public class GoogleProcessingRequestDAO implements IProcessingRequestDAO {

	public static GoogleProcessingRequestDAO instance = new GoogleProcessingRequestDAO();

	private GoogleProcessingRequestDAO(){
	}

    @Override
    public void saveRequest(SentimentProcessingRequestDTO request)
    {
        ofy().save().entity(request).now();
    }

    @Override
    public List<SentimentProcessingRequestDTO> getAllSentimentProcessingRequest()
    {
        return ofy().load().type(SentimentProcessingRequestDTO.class).list();
    }

    @Override
    public List<SentimentProcessingRequestDTO> getAllSentimentRequestForRequest(String key)
    {
        return ofy().load().type(SentimentProcessingRequestDTO.class).filter("parentID", key).list();
    }

    @Override
    public SentimentProcessingRequestDTO getRequest(String id)
    {
        return ofy().load().type(SentimentProcessingRequestDTO.class).id(id).get();
    }
}

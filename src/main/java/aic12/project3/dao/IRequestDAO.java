package aic12.project3.dao;

import java.util.List;

import com.googlecode.objectify.Key;

import aic12.project3.dto.SentimentRequestDTO;

public interface IRequestDAO
{
    /**
     * Saves a Sentiment Request
     * 
     * @param request SentimentRequest
     * @return 
     */
    Key<SentimentRequestDTO> saveRequest(SentimentRequestDTO request);

    List<SentimentRequestDTO> getAllRequestForCompany(String company);

    SentimentRequestDTO getRequest(String id);
    
    List<SentimentRequestDTO> getAllRequests();
}
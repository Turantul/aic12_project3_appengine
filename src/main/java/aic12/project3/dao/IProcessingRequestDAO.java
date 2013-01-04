package aic12.project3.dao;

import java.util.List;

import com.googlecode.objectify.Key;

import aic12.project3.dto.SentimentProcessingRequestDTO;
import aic12.project3.dto.SentimentRequestDTO;

public interface IProcessingRequestDAO
{
    void saveRequest(SentimentProcessingRequestDTO request);
    
    SentimentProcessingRequestDTO getRequest(String id);

    List<SentimentProcessingRequestDTO> getAllSentimentRequestForRequest(Key<SentimentRequestDTO> key);

    List<SentimentProcessingRequestDTO> getAllSentimentProcessingRequest();
}
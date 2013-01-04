package aic12.project3.dao;

import java.util.List;

import aic12.project3.dto.SentimentProcessingRequestDTO;

public interface IProcessingRequestDAO
{
    void saveRequest(SentimentProcessingRequestDTO request);
    
    SentimentProcessingRequestDTO getRequest(String id);

    List<SentimentProcessingRequestDTO> getAllSentimentProcessingRequest();

    List<SentimentProcessingRequestDTO> getAllSentimentRequestForRequest(String key);
}
package aic12.project3.dto;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SentimentProcessingRequestDTO
{
    @Id
    private String id;
    @Index
    private String parentID;
    private Long timestampStartOfAnalysis;
    private Long timestampDataFetched;
    private Long timestampAnalyzed;
    private double sentiment;
    private long numberOfTweets;

    public String getId()
    {
        return this.id;
    }

    public String getParentID()
    {
        return parentID;
    }

    public void setParentID(String parentID)
    {
        this.parentID = parentID;
    }

    public Long getTimestampStartOfAnalysis()
    {
        return timestampStartOfAnalysis;
    }

    public void setTimestampStartOfAnalysis(Long timestampStartOfAnalysis)
    {
        this.timestampStartOfAnalysis = timestampStartOfAnalysis;
    }

    public Long getTimestampDataFetched()
    {
        return timestampDataFetched;
    }

    public void setTimestampDataFetched(Long timestampDataFetched)
    {
        this.timestampDataFetched = timestampDataFetched;
    }

    public Long getTimestampAnalyzed()
    {
        return timestampAnalyzed;
    }

    public void setTimestampAnalyzed(Long timestampAnalyzed)
    {
        this.timestampAnalyzed = timestampAnalyzed;
    }

    public double getSentiment()
    {
        return sentiment;
    }

    public void setSentiment(double d)
    {
        this.sentiment = d;
    }

    public long getNumberOfTweets()
    {
        return numberOfTweets;
    }

    public void setNumberOfTweets(long numberOfTweets)
    {
        this.numberOfTweets = numberOfTweets;
    }
}

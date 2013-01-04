package aic12.project3.dto;

import java.util.UUID;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class SentimentProcessingRequestDTO
{
    @Id
    private String id;
    @Parent
    private Key<SentimentRequestDTO> parentID;
    private Long timestampStartOfAnalysis;
    private Long timestampDataFetched;
    private Long timestampAnalyzed;
    private float sentiment;
    private int numberOfTweets;
    private int offset;
    private int pagesize;

    public SentimentProcessingRequestDTO()
    {
        this.id = UUID.randomUUID().toString();
    }

    public String getId()
    {
        return this.id;
    }

    public Key<SentimentRequestDTO> getParentID()
    {
        return parentID;
    }

    public void setParentID(Key<SentimentRequestDTO> parentID)
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

    public float getSentiment()
    {
        return sentiment;
    }

    public void setSentiment(float sentiment)
    {
        this.sentiment = sentiment;
    }

    public int getNumberOfTweets()
    {
        return numberOfTweets;
    }

    public void setNumberOfTweets(int numberOfTweets)
    {
        this.numberOfTweets = numberOfTweets;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getPagesize()
    {
        return pagesize;
    }

    public void setPagesize(int pagesize)
    {
        this.pagesize = pagesize;
    }
}

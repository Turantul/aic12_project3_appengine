package aic12.project3.analysis.service;

import java.util.Date;
import java.util.UUID;

public class SentimentRequest
{
    private String id;
    private String companyName;
    private Date from;
    private Date to;
    private long timestampStartOfAnalysis;
    private long timestampDataFetched;
    private long timestampAnalyzed;
    private long timestampFinished;
    private float sentiment;
    private int numberOfTweets;

    public SentimentRequest()
    {
        this.id = UUID.randomUUID().toString();
    }

    public String getId()
    {
        return this.id;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public Date getFrom()
    {
        return from;
    }

    public void setFrom(Date from)
    {
        this.from = from;
    }

    public Date getTo()
    {
        return to;
    }

    public void setTo(Date to)
    {
        this.to = to;
    }

    public Long getTimestampStartOfAnalysis()
    {
        return timestampStartOfAnalysis;
    }

    public void setTimestampStartOfAnalysis(Long timestampStartOfAnalysis)
    {
        this.timestampStartOfAnalysis = timestampStartOfAnalysis;
    }

    public long getTimestampDataFetched()
    {
        return timestampDataFetched;
    }

    public void setTimestampDataFetched(long timestampDataFetched)
    {
        this.timestampDataFetched = timestampDataFetched;
    }

    public long getTimestampAnalyzed()
    {
        return timestampAnalyzed;
    }

    public void setTimestampAnalyzed(long timestampAnalyzed)
    {
        this.timestampAnalyzed = timestampAnalyzed;
    }
    
    public long getTimestampFinished()
    {
        return timestampFinished;
    }

    public void setTimestampFinished(long timestampFinished)
    {
        this.timestampFinished = timestampFinished;
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
}

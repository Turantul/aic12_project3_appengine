package aic12.project3.dto;

import java.util.Date;
import java.util.UUID;

import com.googlecode.objectify.annotation.Index;

/**
 * Sub-processing part of Sentiment Request
 * 
 * @author johannes
 * 
 */

public class SentimentProcessingRequest {
	
	private String id;
	private String parentID;
    private String companyName;
    private Date from;
    private Date to;
    private Long timestampStartOfAnalysis;
    private Long timestampDataFetched;
    private Long timestampAnalyzed;
    private float sentiment;
    private int numberOfTweets;
    private int offset;
    private int pagesize;
    @Index
    private boolean taskFinished = false;
    
    public SentimentProcessingRequest(){
    	this.id = UUID.randomUUID().toString();
    }
    
    public String getId(){
    	return this.id;
    }

    /**
	 * @return the parentID
	 */
	public String getParentID() {
		return parentID;
	}

	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	/**
     * @return the companyName
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * @return the from
     */
    public Date getFrom()
    {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Date from)
    {
        this.from = from;
    }

    /**
     * @return the to
     */
    public Date getTo()
    {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(Date to)
    {
        this.to = to;
    }

    public Long getTimestampStartOfAnalysis() {
		return timestampStartOfAnalysis;
	}

	public void setTimestampStartOfAnalysis(Long timestampStartOfAnalysis) {
		this.timestampStartOfAnalysis = timestampStartOfAnalysis;
	}

	public Long getTimestampDataFetched() {
		return timestampDataFetched;
	}

	public void setTimestampDataFetched(Long timestampDataFetched) {
		this.timestampDataFetched = timestampDataFetched;
	}

	public Long getTimestampAnalyzed() {
		return timestampAnalyzed;
	}

	public void setTimestampAnalyzed(Long timestampAnalyzed) {
		this.timestampAnalyzed = timestampAnalyzed;
	}

	/**
     * @return the sentiment
     */
    public float getSentiment()
    {
        return sentiment;
    }

    /**
     * @param sentiment the sentiment to set
     */
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

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public boolean isTaskFinished() {
		return taskFinished;
	}

	public void setTaskFinished(boolean taskFinished) {
		this.taskFinished = taskFinished;
	}
    
}

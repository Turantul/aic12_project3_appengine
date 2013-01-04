package aic12.project3.dto;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SentimentRequestDTO
{
	@Id
    private String id;
	@Index
    private String companyName;
	@Index
    private Date from;
	@Index
    private Date to;

	private long timestampRequestSending;
    private long timestampRequestFinished;

    private int numberOfParts;


    public SentimentRequestDTO() { }
    
    public SentimentRequestDTO(String id) {
    	this.id = id;
	}

    public String toString(){
    	return this.getCompanyName() + " - from: " + this.getFrom() + " to: " + this.getTo() + " with ID: " + this.getId(); 
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SentimentRequestDTO other = (SentimentRequestDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}
	
	public long getTimestampRequestSending() {
		return timestampRequestSending;
	}

	public void setTimestampRequestSending(long timestampRequestSending) {
		this.timestampRequestSending = timestampRequestSending;
	}

	public long getTimestampRequestFinished() {
		return timestampRequestFinished;
	}

	public void setTimestampRequestFinished(long timestampRequestFinished) {
		this.timestampRequestFinished = timestampRequestFinished;
	}

	public void setParts(int i){
		this.numberOfParts = i;
	}
	
	public int getParts(){
		return this.numberOfParts;
	}
}

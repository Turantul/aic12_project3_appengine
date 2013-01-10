package aic12.project3.dto;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class TweetDTO
{
	@Id
    private String twitterId;
    private String text;
    @Index
    private Date date;
    @Index
    private String companies;
    
    public TweetDTO() {}
    
    public TweetDTO(String twitterId, String text, Date date)
    {
        this.twitterId = twitterId;
        this.text = text;
        this.date = date;
    }

    public String getTwitterId()
    {
        return twitterId;
    }

    public void setTwitterId(String twitterId)
    {
        this.twitterId = twitterId;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
	
	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}
}

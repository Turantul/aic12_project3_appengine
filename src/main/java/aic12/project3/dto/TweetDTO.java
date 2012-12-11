package aic12.project3.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@Entity
public class TweetDTO
{
	@Id
    private String twitterId;
    private String text;
    @Index
    private Date date;
    private Integer sentiment;
    @Embed
    @Index
    private List<String> companies = new ArrayList<String>();
    
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

    public Integer getSentiment()
    {
        return sentiment;
    }

    public void setSentiment(Integer sentiment)
    {
        this.sentiment = sentiment;
    }
	
	public List<String> getCompanies() {
		return companies;
	}

	public void setCompanies(List<String> companies) {
		this.companies = companies;
	}

	@Override
	public String toString(){
		return "TweetDTO[tweetId="+twitterId+", date="+date+", companies="+companies+"]";
	}
    
}

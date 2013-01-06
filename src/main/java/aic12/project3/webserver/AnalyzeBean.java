package aic12.project3.webserver;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import aic12.project3.dao.GoogleProcessingRequestDAO;
import aic12.project3.dao.GoogleRequestDAO;
import aic12.project3.dao.GoogleTweetDAO;
import aic12.project3.dto.SentimentProcessingRequestDTO;
import aic12.project3.dto.SentimentRequestDTO;
import aic12.project3.dto.TweetDTO;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;

@ManagedBean
@SessionScoped
public class AnalyzeBean implements Serializable
{
    private String companyName;
    private int parts;
    private String key;
    
    // Product should be 1000
    public static int pagesize = 10;
    private static int multiplicator = 100;

    public String analyze() throws Exception
    {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        SentimentRequestDTO request = new SentimentRequestDTO();

        request.setCompanyName(companyName);
        request.setFrom(new Date(System.currentTimeMillis() - 100000000));
        request.setTo(new Date());
        request.setTimestampRequestSending(System.currentTimeMillis());
        
        long amount = (long) ((Math.ceil(new GoogleTweetDAO().countTweet(request.getCompanyName(), request.getFrom(), request.getTo()) / multiplicator) + 1) * multiplicator);
        
        /*if (amount < 10000)
        {
            fetchTweets(companyName);
            Thread.sleep(1000);
            
            amount = new GoogleTweetDAO().countTweet(request.getCompanyName(), request.getFrom(), request.getTo());
        }*/
        
        parts = (int) Math.ceil((float) amount / pagesize / multiplicator);
        
        request.setParts(parts);

        Key<SentimentRequestDTO> key = GoogleRequestDAO.instance.saveRequest(request);
        this.key = key.getString();

        Queue queue = QueueFactory.getQueue("Analysis");
        for (int i = 0; i < parts; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("request", key.getString()).param("offset", Integer.toString((i) * pagesize * multiplicator)).param("pagesize", Integer.toString(pagesize)).param("multiplicator", Integer.toString(multiplicator)));
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("key", this.key);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("amount", amount);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("parts", parts);
        
        return "result.xhtml";
    }
    
    public void poll()
    {
        int parts = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("parts").toString());
        String key = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("key").toString();
        
        String result;
        
        int count = GoogleProcessingRequestDAO.instance.getCountSentimentRequestForRequest(key);
        if (count < parts)
        {
            result = "Calculating: " + count + "/" + parts;
        }
        else
        {
            List<SentimentProcessingRequestDTO> list = GoogleProcessingRequestDAO.instance.getAllSentimentRequestForRequest(key);

            double sentiment = 0;
            long amount = 0;
            for (SentimentProcessingRequestDTO pro : list)
            {
                amount += pro.getNumberOfTweets();
                sentiment += pro.getSentiment() * pro.getNumberOfTweets();
            }

            sentiment /= amount;

            double interval = 1.96 * Math.sqrt(sentiment * (1 - sentiment) / (amount - 1));

            result = "Amount: " + amount + " - Sentiment: (" + (sentiment - interval) + " < " + sentiment + " < " + (sentiment + interval) + ")";
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("result", result);
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    @SuppressWarnings("unused")
    private void fetchTweets(String company) throws TwitterException
    {
        Twitter twitter = new TwitterFactory().getInstance();

        Query query = new Query(company);
        query.setLang("en");
        query.setRpp(100);
        
        List<String> companies = new ArrayList<String>(1);
        companies.add(company);
        
        List<TweetDTO> tweets = new ArrayList<TweetDTO>();
        
        for (int i = 1; i < 10; i++)
        {
            query.setPage(i);
            QueryResult result = twitter.search(query);
            for (Tweet tweet : result.getTweets())
            {
                TweetDTO dto = new TweetDTO();
                dto.setTwitterId(Long.toString(tweet.getId()));
                dto.setText(tweet.getText());
                dto.setDate(tweet.getCreatedAt());
                dto.setCompanies(companies);
                tweets.add(dto);
            }
        }

        new GoogleTweetDAO().storeTweet(tweets);
    }

    public int getParts()
    {
        return parts;
    }

    public void setParts(int parts)
    {
        this.parts = parts;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}

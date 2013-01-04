package aic12.project3.webserver;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

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
    private String result;
    private int parts;
    private String key;
    
    public static int pagesize = 1000;

    public String analyze() throws Exception
    {
        SentimentRequestDTO request = new SentimentRequestDTO();

        request.setCompanyName(companyName);
        request.setFrom(new Date(System.currentTimeMillis() - 100000000));
        request.setTo(new Date());
        request.setTimestampRequestSending(System.currentTimeMillis());
        
        Long amount = new GoogleTweetDAO().countTweet(request.getCompanyName(), request.getFrom(), request.getTo());
        
        if (amount < 10000)
        {
            fetchTweets(companyName);
            amount = new GoogleTweetDAO().countTweet(request.getCompanyName(), request.getFrom(), request.getTo());
        }
        
        parts = (int) Math.ceil(amount / pagesize) + 1;
        
        request.setParts(parts);

        Key<SentimentRequestDTO> key = GoogleRequestDAO.instance.saveRequest(request);
        this.key = key.getString();

        Queue queue = QueueFactory.getQueue("Analysis");
        for (int i = 0; i <= parts; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("request", key.getString()).param("offset", Integer.toString((i) * pagesize)).param("pagesize", Integer.toString(pagesize)));
        }
        
        return "result.xhtml";
    }
    
    public void poll()
    {
        int count = GoogleProcessingRequestDAO.instance.getCountSentimentRequestForRequest(key);
        if (count < parts)
        {
            result = "Calculating: " + count + "/" + parts;
        }
        else
        {
            List<SentimentProcessingRequestDTO> list = GoogleProcessingRequestDAO.instance.getAllSentimentRequestForRequest(key);
            
            float sentiment = 0;
            int amount = 0;
            for (SentimentProcessingRequestDTO pro : list)
            {
                amount += pro.getNumberOfTweets();
                sentiment += pro.getSentiment() * pro.getNumberOfTweets();
            }
            
            sentiment /= amount;
    
            double interval = 1.96 * Math.sqrt(sentiment * (1 - sentiment) / (amount - 1));
            
            result = "Amount: " + amount + " - Sentiment: (" + (sentiment - interval) + " < " + sentiment + " < " + (sentiment + interval) + ")";
            
            RequestContext reqCtx = RequestContext.getCurrentInstance();
            reqCtx.execute("poll.stop();");
        }
        result += "/Parts: " + parts + " Count: " + count;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getResult()
    {
        return result;
    }
    
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
}

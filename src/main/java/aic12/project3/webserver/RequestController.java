package aic12.project3.webserver;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

@ManagedBean
@SessionScoped
public class RequestController implements Serializable
{
    private String companyName;
    private Date from;
    private Date to;

    private int parts;
    private String key;

    public static int pagesize = 10;
    private static int multiplicator = 100;

    public String sendToAnalysis()
    {
        companyName = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyName").toString();

        SentimentRequestDTO request = new SentimentRequestDTO();
        
        key = UUID.randomUUID().toString();
        request.setId(key);

        request.setCompanyName(companyName);
        request.setFrom(from);
        request.setTo(to);
        request.setTimestampRequestSending(System.currentTimeMillis());

        long amount = (long) ((Math.ceil(new GoogleTweetDAO().countTweet(request.getCompanyName(), request.getFrom(), request.getTo()) / multiplicator) + 1) * multiplicator);

        /*if (amount < 10000)
        {
            try
            {
                fetchTweets(companyName);
                Thread.sleep(1000);
            }
            catch (TwitterException e)
            {}
            catch (InterruptedException e)
            {}

            amount = (long) ((Math.ceil(new GoogleTweetDAO().countTweet(request.getCompanyName(), request.getFrom(), request.getTo()) / multiplicator) + 1) * multiplicator);
        }*/

        parts = (int) Math.ceil((float) amount / multiplicator);

        request.setParts(parts);

        GoogleRequestDAO.instance.saveRequest(request);

        Queue queue = QueueFactory.getQueue("Analysis");
        for (int i = 0; i < parts; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("request", key).param("offset", Integer.toString((i) * pagesize))
                    .param("pagesize", Integer.toString(pagesize)).param("multiplicator", Integer.toString(multiplicator)));
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("key", key);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("parts", parts);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("amount", amount);

        String result = "Found " + amount + " tweets. Splitting work into " + parts + " tasks. Please poll for results.";

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("result", result);
        
        return "requestproc.xhtml";
    }

    public void getResponseFromDB()
    {
        int parts = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("parts").toString());
        String key = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("key").toString();

        String result = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("result").toString();

        int count = GoogleProcessingRequestDAO.instance.getCountSentimentRequestForRequest(key);
        if (count < parts)
        {
            result += "\nCalculating: " + count + "/" + parts;
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

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("numberOfTweets", FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("amount"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("minimumSentiment", sentiment - interval);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("maximumSentiment", sentiment + interval);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sentiment", sentiment);
            
            SentimentRequestDTO request = GoogleRequestDAO.instance.getRequest(key);
            request.setTimestampRequestFinished(System.currentTimeMillis());
            GoogleRequestDAO.instance.saveRequest(request);

            result += "\nAnalysis finished";
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("result", result);
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
}
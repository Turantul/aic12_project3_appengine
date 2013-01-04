package aic12.project3.webserver;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import aic12.project3.dao.GoogleProcessingRequestDAO;
import aic12.project3.dao.GoogleRequestDAO;
import aic12.project3.dto.SentimentProcessingRequestDTO;
import aic12.project3.dto.SentimentRequestDTO;

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

    public String analyze() throws Exception
    {
        SentimentRequestDTO request = new SentimentRequestDTO();

        request.setCompanyName(companyName);
        request.setFrom(new Date());
        request.setTo(new Date());
        request.setTimestampRequestSending(System.currentTimeMillis());
        
        //TODO: set correctly
        request.setParts(10);
        parts = 10;

        Key<SentimentRequestDTO> key = GoogleRequestDAO.instance.saveRequest(request);
        this.key = key.getString();

        //TODO: enable a reasonable splitting mechanism based on tweet amount
        Queue queue = QueueFactory.getDefaultQueue();
        for (int i = 0; i < 10; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("request", key.getString()));
        }
        
        return "result.xhtml";
    }
    
    public void poll()
    {
        List<SentimentProcessingRequestDTO> list = GoogleProcessingRequestDAO.instance.getAllSentimentRequestForRequest(key);
        if (list.size() < parts)
        {
            result = "Calculating: " + list.size() + "/" + parts;
        }
        else
        {
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
        }
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
}

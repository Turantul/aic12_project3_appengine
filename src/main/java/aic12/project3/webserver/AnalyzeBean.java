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

    public String analyze() throws Exception
    {
        SentimentRequestDTO request = new SentimentRequestDTO();

        request.setCompanyName(companyName);
        request.setFrom(new Date());
        request.setTo(new Date());
        request.setTimestampRequestSending(System.currentTimeMillis());
        request.setParts(10);

        Key<SentimentRequestDTO> key = GoogleRequestDAO.instance.saveRequest(request);

        Queue queue = QueueFactory.getDefaultQueue();

        for (int i = 0; i < 10; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("request", key.getString()));
        }
        
        List<SentimentProcessingRequestDTO> list;
        do
        {
            Thread.sleep(5000);
            list = GoogleProcessingRequestDAO.instance.getAllSentimentRequestForRequest(key.getString());
            System.out.println(list.size());
        } while (list.size() < 10);
        
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
        return "result.xhtml";
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

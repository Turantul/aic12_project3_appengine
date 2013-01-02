package aic12.project3.webserver;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import aic12.project3.dao.GoogleRequestDAO;
import aic12.project3.dto.SentimentRequest;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@ManagedBean
@SessionScoped
public class AnalyzeBean implements Serializable
{
    private String companyName;
    private String result;

    public String analyze() throws Exception
    {
        SentimentRequest request = new SentimentRequest(UUID.randomUUID().toString());

        request.setCompanyName(companyName);
        // TODO: set From and To

        GoogleRequestDAO.instance.saveRequest(request);
        
        Queue queue = QueueFactory.getDefaultQueue();

        for (int i = 0; i < 10; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("request", request.getId()));
        }
        
        Thread.sleep(20000);
        
        request = GoogleRequestDAO.instance.getRequest(request.getId());

        // double interval = 1.96 * Math.sqrt(req.getSentiment() * (1 -
        // req.getSentiment()) / (req.getNumberOfTweets() - 1));
        //
        // result = "Amount: " + req.getNumberOfTweets() + " - Sentiment: (" +
        // (req.getSentiment() - interval) + " < " + req.getSentiment() + " < "
        // + (req.getSentiment() + interval)
        // + ")";
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

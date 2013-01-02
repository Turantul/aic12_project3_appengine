package aic12.project3.webserver;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
        Queue queue = QueueFactory.getDefaultQueue();

        for (int i = 0; i < 10; i++)
        {
            queue.add(withUrl("/tasks/analysis").method(TaskOptions.Method.GET).param("company", companyName));
        }

//        double interval = 1.96 * Math.sqrt(req.getSentiment() * (1 - req.getSentiment()) / (req.getNumberOfTweets() - 1));
//
//        result = "Amount: " + req.getNumberOfTweets() + " - Sentiment: (" + (req.getSentiment() - interval) + " < " + req.getSentiment() + " < " + (req.getSentiment() + interval)
//                + ")";
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

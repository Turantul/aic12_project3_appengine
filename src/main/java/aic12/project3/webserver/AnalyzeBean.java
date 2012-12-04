package aic12.project3.webserver;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import aic12.project3.analysis.service.SentimentRequest;
import aic12.project3.analysis.service.SentimentService;

import java.io.Serializable;

@ManagedBean
@SessionScoped
public class AnalyzeBean implements Serializable
{
    private String companyName;
    private String result;

    public String analyze() throws Exception
    {
        SentimentRequest req = new SentimentRequest();
        req.setCompanyName(companyName);

        SentimentService service = new SentimentService();
        service.analyze(req);

        double interval = 1.96 * Math.sqrt(req.getSentiment() * (1 - req.getSentiment()) / (req.getNumberOfTweets() - 1));

        result = "Amount: " + req.getNumberOfTweets() + " - Sentiment: (" + (req.getSentiment() - interval) + " < " + req.getSentiment() + " < " + (req.getSentiment() + interval)
                + ")";

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

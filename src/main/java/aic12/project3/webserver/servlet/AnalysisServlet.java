package aic12.project3.webserver.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import aic12.project3.dao.GoogleRequestDAO;
import aic12.project3.dto.SentimentProcessingRequest;
import aic12.project3.dto.SentimentRequest;
import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.WeightedMajority;
import classifier.WekaClassifier;

public class AnalysisServlet extends HttpServlet
{
    private WeightedMajority wm;

    public AnalysisServlet() throws Exception
    {
        // Pre chaching of neural networks
        List<IClassifier> classifiers = new LinkedList<IClassifier>();
        ClassifierBuilder cb = new ClassifierBuilder();
        WekaClassifier wc1 = cb.retrieveClassifier("weka.classifiers.bayes.NaiveBayes");
        WekaClassifier wc2 = cb.retrieveClassifier("weka.classifiers.trees.J48");
        WekaClassifier wc3 = cb.retrieveClassifier("wlsvm.WLSVM");
        classifiers.add(wc1);
        classifiers.add(wc2);
        classifiers.add(wc3);
        wm = new WeightedMajority(classifiers);

        // Running test classification for further caching
        wm.weightedClassify("test");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        SentimentRequest request = GoogleRequestDAO.instance.getRequest(req.getParameter("request"));

        // TODO: query database for tweets
        // so far Twitter4J is used
        try
        {
            List<Tweet> tweets = new ArrayList<Tweet>(1000);
            
            Twitter twitter = new TwitterFactory().getInstance();

            Query query = new Query(request.getCompanyName());
            query.setLang("en");
            query.setRpp(10);

            for (int i = 1; i < 10; i++)
            {
                query.setPage(i);
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
            }

            calculateSentiment(request, tweets);
            
            resp.setStatus(200);
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    private void calculateSentiment(SentimentRequest request, List<Tweet> list) throws Exception
    {
        SentimentProcessingRequest proreq = new SentimentProcessingRequest();
        
        proreq.setTimestampDataFetched(System.currentTimeMillis());

        int i = 0;
        for (Tweet tweet : list)
        {
            i += wm.weightedClassify(tweet.getText()).getPolarity();
        }

        proreq.setSentiment((float) i / list.size() / 4);
        proreq.setNumberOfTweets(list.size());

        proreq.setTimestampAnalyzed(System.currentTimeMillis());
        
        request.getSubRequests().add(proreq);
        GoogleRequestDAO.instance.saveRequest(request);
    }
}

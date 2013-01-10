package aic12.project3.webserver.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.WeightedMajority;
import classifier.WekaClassifier;

public class AnalysisServlet extends HttpServlet
{
    private static final Logger log = Logger.getLogger(AnalysisServlet.class.getName());
    
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
        SentimentRequestDTO request;

        // Necessary because it sometimes is null
        do
        {
            request = GoogleRequestDAO.instance.getRequest(req.getParameter("request"));
        } while (request == null);

        SentimentProcessingRequestDTO proreq = new SentimentProcessingRequestDTO();
        proreq.setParentID(req.getParameter("request"));
        proreq.setTimestampStartOfAnalysis(System.currentTimeMillis());

        try
        {
            // fetchTweets(request.getCompanyName());

            List<TweetDTO> tweets = new GoogleTweetDAO().searchTweetOffsetLimit(request.getCompanyName(), request.getFrom(), request.getTo(), Integer.parseInt(req.getParameter("offset")), Integer.parseInt(req.getParameter("pagesize")));

            log.warning("Found " + tweets.size() + " tweets");
            
            calculateSentiment(proreq, tweets, Integer.parseInt(req.getParameter("multiplicator")));

            resp.setStatus(200);
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    private void calculateSentiment(SentimentProcessingRequestDTO proreq, List<TweetDTO> list, int multiplicator) throws Exception
    {
        proreq.setTimestampDataFetched(System.currentTimeMillis());

        if (list.size() > 0)
        {
            double i = 0;
            for (TweetDTO tweet : list)
            {
                for (int j = 0; j < multiplicator; j++)
                {
                    i += wm.weightedClassify(tweet.getText()).getPolarity();
                }
            }
    
            proreq.setSentiment(i / list.size() / multiplicator / 4);
        }
        else
        {
            proreq.setSentiment(0);
        }
        proreq.setNumberOfTweets(list.size() * multiplicator);

        proreq.setTimestampAnalyzed(System.currentTimeMillis());

        GoogleProcessingRequestDAO.instance.saveRequest(proreq);
    }
    
    @SuppressWarnings("unused")
    private void fetchTweets(String company)
    {
        Twitter twitter = new TwitterFactory().getInstance();

        Query query = new Query(company);
        query.setLang("en");
        query.setRpp(100);

        try
        {
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
                    dto.setCompanies(company);
                    tweets.add(dto);
                }
            }

            new GoogleTweetDAO().storeTweet(tweets);
        }
        catch (TwitterException e)
        {}
    }
}

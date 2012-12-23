package aic12.project3.webserver;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;


import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.Tweet;
import aic12.project3.dao.GoogleTweetDAO;
import aic12.project3.dto.TweetDTO;

import java.io.Serializable;
import java.util.Date;

@ManagedBean
@SessionScoped
public class IndexBean implements Serializable{

	private int pagesize = 100;
	private String companyName;
	
	private String result;
	
	public String index() throws Exception{
		GoogleTweetDAO dao = new GoogleTweetDAO();
		int numberOfTweetsDownloaded = downloadTweets(dao);
		
		Queue queue = QueueFactory.getDefaultQueue();
		
		Long numberAllTweets = dao.countAllTweet();		
		int numberTasks = numberAllTweets.intValue()/pagesize;
		for(int i=0; i<numberTasks;i++){
			queue.add(withUrl("/tasks/index").method(TaskOptions.Method.GET).param("company", companyName).param("offset", Integer.toString((i)*pagesize)).param("pagesize", Integer.toString(pagesize)));
		}
		
		//For testing
		Thread.sleep(10000);
		
		Long countIndexed = dao.countTweet(companyName, new Date(System.currentTimeMillis()-new Long(1209600000)), new Date(System.currentTimeMillis()));
		
		result = numberOfTweetsDownloaded+" Tweets downloaded and indexed for company "+companyName+" in "+numberTasks+" Tasks.\n"+
				"Number of Tweets with Company found: "+countIndexed;
		return "indexResult.xhtml";
	}
	
	public String getResult() {
		return result;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	private int downloadTweets(GoogleTweetDAO dao) throws Exception{
		int ret = 0;
    	Twitter twitter = new TwitterFactory().getInstance();

        Query query = new Query(this.companyName);
        query.setLang("en");
        query.setRpp(100);

        for (int i = 1; i < 10; i++)
        {
            query.setPage(i);
            QueryResult result = twitter.search(query);
            for(Tweet t : result.getTweets()){
            	TweetDTO tweet = new TweetDTO();
            	tweet.setTwitterId(Long.toString(t.getId()));
            	tweet.setDate(t.getCreatedAt());
            	tweet.setText(t.getText());
            	dao.storeTweet(tweet);
            	ret++;
            }
        }
        return ret;
    }
}
package aic12.project3.dao;

import static aic12.project3.dao.OfyService.ofy;

import java.util.Date;
import java.util.List;

import aic12.project3.dto.TweetDTO;

public class GoogleTweetDAO implements ITweetDAO{

	@Override
	public void storeTweet(TweetDTO tweet) {
		ofy().save().entity(tweet).now();
	}

	@Override
	public void storeTweet(List<TweetDTO> tweets) {
		ofy().save().entities(tweets).now();
	}

	@Override
	public List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate) {
		return ofy().load().type(TweetDTO.class).filter("date >=", fromDate).filter("date <=", toDate).filter("companies =", company).list();
	}

	@Override
	public Long countTweet(String company, Date fromDate, Date toDate) {
		return new Long(ofy().load().type(TweetDTO.class).filter("date >=", fromDate).filter("date <=", toDate).filter("companies =", company).count());
		
	}
	
	public Long countAllTweet() {
		return new Long(ofy().load().type(TweetDTO.class).count());
		
	}
	
	//Use for indexing
	public List<TweetDTO> getAllTweetsOffsetLimit(int offset, int pagesize){
		return ofy().load().type(TweetDTO.class).offset(offset).limit(pagesize).list();
	}
	
	//Use for sentiment analysis
	public List<TweetDTO> searchTweetOffsetLimit(String company, Date fromDate, Date toDate, int offset, int pagesize) {
		return ofy().load().type(TweetDTO.class).filter("date >=", fromDate).filter("date <=", toDate).filter("companies =", company).offset(offset).limit(pagesize).list();
	}

	@Override
	public List<TweetDTO> getAllTweet() {
		return ofy().load().type(TweetDTO.class).list();
	}

	@Override
	public void insertTweet(TweetDTO tList) {
		ofy().save().entities(tList).now();		
	}

	@Override
	public int indexCompany(String company) {
		// TODO Auto-generated method stub
		return 0;
	}

}

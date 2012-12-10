package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import aic12.project3.dto.TweetDTO;

public class GoogleTweetDAO implements ITweetDAO{

	@Override
	public void storeTweet(TweetDTO tweet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeTweet(List<TweetDTO> tweets) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countTweet(String company, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexCompany(String company) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TweetDTO> getAllTweet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertTweet(TweetDTO tList) {
		// TODO Auto-generated method stub
		
	}

}

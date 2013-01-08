package aic12.project3.webserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.googlecode.objectify.Key;

import aic12.project3.dao.GoogleProcessingRequestDAO;
import aic12.project3.dao.GoogleRequestDAO;
import aic12.project3.dto.SentimentProcessingRequestDTO;
import aic12.project3.dto.SentimentRequestDTO;
import aic12.project3.dto.SentimentRequestStats;
import aic12.project3.dto.StatisticsBean;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {
  	
	//private static Logger myLogger = Logger.getLogger("JULI"); //import java.util.logging.Logger;
	
	private String name;
	private String registered = "false";
	private String  helloMessage = "none yet";
	
	private List<SentimentRequestStats> requestStats = new ArrayList<SentimentRequestStats>();

	private StatisticsBean statistics;
	
	public String loginAction(){
    	
		/*
		 * send request to requestService if company already reqistered
		 */
		List<SentimentRequestDTO> response = GoogleRequestDAO.instance.getAllRequestForCompany(this.name);
		
		/*
		 * if company not yet registered
		 */
		if(response.size() == 0){
			this.setRegistered("false");
			
		} 
		/*
		 * if company already registered
		*/ 
		else {
					
		System.out.println("USER: " + this.name + " - getting requests.");
		/*
		 * get this company's past sentiment analysis results
		 */
		//transformRequest(response);
		System.out.println("USER: " + this.name + " - requests obtained.");
			
		this.setRegistered("true");
		
		}
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("companyName", name);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("requestStats", requestStats);
		
		return "request.xhtml";
		
//		/*
//		 * TESTING
//		 */
//		  if(this.name.equals("jana")){
//		/*
//		 * Test
//		 */
//		SentimentRequestList userRequests = mockUserRequests();
//		/*
//		 * End test
//		 */
//			  transformRequest(userRequests);
//			  this.setRegistered("true");
//			
//		}else{
//			this.setRegistered("false");
//			
//		}
//		
//		return "login";
//		/*
//		 * END TESTING
//		 */
	}
	
	private void transformRequest(List<SentimentRequestDTO> userRequests){
					
		double sumSentiment = 0;
		long finalNumberOfTweets = 0;
		int numberOfSubrequests = 0;

		/*
		 * calculating request details
		 */
		for (SentimentRequestDTO userRequest : userRequests) {
			List<SentimentProcessingRequestDTO> list = GoogleProcessingRequestDAO.instance.getAllSentimentRequestForRequest(Key.create(SentimentRequestDTO.class, userRequest.getId()).getString());
			numberOfSubrequests = list.size();
			SentimentRequestStats stats = new SentimentRequestStats();
			stats.setFrom(userRequest.getFrom());
			stats.setTo(userRequest.getTo());
			
			sumSentiment = 0;
			finalNumberOfTweets = 0;
			
			for (SentimentProcessingRequestDTO subrequest : list) {

				sumSentiment += subrequest.getSentiment();
				finalNumberOfTweets += subrequest.getNumberOfTweets();
				
			}
			
			
			double finalSentiment = sumSentiment / numberOfSubrequests;

			double standardError = 1.96 * Math.sqrt(finalSentiment
					* (1 - finalSentiment) / (finalNumberOfTweets - 1));
			
			stats.setSentiment(finalSentiment);
			stats.setTweets(finalNumberOfTweets);
			stats.setIntervalMin(finalSentiment - standardError);
			stats.setIntervalMax(finalSentiment + standardError);
			
			
			requestStats.add(stats);
		}

	}
	
	public String getAnalysisStatistics() {

		// TODO calculate

//		/*
//		 * TEST
//		 */
//		 StatisticsBean test = new StatisticsBean();
//		 test.setAverageDurationPerRequest(4464);
//		 test.setAverageProcessingDurationPerTweet(4464);
//		 test.setAverageTotalDurationPerTweet(4);
//		 test.setMaximumDurationOfRequest(545646);
//		 test.setMinimumDurationOfRequest(98789749); this.statistics=test;
//		 /*
//		  * END TEST
//		  */

		return "statistics";
	}


	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public List<SentimentRequestStats> getRequestStats() {
		return requestStats;
	}

	public void setRequestStats(List<SentimentRequestStats> requestStats) {
		this.requestStats = requestStats;
	}
	
	public String getHelloMessage() {
		return helloMessage;
	}

	public void setHelloMessage(String helloMessage) {
		this.helloMessage = helloMessage;
	}

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public StatisticsBean getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsBean statistics) {
		this.statistics = statistics;
	}

}

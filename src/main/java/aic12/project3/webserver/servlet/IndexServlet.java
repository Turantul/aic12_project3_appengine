package aic12.project3.webserver.servlet;


import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aic12.project3.dao.GoogleTweetDAO;
import aic12.project3.dto.TweetDTO;

public class IndexServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		String company = req.getParameter("company");
		GoogleTweetDAO dao = new GoogleTweetDAO();
		
		List<TweetDTO> tList= dao.getAllTweetsOffsetLimit(Integer.parseInt(req.getParameter("offset")), Integer.parseInt(req.getParameter("pagesize")));
		for(TweetDTO t : tList){
			if(t.getText().contains(company)){
				List<String> cList = t.getCompanies();
				if(!cList.contains(company)){
					cList.add(company);
					t.setCompanies(cList);
					dao.storeTweet(t);
				}
			}
		}
        resp.setStatus(200);
    }
}

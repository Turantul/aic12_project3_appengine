package aic12.project3.webserver;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import aic12.project3.dao.GoogleProcessingRequestDAO;
import aic12.project3.dao.GoogleRequestDAO;
import aic12.project3.dto.SentimentProcessingRequestDTO;
import aic12.project3.dto.SentimentRequestDTO;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable
{
    private String name;

    public String loginAction()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("companyName", name);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("result", "");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("averageDurationPerRequest", "");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("minimumDurationOfRequest", "");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("maximumDurationOfRequest", "");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("averageProcessingDurationPerTweet", "");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("averageTotalDurationPerTweet", "");

        return "request.xhtml";
    }
    
    public String getAnalysisStatistics()
    {
        calculate(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("companyName").toString());
        
        return "statistics.xhtml";
    }
    
    private void calculate(String companyName)
    {
        long totalTime = 0;
        long totalProcessingTime = 0;
        long totalTotalTime = 0;
        int count = 0;
        int amount = 0;
        
        long maximumDurationOfRequest = -1;
        long minimumDurationOfRequest = -1;
        
        List<SentimentRequestDTO> list = GoogleRequestDAO.instance.getAllRequestForCompany(companyName);
        
        if (list != null && list.size() > 0)
        {
            for (SentimentRequestDTO request : list)
            {
                long requestDuration = request.getTimestampRequestFinished() - request.getTimestampRequestSending();
                
                if (requestDuration > 0)
                {
                    amount++;
                    totalTime += requestDuration;
                    
                    if (requestDuration > maximumDurationOfRequest)
                    {
                        maximumDurationOfRequest = requestDuration;
                    }
                    if (requestDuration < minimumDurationOfRequest || minimumDurationOfRequest == -1)
                    {
                        minimumDurationOfRequest = requestDuration;
                    }
                    
                    List<SentimentProcessingRequestDTO> list2 = GoogleProcessingRequestDAO.instance.getAllSentimentRequestForRequest(request.getId());
    
                    for (SentimentProcessingRequestDTO procrequest : list2)
                    {
                        totalProcessingTime += procrequest.getTimestampAnalyzed() - procrequest.getTimestampDataFetched();
                        totalTotalTime += procrequest.getTimestampAnalyzed() - procrequest.getTimestampStartOfAnalysis();
                        count += procrequest.getNumberOfTweets();
                    }
                    
                    totalTotalTime += requestDuration - request.getTimestampRequestFinished() + request.getTimestampRequestSending();
                }
            }
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("averageDurationPerRequest", amount == 0 ? -1 : totalTime / amount);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("minimumDurationOfRequest", minimumDurationOfRequest);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("maximumDurationOfRequest", maximumDurationOfRequest);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("averageProcessingDurationPerTweet", count == 0 ? -1 : totalProcessingTime / count);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("averageTotalDurationPerTweet", count == 0 ? -1 : totalTotalTime / count);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

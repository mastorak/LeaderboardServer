package com.ludumium.leaderboard.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ludumium.leaderboard.server.dao.ScoreDAO;
import com.ludumium.leaderboard.server.util.Constants;
import com.ludumium.leaderboard.server.util.Tools;

/**
 * Servlet for score retrieval
 *  
 * @author mastorak 
 * 
 */
@SuppressWarnings("serial")
public class RetrieveScoreServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		ScoreDAO dao=new ScoreDAO();
		
		List<Entity> records=null;
		
		//get the country code. This will only work when deployed on google cloud 
		String countryCode =req.getHeader("X-AppEngine-country");
		
		//if country code is null set it to 'n/a'
		if(countryCode==null){
			countryCode=Constants.NOTAVAILABLE;
		}
		
		//get the payload
		String type =req.getParameter(Constants.TYPE);
				
		//get the highscore records based on request type
		if(!Tools.isEmptyOrNull(type) && type.equals(Constants.GLOBAL)){
			records=dao.getGlobalHighScores();
		}
		else if(!Tools.isEmptyOrNull(type) && type.equals(Constants.NATIONAL)){
			records=dao.getNationalHighScores(countryCode);
		}
		else if(!Tools.isEmptyOrNull(type) && type.equals(Constants.MONTHLY)){
			records=dao.getMonthlyHighScores();
		}
		else if(!Tools.isEmptyOrNull(type) && type.equals(Constants.WEEKLY)){
			records=dao.getWeeklyHighScores();
		}
		else if(!Tools.isEmptyOrNull(type) && type.equals(Constants.DAILY)){
			records=dao.getDailyHighScores();
		}
		
		
		JsonArray recordArray=new JsonArray();
		
		if(records!=null){
			//iterate through the records and construct the json object
			for(Entity entity:records){
				JsonObject record = new JsonObject();
		        record.addProperty(Constants.SCORE, (Double)entity.getProperty(Constants.SCORE));
		        record.addProperty(Constants.USERNAME, (String)entity.getProperty(Constants.USERNAME));
		        record.addProperty(Constants.PLATFORM, (String)entity.getProperty(Constants.PLATFORM));
		        record.addProperty(Constants.COUNTRY_CODE, (String)entity.getProperty(Constants.COUNTRY_CODE));
		        recordArray.add(record);
			}
		}	
		//prepare response
		resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(recordArray);
		
	}
}

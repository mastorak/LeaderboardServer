package com.ludumium.leaderboard.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ludumium.leaderboard.server.dao.ScoreDAO;
import com.ludumium.leaderboard.server.util.Constants;

/**
 * Servlet to store a score record
 * 
 * @author mastorak
 * 
 */
@SuppressWarnings("serial")
public class UpdateScoreServlet extends HttpServlet {
	
		
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//get the country code. This will only work when deployed on google cloud
		String countryCode =req.getHeader("X-AppEngine-country");
		
		ScoreDAO dao=new ScoreDAO();
		
		//get the payload
		String jsonString =req.getParameter(Constants.RECORD);
		
		//create and store the record
		dao.createScoreRecord(jsonString,countryCode);
		
	}
	
}


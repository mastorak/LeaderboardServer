package com.ludumium.leaderboard.server.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ludumium.leaderboard.server.util.Constants;
import com.ludumium.leaderboard.server.util.Tools;


/**
 * The Score DAO
 * 
 * @author mastorak
 * 
 */
public class ScoreDAO {
	
	private static final Logger log = Logger.getLogger(ScoreDAO.class.getSimpleName());
	
	private DatastoreService datastore;
	
	/**
	 * Constructor
	 * It instantiates the Datastore ServiceFactory
	 */
	public ScoreDAO (){
		super();
		 datastore= DatastoreServiceFactory.getDatastoreService();
	}
	
	/**
	 * Method to create a record from a json string and store the record to the datastore
	 * @param jsonString
	 * @param countryCode
	 * @return
	 */
	public Entity createScoreRecord(String jsonString,String countryCode){
	
		Entity record=null;
		log.info("Creating gamescore record");
		log.info("Record:"+jsonString);
		
		
		//if country code is null set it to 'n/a'
		if(countryCode==null){
			countryCode=Constants.NOTAVAILABLE;
		}
				
		//parse json string
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject)parser.parse(jsonString);
        
		//read json object properties
		JsonElement score = obj.get(Constants.SCORE);
        JsonElement username=obj.get(Constants.USERNAME);
        JsonElement platform=obj.get(Constants.PLATFORM);
       
      
        
        if(!Tools.isEmptyOrNull(score.getAsString())&& !Tools.isEmptyOrNull(username.getAsString())&& !Tools.isEmptyOrNull(platform.getAsString())){
	        //create and populate record
        	Date date=new Date();
        	Calendar calendar = Calendar.getInstance();
        	record=new Entity(Constants.RECORD);
	        record.setProperty(Constants.SCORE, new Double(score.getAsString()));
	        record.setProperty(Constants.USERNAME, username.getAsString());
	        record.setProperty(Constants.PLATFORM, platform.getAsString());
	        record.setProperty(Constants.DATE, date);
	        record.setProperty(Constants.COUNTRY_CODE, countryCode);
	        
	        /*
	         *Google app engine datastore does not allow queries with inequality comparison that are not sorted
	         *with the comparison property. So, we store the date info we want in order to only use simple equality comparison in
	         *order to sort just with score
	         */
	        record.setProperty(Constants.YEAR,calendar.get(Calendar.YEAR));
	        record.setProperty(Constants.MONTH,calendar.get(Calendar.MONTH));
	        record.setProperty(Constants.WEEK_OF_THE_YEAR,calendar.get(Calendar.WEEK_OF_YEAR));
	        record.setProperty(Constants.DAY_OF_THE_YEAR,calendar.get(Calendar.DAY_OF_YEAR));
	        
	        //persist record
	        log.info("Persisting record "+score.getAsString()+" "+username.getAsString());
			datastore.put(record);
        }else{
        	log.warning("Insufficient data to create gamescore record");
        }
		return record;
	}
	
	/**
	 * Method to retrieve the global high scores 
	 * @return
	 */
	public List<Entity> getGlobalHighScores(){
		log.info("Retrieving global high scores");
		//create the query to read the records sorted by score
		Query q = new Query(Constants.RECORD).addSort(Constants.SCORE, SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		//retrieve and return the list of records	
		return pq.asList(FetchOptions.Builder.withLimit(100));
	}

	/**
	 * Method to retrieve the national high scores 
	 * @return
	 */
	public List<Entity> getNationalHighScores(String countryCode){
		log.info("Retrieving national high scores");
		
		//create the country code filter
		Filter country = new FilterPredicate(Constants.COUNTRY_CODE,FilterOperator.EQUAL,countryCode);
		//create the query to read the records sorted by score
		Query q = new Query(Constants.RECORD).addSort(Constants.SCORE, SortDirection.DESCENDING);
		//set the filter to the query
		q.setFilter(country);
		
		PreparedQuery pq = datastore.prepare(q);
		
		//retrieve and return the list of records	
		return pq.asList(FetchOptions.Builder.withLimit(100));
	}
	
	
	/**
	 * Method to retrieve the monthly high scores 
	 * @return
	 */
	public List<Entity> getMonthlyHighScores(){
		log.info("Retrieving monthly high scores");
		
		//calculate calendar info
		Calendar calendar= Calendar.getInstance();		
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		
		//create filters
		Filter yearFilter = new FilterPredicate(Constants.YEAR,FilterOperator.EQUAL,year);
		Filter monthFilter = new FilterPredicate(Constants.MONTH,FilterOperator.EQUAL,month);
			
		//create the query to read the records sorted by score
		Query q = new Query(Constants.RECORD).addSort(Constants.SCORE, SortDirection.DESCENDING);
		//set filters to the query
		q.setFilter(yearFilter);
		q.setFilter(monthFilter);
		
		//prepare query
		PreparedQuery pq = datastore.prepare(q);
		
		//retrieve and return the list of records	
		return pq.asList(FetchOptions.Builder.withLimit(100));
	}
	
	/**
	 * Method to retrieve the weekly high scores 
	 * @return
	 */
	public List<Entity> getWeeklyHighScores(){
		log.info("Retrieving weekly high scores");
		
		//calculate calendar info
		Calendar calendar= Calendar.getInstance();		
		int year=calendar.get(Calendar.YEAR);
		int week=calendar.get(Calendar.WEEK_OF_YEAR);
		
		//create filters
		Filter yearFilter = new FilterPredicate(Constants.YEAR,FilterOperator.EQUAL,year);
		Filter weekFilter = new FilterPredicate(Constants.WEEK_OF_THE_YEAR,FilterOperator.EQUAL,week);
			
		//create the query to read the records sorted by score
		Query q = new Query(Constants.RECORD).addSort(Constants.SCORE, SortDirection.DESCENDING);
		//set filters to the query
		q.setFilter(yearFilter);
		q.setFilter(weekFilter);
		
		//prepare query
		PreparedQuery pq = datastore.prepare(q);
		
		//retrieve and return the list of records	
		return pq.asList(FetchOptions.Builder.withLimit(100));
	}
	
	
	/**
	 * Method to retrieve the daily high scores 
	 * @return
	 */
	public List<Entity> getDailyHighScores(){
		log.info("Retrieving weekly high scores");
		
		//calculate calendar info
		Calendar calendar= Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int day=calendar.get(Calendar.DAY_OF_YEAR);
		
		//create filters
		Filter yearFilter = new FilterPredicate(Constants.YEAR,FilterOperator.EQUAL,year);
		Filter dayFilter = new FilterPredicate(Constants.DAY_OF_THE_YEAR,FilterOperator.EQUAL,day);
		
		//create the query to read the records sorted by score
		Query q = new Query(Constants.RECORD).addSort(Constants.SCORE, SortDirection.DESCENDING);
		
		//set filters to the query
		q.setFilter(yearFilter);
		q.setFilter(dayFilter);
		
		//prepare query
		PreparedQuery pq = datastore.prepare(q);
		
		//retrieve and return the list of records	
		return pq.asList(FetchOptions.Builder.withLimit(100));
	}
	
}

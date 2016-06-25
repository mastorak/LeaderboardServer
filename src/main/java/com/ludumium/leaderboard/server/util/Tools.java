package com.ludumium.leaderboard.server.util;

/**
 * A class for utility methods 
 *
 * @author mastorak
 */
public class Tools {
	
	/**
	 * Method to determine if a String is empty or null
	 * @param string
	 * @return
	 */
	public static boolean isEmptyOrNull(String string){
		
		boolean check=false;
		
		if(string==null || string.trim().isEmpty() ){
			check=true;
		}
		
		return check;
	}
		
}

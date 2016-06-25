/**
 * Functions to use in the sample landing page to store and retrieve records
 * 
 * @author mastorak
 * 
 */


/**
 * Create the Ajax request object
 */
function ajaxRequest(){
 var activexmodes=["Msxml2.XMLHTTP", "Microsoft.XMLHTTP"] //activeX versions to check for in IE
 if (window.ActiveXObject){ //If IE
  for (var i=0; i<activexmodes.length; i++){
   try{
    return new ActiveXObject(activexmodes[i])
   }
   catch(e){
	   console.log(e);
   }
  }
 }
 else if (window.XMLHttpRequest) // If not IE
  return new XMLHttpRequest()
 else
  return false
}

/**
 * Create a record
 */
function createRecord(){
	
	
	var i=0;
	for(i=0; i<10; i++){
		
		var val= i*10;
		
		var myrequest=new ajaxRequest()
		myrequest.onreadystatechange=function(){
		 if (myrequest.readyState==4){
		  if (myrequest.status==200 || window.location.href.indexOf("http")==-1){
		   document.getElementById("result").innerHTML=myrequest.responseText
		  }
		  else{
		   alert("An error has occured making the request")
		  }
		 }
		}
		
		
		var jsonString="{'score': '"+val+"', 'username': 'UglyJoe','platform':'Android','countrycode':'lu'}";
		var value=encodeURIComponent(jsonString)
		var parameters="record="+value;
		
		myrequest.open("POST", "../updategamescore", true)
		myrequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
		myrequest.send(parameters);
		console.log("Persisting :"+jsonString);
		
	}
	alert("10 new records created");
	
}

/**
 * Retrieve records
 */
function retrieveRecords(value){
	
	var myrequest=new ajaxRequest()
	myrequest.onreadystatechange=function(){
	 if (myrequest.readyState==4){
	  if (myrequest.status==200 || window.location.href.indexOf("http")==-1){
	   document.getElementById("result").innerHTML=myrequest.responseText
	  }
	  else{
	   alert("An error has occured making the request")
	  }
	 }
	}
	var parameters="type="+encodeURIComponent(value);	
	myrequest.open("GET", "../retrievegamescore?type="+value, true)
	myrequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
	
	myrequest.send();
	console.log("Requesting "+value);
	
}


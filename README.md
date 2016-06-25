Leaderboard Server
==================
This is a simple leaderboard server for use in games. It is a google app-engine based project.
The server offers services to store and retrieve score records. Records are stored on the google cloud nosql datastore.

Score record retrieval includes the following filters:
 * national 
 * global
 * weekly
 * monthly
 * daily

Usage
-----
You need Java and Maven installed.

Build

`
mvn clean install
`

Package

`
mvn package
`

Run the server locally

`
mvn appengine:devserver
`

The server contains a sample landing page for creating and viewing records.

For examples of how to call the servlets to store and retrieve records check the tools.js 

To test the server open http://localhost:8080/ in your browser


Security Warning
----------------
The leaderboard server __is provided as is with no security__.  You are responsible for securing the services. This is a sample on which to base your own server. For real deployments you will also need to remove the landing page and the respective sample javascript lib.

 - - - -

Created by [@mastorak](https://twitter.com/mastorak). More info at [ludumium.com](http://ludumium.com)





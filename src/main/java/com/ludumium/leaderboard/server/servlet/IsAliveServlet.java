package com.ludumium.leaderboard.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test if the server is alive
 * 
 * @author mastorak
 *
 */
@SuppressWarnings("serial")
public class IsAliveServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// prepare response
		resp.setContentType("Content-Type: text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print("OK");
	}
}

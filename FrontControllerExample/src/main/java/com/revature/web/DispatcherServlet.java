package com.revature.web;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final ObjectMapper mapper = new ObjectMapper();

	public DispatcherServlet() {
		// The following line is used to pretty print JSON
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Tell the client to expect JSON
		resp.setContentType("application/json");

		// Delegate to the Dispatcher to get the response. 
		// This logic _technically_ should be in a service method
		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			// Write the JSON Response and return
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
			} catch (IOException e) {
				// The Collections.singletonMap method I use here is a quick way to marshal a JSON object
				// will return something like { "error": "Failed to write Todo as JSON" }
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write List of Todos as JSON")));
			}
		} else {
			// Return some 4XX error
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");

		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			// Write the JSON Response and return
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
			} catch (IOException e) {
				// The Collections.singletonMap method I use here is a quick way to marshal a JSON object
				// will return something like { "error": "Failed to write Todo as JSON" }
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write Todo as JSON")));
			}
		} else {
			// Return some 4XX error
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");

		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			// Write the JSON Response and return
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
			} catch (IOException e) {
				// The Collections.singletonMap method I use here is a quick way to marshal a JSON object
				// will return something like { "error": "Failed to write Todo as JSON" }
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write Todo as JSON")));
			}
		} else {
			// Return some 4XX error
		}
	}
}

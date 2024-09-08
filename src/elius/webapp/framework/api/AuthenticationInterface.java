/**
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at
    
      http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
*/

package elius.webapp.framework.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import elius.webapp.framework.application.ApplicationAttributes;
import elius.webapp.framework.application.ApplicationUser;
import elius.webapp.framework.security.authentication.AuthenticationManager;
import elius.webapp.framework.security.secret.SecretCredentials;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * API to manage user authentication
 * 
 * @author Elia Milioni
 *
 */
@Path("/auth")
public class AuthenticationInterface extends Application {

	// Get logger
	private static Logger logger = LogManager.getLogger(AuthenticationInterface.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(SecretCredentials credentials, @Context HttpServletRequest httpRequest) {
		// Log request received
		logger.trace("Application Login request received");

		// Create instance for user profile
		AuthenticationManager appUserProfile = new AuthenticationManager();
		
		// Execute login
		ApplicationUser appUser = appUserProfile.login(credentials, httpRequest.getSession());
		
		// Check for errors
		if(null == appUser) {
			// Log unauthorized used
			logger.error("UserId(" + credentials.getUserId() + ") not authorized");
			
			// Return error
			return Response.status(Response.Status.UNAUTHORIZED).entity(appUserProfile.getResponse()).build();
		}
		
		// Get Session
		HttpSession session = httpRequest.getSession();
		
		// Save userId in session
		session.setAttribute(ApplicationAttributes.APP_USER_INFO, appUser);
		
		// Log request successfully
		logger.trace("UserId(" + credentials.getUserId() + ") successfully authenticated with role (" + appUser.getUserRole() + ")");
		
		// Return authenticated
		return Response.ok().build();
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@Context HttpServletRequest httpRequest) {
		
		// Log request received
		logger.trace("Session get user request received");
		
		// Get Session
		HttpSession session = httpRequest.getSession();
		
		// Save userId in session
		ApplicationUser appUser = (ApplicationUser)session.getAttribute(ApplicationAttributes.APP_USER_INFO);
		
		// No valid user found in session
		if(null == appUser) {
			// Log request error
			logger.warn("User not found");		
			// Set error
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	
		// Log request successfully
		logger.trace("Fetched userId(" + appUser.getUserId() + ") cn(" + appUser.getCompleteName() + ") role(" + appUser.getUserRole().getName() + ")");
		
		// Return successful
		return Response.ok().entity(appUser).build();
	}	
	

	
	@GET
	@Path("logout")
	public Response logout(@Context HttpServletRequest httpRequest) {
		
		// Log request received
		logger.trace("Logout request received");
		
		// Get Session
		HttpSession session = httpRequest.getSession();
		
		// Get userId in session
		ApplicationUser appUser = (ApplicationUser)session.getAttribute(ApplicationAttributes.APP_USER_INFO);
		
		// No valid user found in session
		if(null == appUser) {
			// Log request error
			logger.warn("User not found");		
		} else {
			// Log request successfully
			logger.trace(appUser.getUserId() + " logged out");
		}
		// Invalidate session
		session.invalidate();
		
		// Return successful
		return Response.ok().build();
	}	
}

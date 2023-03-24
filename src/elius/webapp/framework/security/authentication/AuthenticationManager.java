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

package elius.webapp.framework.security.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import elius.webapp.framework.application.ApplicationAttributes;
import elius.webapp.framework.application.ApplicationUser;
import elius.webapp.framework.application.ApplicationUserRole;
import elius.webapp.framework.properties.PropertiesManager;
import elius.webapp.framework.security.ldap.LdapManager;
import elius.webapp.framework.security.secret.SecretCredentials;

public class AuthenticationManager {

	// Get logger
	private static Logger logger = LogManager.getLogger(AuthenticationManager.class);
	
	// Enable authentication
	private String enableAuthentication;
	
	// LDAP Manager
	private LdapManager ldapManager;
	
	// Response message
	private String response;
	
	// Property file
	private PropertiesManager appProperties;
	// Group base distinguished name
	private String groupBaseDn;
	// Search user group
	private String groupSearchUser;
	// Search power user group
	private String groupSearchPowerUser;
	// Attribute for complete name
	private String completeName;


	
	
	/**
	 * Constructor
	 */
	public AuthenticationManager() {
		// Initialize LDAP Manager
		ldapManager = new LdapManager();
		// Application properties
		appProperties = new PropertiesManager();
		// Load default properties
		appProperties.load();
		
		// Enable authentication
		enableAuthentication = appProperties.get(ApplicationAttributes.PROP_SECURITY_AUTHENTICATION_ENABLE, ApplicationAttributes.DEFAULT_SECURITY_AUTHENTICATION_ENABLE);
		
		// Set default in case of invalid value
		if(!"y".equalsIgnoreCase(enableAuthentication) && !"n".equalsIgnoreCase(enableAuthentication)) {
			// Set default application value
			enableAuthentication = ApplicationAttributes.DEFAULT_SECURITY_AUTHENTICATION_ENABLE;
			// Log the error
			logger.warn("Invalid value of properties (" + ApplicationAttributes.PROP_SECURITY_AUTHENTICATION_ENABLE + "), default was set (" + ApplicationAttributes.DEFAULT_SECURITY_AUTHENTICATION_ENABLE + ")");
		}
		
		// Group base distinguished name
		groupBaseDn = appProperties.get(ApplicationAttributes.PROP_LDAP_BASEDN);
		// User group search
		groupSearchUser = appProperties.get(ApplicationAttributes.PROP_LDAP_GROUP_SEARCH_USER);
		// Power user group search
		groupSearchPowerUser = appProperties.get(ApplicationAttributes.PROP_LDAP_GROUP_SEARCH_POWERUSER);
		// Attribute for complete name
		completeName = appProperties.get(ApplicationAttributes.PROP_LDAP_USER_CN);
	}
	
	
	/**
	 * Execute login procedure
	 * @param credentials Credentials
	 * @return Application user or null in case of errors
	 */
	public ApplicationUser login(SecretCredentials credentials) {
		// Response message
		response = "";
		
		// User authenticated without error: create response user
		ApplicationUser appUser = new ApplicationUser();
		
		// Set userId
		appUser.setUserId(credentials.getUserId());

		// Application authentication disabled
		if("n".equalsIgnoreCase(enableAuthentication)) {
			// Get default role for the unauthenticated users
			String uRole = appProperties.get(ApplicationAttributes.PROP_SECURITY_UNAUTHENTICATED_ROLE);
			//Set role
			appUser.setUserRole(ApplicationUserRole.getByName(uRole));
			
			// Check if default role it's unauthorized
			if(ApplicationUserRole.UNAUTHORIZED == appUser.getUserRole()) {
				// Set error
				logger.warn("User (" + appUser.getUserId() + ") not authorized");
				// Return error
				return null;
			}
			
			// Log the unauthenticated user
			logger.warn("User (" + appUser.getUserId() + ") Role(" + appUser.getUserRole().getName() + ") logged in without authentication (disabled)");
			
			// Return successful
			return appUser;
		}
		
		// Connect to LDAP
		if(0 != ldapManager.connect()) {
			// Set error response message
			response = "Error during ldap connection";
			// return error
			return null;
		}
		
		// Authentication
		response = ldapManager.authenticate(credentials);
		
		// Check errors
		if(!response.isEmpty())
			return null;		
	
		// Check user search string
		if(!groupSearchUser.isEmpty()) {
			// Check user group
			if(ldapManager.checkGroup(groupBaseDn, groupSearchUser.replace("?", credentials.getUserId()))) {
				// Set authorization to user
				appUser.setUserRole(ApplicationUserRole.USER);
			}
		} else {
			// No specific filter: set authorization to user
			appUser.setUserRole(ApplicationUserRole.USER);		
		}
		
		// Check power user search string
		if(!groupSearchPowerUser.isEmpty()) {
			// Check power user group
			if(ldapManager.checkGroup(groupBaseDn, groupSearchPowerUser.replace("?", credentials.getUserId()))) {
				// Set authorization to power user
				appUser.setUserRole(ApplicationUserRole.POWER_USER);
			}
		} else {
			// No specific filter: set authorization to power user
			appUser.setUserRole(ApplicationUserRole.POWER_USER);		
		}
		
		// Get complete name
		String userCN = ldapManager.getAttribute(credentials.getUserId(), completeName);
		// Check result
		if(null != userCN) {
			// Set complete name
			appUser.setCompleteName(userCN);
		}
			
		// Close LDAP connection
		ldapManager.close();
		
		// Return successful
		return appUser;
	}


	/**
	 * Get last response message
	 * @return Message
	 */
	public String getResponse() {
		return response;
	}	
	
}

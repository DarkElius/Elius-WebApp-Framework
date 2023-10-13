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

package elius.webapp.framework.security.ldap;

import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

import elius.webapp.framework.application.ApplicationAttributes;
import elius.webapp.framework.properties.PropertiesManager;
import elius.webapp.framework.security.SecurityTrustAllCertificates;
import elius.webapp.framework.security.secret.SecretCredentials;

public class LdapManager {
	
	// Get logger
	private static Logger logger = LogManager.getLogger(LdapManager.class);

	// Properties file
	private PropertiesManager appProperties;
	
	// LDAP server address
	private String server;
	// LDAP port
	private int port;
	// LDAP Base distinguished name
	private String baseDn;
	// LDAP use secure connection (LDAPs)
	private boolean useSecure;
	// LDAP trust all server certificates
	private boolean trustAllCertificates;
	// LDAP connection
	private LDAPConnection connection;

	
	/**
	 * Constructor
	 */
	public LdapManager() {
		// Application properties
		appProperties = new PropertiesManager();
		// Load default properties
		appProperties.load();
		
		// LDAP Server
		server = appProperties.get(ApplicationAttributes.PROP_LDAP_SERVER);
		
		// LDAP Port
		port = appProperties.getInt(ApplicationAttributes.PROP_LDAP_PORT, ApplicationAttributes.DEFAULT_LDAP_PORT);

		// LDAP enable LDAPs (secure) set default to yes
		useSecure = true;
		// LDAP enable LDAPs (secure) (y/n)
		if(!"Y".equalsIgnoreCase(appProperties.get(ApplicationAttributes.PROP_LDAP_SECURE)))
			useSecure = false;

		// LDAP
		trustAllCertificates = false;
		// LDAP trust all certificates (y/n)
		if("Y".equalsIgnoreCase(appProperties.get(ApplicationAttributes.PROP_LDAP_TRUST_ALL_CERTIFICATES)))
			trustAllCertificates = true;
		
		// LDAP BaseDN
		baseDn = appProperties.get(ApplicationAttributes.PROP_LDAP_BASEDN);
	}
	

	/**
	 * Connect to LDAP
	 * @return 0 Successful, 1 Error
	 */
	public int connect() {
		// Log connection
		logger.trace("Connect to server(" + server + ") port(" + port + ") ldaps(" + useSecure + ") trustAllCertificates(" + trustAllCertificates + ")");
		
		// LDAP connection
		connection = null;
		
		// SSL Context for secure connections
		SSLContext sslContext = null;
			
		try {
			// Enable secure LDAP (LDAPs)
			if(useSecure) {
				// Get context for TLS 1.2
				sslContext = SSLContext.getInstance("TLSv1.2");
				// Initialize context
				sslContext.init(null, null, null);
				
				// Trust all certificates
				if(trustAllCertificates) {
					// Create Trust Manager
					TrustManager[] trustAll = new TrustManager[] {new SecurityTrustAllCertificates()};
					// Set trust manager in the context
					sslContext.init(null, trustAll, new java.security.SecureRandom());
				}
				// Start connection / authentication
				connection = new LDAPConnection(sslContext.getSocketFactory(), server, port);
			} else {
				// Start connection / authentication
				connection = new LDAPConnection(server, port);
			}
			
			// Log successful
			logger.trace("Connected");		
		} catch (Exception e) {
			// Log error
			logger.error("Error initializing connection");
			// Log error message
			logger.error(e.getMessage());
			// Set error code
			return 1;
		}
		
		// Return successful
		return 0;
	}
	
	
	/**
	 * Close LDAP connection
	 */
	public void close() {
		// Verify connection
		if(null != connection) {
			// Close connection
			connection.close();			
			// Reset connection status
			connection = null;
			// Log closure
			logger.trace("Connection closed");
		}
	}
	
	
	/**
	 * Authenticate user
	 * @param credentials Credentials
	 * @return Blank for successful or the error message
	 */
	public String authenticate(SecretCredentials credentials) {
		// Log authentication
		logger.trace("Authenticate userId(" + credentials.getUserId() + ") on baseDn(" + baseDn + ")");
		
		// Response message
		String response = "";
		
		// Check for connection
		if(null == connection) {
			// Set error message
			response = "Connection not initialized";
		}
		
		try {
			// Authentication
			connection.bind("uid=" + credentials.getUserId() + "," + baseDn, credentials.getPassword()); 
			// Log logged in
			logger.info("UserId(" + credentials.getUserId() + ") authenticated");
		} catch (LDAPException e) {
			// Log error
			logger.error("Error during ldap authentication of userId(" + credentials.getUserId() + ")");
			// Log error message
			logger.error(e.getMessage());
			// Set generic response message for security reasons
			response = "Invalid Credentials";
		}
		
		// Return response message
		return response;
	}
	
	
	/**
	 * Execute a search to verify if user is present in a specific group
	 * @param groupBaseDn Group base distinguished name
	 * @param groupSearch Group search string
	 * @return True if search return at least one element
	 */
	public boolean checkGroup(String groupBaseDn, String groupSearch) {
		// Log request
		logger.debug("Check GroupBaseDn(" + groupBaseDn + ") GroupSearch(" + groupSearch + ")");
		
		try {
			// Prepare search request
			SearchRequest request = new SearchRequest(groupBaseDn, SearchScope.SUB, groupSearch);
			
			// No limit
            request.setSizeLimit(0);
            
            // Execute search
            SearchResult searchResult = connection.search(request);

            // Get results
            List<SearchResultEntry> result = searchResult.getSearchEntries();

            // Empty list
            if (result.isEmpty()) {
            	// No records found
            	return false;
            }
		} catch (LDAPException e) {
			// Log error
			logger.error("Error searching group");
			// Log error message
			logger.error(e.getMessage());
			//Return error
			return false;
		}

		// Log value
		logger.debug("UserId found in group");

		// One or more records found
		return true;	
	}
	
	
	/**
	 * Get a specific LDAP attribute for selected user
	 * @param userId UserId
	 * @param attribute LDAP attribute
	 * @return The value of specific attribute if present, otherwise null
	 */
	public String getAttribute(String userId, String attribute) {
		// Log request
		logger.debug("Get attribute(" + attribute + ") for userId(" + userId + ")");

		// Value
		String value = "";
		
		// Search instance
		SearchResultEntry sre;
		
		try {
			// Get attribute for selected user
			sre = connection.getEntry("uid=" + userId + "," + baseDn, attribute);
		} catch (LDAPException e) {
			// Log error
			logger.error("Error searching attribute for userId(" + userId + ")");
			// Log error message
			logger.error(e.getMessage());
			//Return error
			return null;
		}
		
		// Successfully fetched
		if (null != sre) {
			// Get value
			value = sre.getAttributeValue(attribute);			
		}
		
		// Log value
		logger.debug("Value(" + value + ") of attribute(" + attribute + ") for userId(" + userId + ")");

		// Return value for selected attribute-userId
		return value;
	}
	

}

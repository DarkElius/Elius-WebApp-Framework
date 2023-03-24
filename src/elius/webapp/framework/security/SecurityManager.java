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

package elius.webapp.framework.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import elius.webapp.framework.application.ApplicationAttributes;
import elius.webapp.framework.properties.PropertiesManager;



public class SecurityManager {
	
	// Get logger
	private static Logger logger = LogManager.getLogger(SecurityManager.class);

	// Properties file
	private PropertiesManager appProperties;
	
	// Security repository
	private SecurityRepository secRepo;
	
	// Security repository type
	private SecurityRepositoryType secRepoType;
	

	
	/**
	 * Initialize Security Manager
	 * @password KeyStore main password
	 * @return 0 Successful or 1 Error
	 */
	public int initialize(String password) {
		// Log initialization
		logger.trace("Initialize security manager");
		
		// Application properties
		appProperties = new PropertiesManager();
		
		// Load default properties
		if(0 != appProperties.load())
			return 1;
		
		// Get security type from repository
		secRepoType = SecurityRepositoryType
				.getByName(appProperties.get(ApplicationAttributes.PROP_SECURITY_REPOSITORY_TYPE,
						ApplicationAttributes.DEFAULT_SECURITY_REPOSITORY_TYPE));
		
		// Initilaze selected repository type
		switch(secRepoType) {
			
			case KEYSTORE:
				
				// KeyStore
				secRepo = new SecurityRepositoryKeyStore();
				
				break;
				
			case EXTERNAL:

				// External
				secRepo = new SecurityRepositoryExternal();
				
				break;
			
			default:
				logger.error("Invalid security repository type (" + secRepoType.getName() + ")");
		}
		
		// Return security repository result
		return secRepo.initialize(password);
	}
	
	
	/**
	 * Get Entry from repository (KeyStore or External)
	 * @param entry Entry name
	 * @return Entry value or null in case of errors
	 */
	public String get(String entry) {
		// Verify if the Security Manager is initialized checking properties file
		if(null == appProperties) {
			// Log error
			logger.error("Security Manager not initialized");
			// Return error
			return null;
		}
		
		// Return entry value based on intialization
		return secRepo.getEntry(entry);
	}
}

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

import java.io.File;
import java.security.Key;
import java.security.KeyStore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import elius.webapp.framework.application.ApplicationAttributes;


public class SecurityRepositoryKeyStore implements SecurityRepository {
	
	// Get logger
	private static Logger logger = LogManager.getLogger(SecurityRepositoryKeyStore.class);
	
	// KeyStore
	private KeyStore ks;
	
	// KeyStore password
	char [] pwdArray;
	
	
	/**
	 * Load KeyStore
	 * @param password KeyStore password
	 * @return 0 Successful or 1 Error
	 */
	@Override
	public int initialize(String password) {
		// Log
		logger.trace("Load KeyStore (" + System.getProperty(ApplicationAttributes.APP_PATH) + "/" + ApplicationAttributes.APP_KEYSTORE_FILE + ")");
		
		// Verify password before converting it
		if(null == password) {
			// Log error message
			logger.error("KeyStore password can't be null");
			// Return error
			return 1;
		}
			
		// Convert password to byte array
		pwdArray = password.toCharArray();
		
		try {
			// Open keystore with selected password
			ks = KeyStore.getInstance(new File(System.getProperty(ApplicationAttributes.APP_PATH) + "/" + ApplicationAttributes.APP_KEYSTORE_FILE), pwdArray);
		} catch (Exception e) {
			// Log error message
			logger.error("Unable to load KeyStore (" + System.getProperty(ApplicationAttributes.APP_PATH) + "/" + ApplicationAttributes.APP_KEYSTORE_FILE + ")");
			logger.error(e.getMessage());
			// Return error
			return 1;			
		}	
		
		// Log
		logger.debug("KeyStore (" + ApplicationAttributes.APP_KEYSTORE_FILE + ") loaded");
		
		// Return successful
		return 0;
	}
	
	
	/**
	 * Get the value entry from the KeyStore
	 * @param entry Entry name
	 * @return The value of the entry or null in case of errors
	 */
	@Override
	public String getEntry(String entry) {
		// Log
		logger.trace("Get entry for (" + entry + ")");
		
		// Initialize returned value
		Key key = null;
		
		// Check if KeyStore is loaded
		if(null == ks) {
			// Log error message
			logger.error("KeyStore not loaded");
			// Return error
			return null;
		}
		
		
		try {
			// Fetch entry from KeyStore
			key = ks.getKey(entry, pwdArray);
			
			// Verify 
			if(null == key) {
				// Log error message
				logger.debug("Entry (" + entry + ") is null");
				// Return error
				return null;
			}
				
		} catch (Exception e) {
			// Log error message
			logger.error("Error getting entry (" + entry + ")");
			logger.error(e.getMessage());
			// Return error
			return null;
		}
		
		// Return the entry value
		return new String(key.getEncoded());
	}
}

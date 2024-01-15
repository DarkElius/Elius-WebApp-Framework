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

package elius.webapp.framework.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import elius.webapp.framework.application.ApplicationAttributes;



public class PropertiesManager {

	// Get logger
	private static Logger logger = LogManager.getLogger(PropertiesManager.class);
	
	// Properties file
	private Properties properties;

	
	/**
	 * Load property file from default system property
	 * @return 0 Successful, 1 Error
	 */
	public int load() {
		// Load default
		return load(ApplicationAttributes.APP_PROPERTIES_FILE);
	}
	
	
	/**
	 * Load properties file from the application path defined in the java parameters
	 * @param propertiesFile Properties file
	 * @return 0 Successful, 1 Error
	 */
	public int load(String propertiesFile) {
		
		// Initialize properties file
		if(null == properties) {
			// New instance
			properties = new Properties();
		} else {
			// Clear the old
			properties.clear();
		}
		
		// Log file name
		logger.trace("Open property file(" + System.getProperty(ApplicationAttributes.APP_PATH) + "/" + propertiesFile + ")");
		
		// Read property file from system property
        try (InputStream input = new FileInputStream(System.getProperty(ApplicationAttributes.APP_PATH) + "/" + propertiesFile)) {

            // Load a properties file
            properties.load(input);

        } catch (IOException e) {  	
        	// Log trace
        	logger.error(e.getMessage());
        	
        	// Return error
        	return 1;
        }	
		
		// Log successful
		logger.debug("Property file successfully loaded");		
		
		// Return successful
		return 0;
	}
	
	
	/**
	 * Fetch the value of the property 
	 * @param key Key
	 * @return null in case of error or the value of the key
	 */
	public String get(String key) {
		// Set default value for error
        String value = null;
        
        if(null != properties) {
    		// Get property value
        	value = properties.getProperty(key);
    
			// Log key request
			logger.trace("Key(" + key + ") value(" + value + ")");        	
        } 
    	
    	// Return value
    	return value;
	}
	
	/**
	 * Fetch the value of the property and default value is set if it's null or empty
	 * @param key Key
	 * @param defaultValue Default value
	 * @return Key value or default if the original value is null or empty
	 */
	public String get(String key, String defaultValue) {
		// Fetch the key value
		String value = get(key);
		
		// Set default value if it's null or empty
		if ((null == value) || value.trim().isEmpty()) {
			
			// Log warning
			logger.warn("Invalid or empty properties (" + key + "), default was set (" + defaultValue + ")");
			
			// Return the default value
			return defaultValue;
		}
		
		// Return value
		return value;
	}
	
	/**
	 * Fetch the value of the property and default value is set if it's null, empty or not an integer
	 * @param key Key
	 * @param defaultValue Default value
	 * @return Key value or default if the original value is null, empty or not an integer
	 */
	public int getInt(String key, int defaultValue) {
		// Initialize value
		int value = 0;
	
		try {
			
			// Fetch the key value
			value = Integer.parseInt(get(key));
			
		} catch (Exception e) {
			
			// Set default value
			value = defaultValue;
			
			// Log warning
			logger.warn("Invalid or empty properties (" + key + "), default was set (" + defaultValue + ")");		
		}
		
		// Return value
		return value;
	}
	
	
	/**
	 * Fetch the value of the property and default value is set if it's null, empty or not a long
	 * @param key Key
	 * @param defaultValue Default value
	 * @return Key value or default if the original value is null, empty or not a long
	 */
	public long getLong(String key, long defaultValue) {
		// Initialize value
		long value = 0;
	
		try {
			
			// Fetch the key value
			value = Long.parseLong(get(key));
			
		} catch (Exception e) {
			
			// Set default value
			value = defaultValue;
			
			// Log warning
			logger.warn("Invalid or empty properties (" + key + "), default was set (" + defaultValue + ")");		
		}
		
		// Return value
		return value;
	}
}
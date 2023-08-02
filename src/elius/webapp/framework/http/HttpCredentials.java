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

package elius.webapp.framework.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HttpCredentials {
	
	// Get logger
	private static Logger logger = LogManager.getLogger(HttpCredentials.class);

	// UserId
	private String userId;
	// Password
	private String password;
	// Identify if credentials are correctly loaded
	private boolean loaded;
	
	/**
	 * Load credentials
	 * @return 0 Successful, 1 Error
	 */
	public int load() {
		// Log start load
		logger.debug("Load credentials");
		
		// TODO
		userId = "x";
		password = "x";
		loaded = true;

		// Log error
		/*
		logger.error("Error loading credentials");
		*/
		
		// Log end load
		logger.debug("Credentials loaded");
		
		return 0;
	}

	// Get userId
	public String getUserId() {
		return userId;
	}

	// Get Password
	public String getPassword() {
		return password;
	}
	
	// Get loaded status
	public boolean getLoaded()  {
		return loaded;
	}	
}

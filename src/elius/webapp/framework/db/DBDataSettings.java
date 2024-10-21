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

package elius.webapp.framework.db;

public enum DBDataSettings {

	UNKNOWN(0, "Unknown"), 
	DEFAULT(1, "Default"), 
	EMPTY_STRING_TO_NULL(2, "Empty string to null"),
	EMPTY_STRING_TO_SPACE(3, "Empty string to space");
	
	
	// Type name
	private final String name;
	// Type id
	private final int id;
	
	
	/**
	 * Constructor
	 * @param id Fill flag id
	 * @param name Fill flag name
	 */
	DBDataSettings(int id, String name) {
		this.name = name;
		this.id = id;
	}
	
	
	/**
	 * Get fill flag name
	 * @return Fill flag name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Get fill flag id
	 * @return Fill flag id
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * Get fill flag by id
	 * @param id Fill flag id
	 * @return Fill flag
	 */
	public static DBDataSettings getById(int id) {
	    for(DBDataSettings e : values()) {
	        if(e.id == id) return e;
	    }
	    return UNKNOWN;
	}
	
};
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

public enum SecurityRepositoryType {

	UNKNOWN(0, "Unknown"), 
	KEYSTORE(1, "KeyStore"),
	EXTERNAL(2, "External");

	// Repository type name
	private final String name;
	// Repository type id
	private final int id;

	
	/**
	 * Constructor
	 * @param id Repository type id
	 * @param name Repository type name
	 */
	SecurityRepositoryType(int id, String name) {
		this.name = name;
		this.id = id;
	}

	
	/**
	 * Get repository type name
	 * @return Repository type name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Get repository type id
	 * @return Repository type id
	 */
	public int getId() {
		return id;
	}

	
	/**
	 * Get repository type by id
	 * @param id Repository type id
	 * @return Repository type
	 */
	public static SecurityRepositoryType getById(int id) {
	    for(SecurityRepositoryType e : values()) {
	        if(e.id == id) return e;
	    }
	    return UNKNOWN;
	}
	
	
	/**
	 * Get repository type by name
	 * @param name Repository type name
	 * @return Repository type
	 */
	public static SecurityRepositoryType getByName(String name) {
	    for(SecurityRepositoryType e : values()) {
	        if(e.name.equalsIgnoreCase(name)) return e;
	    }
	    return UNKNOWN;
	}
}

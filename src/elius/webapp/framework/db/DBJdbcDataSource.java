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


public class DBJdbcDataSource {
	
	// Jdbc datasource name
	private String name;
	
	// Connection URL to be passed to our JDBC driver
	private String url;
	
	// Fully qualified Java class name of the JDBC driver to be used
	private String driverClassName;
	
	// Database username to be passed to JDBC driver
	private String username;
	
	// Database password to be passed to JDBC driver
	private String password;

	// The maximum number of connections that can be allocated from this pool at the same time
	private String maxTotal;
	
	// The maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception
	private String maxWaitMillis;
	

	/**
	 * Constructor 
	 * @param name Jdbc datasource name, i.e.: jdbc/mydb
	 * @param url Connection URL to be passed to our JDBC driver
	 * @param driverClassName Fully qualified Java class name of the JDBC driver to be used
	 * @param maxTotal The maximum number of connections that can be allocated from this pool at the same time
	 * @param maxWaitMillis The maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception
	 * @param username Database username to be passed to JDBC driver
	 * @param password Database password to be passed to JDBC driver
	 */
	public DBJdbcDataSource(String name, String url, String driverClassName, 
							String maxTotal, String maxWaitMillis,
							String username, String password) {
		this.name = name;
		this.url = url;
		this.driverClassName = driverClassName;
		this.maxTotal = maxTotal;
		this.maxWaitMillis = maxWaitMillis;
		this.username = username;
		this.password = password;
	}


	/**
	 * Get Jdbc datasource name
	 * @return Jdbc datasource name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Set Jdbc datasource name, i.e.: jdbc/mydb
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	

	/**
	 * Get connection URL to be passed to our JDBC driver
	 * @return Connection URL to be passed to our JDBC driver
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * Set connection URL to be passed to our JDBC driver
	 * @param url Connection URL to be passed to our JDBC driver
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * Get fully qualified Java class name of the JDBC driver
	 * @return Fully qualified Java class name of the JDBC driver
	 */
	public String getDriverClassName() {
		return driverClassName;
	}


	/**
	 * Set fully qualified Java class name of the JDBC driver
	 * @param driverClassName Fully qualified Java class name of the JDBC driver
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}


	/**
	 * Get the maximum number of connections that can be allocated
	 * @return The maximum number of connections that can be allocated
	 */
	public String getMaxTotal() {
		return maxTotal;
	}


	/**
	 * Set the maximum number of connections that can be allocated
	 * @param maxTotal The maximum number of connections that can be allocated
	 */
	public void setMaxTotal(String maxTotal) {
		this.maxTotal = maxTotal;
	}


	/**
	 * Get the maximum number of milliseconds that the pool will wait for a connection
	 * @return The maximum number of milliseconds that the pool will wait for a connection
	 */
	public String getMaxWaitMillis() {
		return maxWaitMillis;
	}


	/**
	 * Set the maximum number of milliseconds that the pool will wait for a connection
	 * @param maxWaitMillis The maximum number of milliseconds that the pool will wait for a connection
	 */
	public void setMaxWaitMillis(String maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}


	/**
	 * Get database username to be passed to JDBC driver
	 * @return Database username to be passed to JDBC driver
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * Set database username to be passed to JDBC driver
	 * @param username Database username to be passed to JDBC driver
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * Get database password to be passed to JDBC driver
	 * @return Database password to be passed to JDBC driver
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Set database password to be passed to JDBC driver
	 * @param password Database password to be passed to JDBC driver
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
	
}

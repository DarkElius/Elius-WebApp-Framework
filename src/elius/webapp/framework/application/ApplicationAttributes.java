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

package elius.webapp.framework.application;

public class ApplicationAttributes {
	
	// Attribute to identify user info
	public static final String APP_USER_INFO = "EWA_USER_INFO";
	
	// Attribute to identify the name of the system properties that contains the application path
	public static final String APP_PATH = "ewa.path";
	
	// Application properties file
	public static final String APP_PROPERTIES_FILE = "ewa.properties";
	
	// Application KeyStore path + filename
	public static final String APP_KEYSTORE_FILE = "ewa.jks";
	
	
	
	// Default - Enable authentication
	public static final String DEFAULT_SECURITY_AUTHENTICATION_ENABLE = "y";
	
	// Default - User role for unauthenticated user
	public static final String DEFAULT_SECURITY_UNAUTHENTICATED_ROLE = "USER";
	
	// Default - Security repository type
	public static final String DEFAULT_SECURITY_REPOSITORY_TYPE = "KeyStore";

	// Default - LDAP port
	public static final int DEFAULT_LDAP_PORT = 636;
		

	// Properties - Enable authentication
	public static final String PROP_SECURITY_AUTHENTICATION_ENABLE = "security.authentication.enable";
	
	// Properties - User role for unauthenticated user
	public static final String PROP_SECURITY_UNAUTHENTICATED_ROLE = "security.unauthenticated.role";	
	
	// Properties - Security repository type
	public static final String PROP_SECURITY_REPOSITORY_TYPE = "security.repository.type";
	
	// Properties - External uri
	public static final String PROP_SECURITY_EXTERNAL_URI = "security.external.uri";
	
	// Properties - LDAP server
	public static final String PROP_LDAP_SERVER = "ldap.server";
	
	// Properties - LDAP port
	public static final String PROP_LDAP_PORT = "ldap.port";

	// Properties - LDAP enable ldaps (secure) (y/n), default is y
	public static final String PROP_LDAP_SECURE = "ldap.secure";
	
	// Properties - LDAP trust all certificates (y/n), default is n
	public static final String PROP_LDAP_TRUST_ALL_CERTIFICATES = "ldap.trustAllCertificates";

	// Properties - LDAP base dn
	public static final String PROP_LDAP_BASEDN = "ldap.baseDn";
	
	// Properties - LDAP group - base dn
	public static final String PROP_LDAP_GROUP_BASEDN = "ldap.group.baseDn";
	
	// Properties - LDAP group search for users (? is the userId), blank for authorize it
	public static final String PROP_LDAP_GROUP_SEARCH_USER = "ldap.group.search.user";
	
	// Properties - LDAP group search for power users (? is the userId), blank for authorize it
	public static final String PROP_LDAP_GROUP_SEARCH_POWERUSER = "ldap.group.search.powerUser ";
	
	// Properties - LDAP complete name attribute
	public static final String PROP_LDAP_USER_CN = "ldap.user.cn";
	
}

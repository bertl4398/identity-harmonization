/*   Copyright 2016 Karlsruhe Institute of Technology (KIT)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.kit.scc.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CustomUrlConnection {

	private static final Logger log = LoggerFactory.getLogger(CustomUrlConnection.class);

	private CustomUrlConnection() {
	}

	public static HttpsURLConnection getSecureHttpConnection(boolean checkCertificate, String url) {
		HttpsURLConnection urlConnection = null;
		try {
			log.debug("Try url {}", url);
			URL uri = new URL(url);

			SSLContext sslContext = null;

			if (checkCertificate) {
				// TODO
				HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
				sslContext = CustomSSLContext.initEmptySslContext();
			} else {
				HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
				sslContext = CustomSSLContext.initEmptySslContext();
			}

			urlConnection = (HttpsURLConnection) uri.openConnection();
			urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

		} catch (MalformedURLException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		}
		return urlConnection;
	}

	public static HttpURLConnection getHttpConnection(String url) {
		HttpURLConnection urlConnection = null;
		try {
			log.debug("Try url {}", url);
			URL uri = new URL(url);

			urlConnection = (HttpURLConnection) uri.openConnection();

		} catch (MalformedURLException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		}
		return urlConnection;
	}
}

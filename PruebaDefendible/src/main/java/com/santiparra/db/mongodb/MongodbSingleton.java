package com.santiparra.db.mongodb;

import com.mongodb.MongoClient;

public class MongodbSingleton {
	
	private static MongoClient client = null;
	
	private MongodbSingleton() {
		
	}
	
	public static MongoClient connection() {
		if(client == null) {
			client = new MongoClient();
		}
		return client;
	}
	
	public static MongoClient closeConnection() {
		if(client != null) {
			client.close();
			client = null;
		}
		return client;
	}
}

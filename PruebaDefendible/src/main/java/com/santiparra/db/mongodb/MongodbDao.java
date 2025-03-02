package com.santiparra.db.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.Config;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import interfaces.InterDAO;
import models.POI;

public class MongodbDao implements InterDAO {
	
	private static MongoClient client;
	private static MongodbDao instance;
	private static MongoDatabase database;
	private static MongoCollection<Document> collection;
	
	private MongodbDao() {
		client = MongodbSingleton.connection();
		database = client.getDatabase(Config.DATABASE_NAME_MONGO);
		createCollectionIfNotExists(database); 
		collection = database.getCollection(Config.COLLECTION_NAME);
		
	}
	
	public static MongodbDao getInstance(){
		if(instance == null) {
			instance = new MongodbDao();
		}
		return instance;
	}
	
	private static void createCollectionIfNotExists(MongoDatabase database) {
        if (!collectionExists(Config.COLLECTION_NAME, database)) {
            System.out.println("La colección '" + Config.COLLECTION_NAME + "' no existe. Creándola...");
            database.createCollection(Config.COLLECTION_NAME); 
            System.out.println("Colección '" + Config.COLLECTION_NAME + "' creada con éxito.");
        }
	}
        
        // Verifica si una colección existe en la base de datos
    	private static boolean collectionExists(String collectionName,MongoDatabase database) {
            MongoIterable<String> collections = database.listCollectionNames();
            for (String name : collections) {
                if (name.equals(Config.COLLECTION_NAME)) {
                    return true; 
                }
            }
            return false; 
        }

	@Override
	public int countElement() {
		 if (collection == null) {
		        throw new IllegalStateException("La colección no ha sido inicializada.");
		    }
		long count = collection.countDocuments();
		return (int) count;
	}

	@Override
	public boolean addPoi(POI poi) {
		try {
			Document doc = new Document();
			doc.put("poiId", poi.getPoiId());
            doc.put("latitude", poi.getLatitude());
            doc.put("longitude", poi.getLongitude());
            doc.put("country", poi.getCountry());
            doc.put("city", poi.getCity());
            doc.put("description", poi.getDescription());
            doc.put("update_date", poi.getUpdate());
            collection.insertOne(doc);
            return true;
		}catch (Exception e) {
            System.err.println("Error al agregar POI: " + e.getMessage());
            return false;
		}
	}

	@Override
	public ArrayList<POI> showAll() {
		ArrayList<POI> poi = new ArrayList<POI>();
		for(Document doc : collection.find()) {
			poi.add(mapDocumentToPoi(doc));
		}
		return poi;
	}

	@Override
	public POI showOneById(int id) {
		Document doc = collection.find(Filters.eq("poiId", id)).first();
	    if (doc != null) {
	        return mapDocumentToPoi(doc);
	    } else {
	        return null;
	    }
	}

	@Override
	public ArrayList<POI> showIdRange(int min, int max) {
		 ArrayList<POI> pois = new ArrayList<>();
	        for (Document doc : collection.find(Filters.and(Filters.gte("poiId", min), Filters.lte("poiId", max)))) {
	            pois.add(mapDocumentToPoi(doc));
	        }
	        return pois;
	}

	@Override
	public ArrayList<POI> showMonthModification(int month) {
		   ArrayList<POI> pois = new ArrayList<>();
	        for (Document doc : collection.find(Filters.expr(
	                new Document("$eq", List.of(new Document("$month", "$update_date"), month))
	        ))) {
	            pois.add(mapDocumentToPoi(doc));
	        }
	        return pois;
	}

	@Override
	public ArrayList<POI> showByCity(String city) {
		   ArrayList<POI> pois = new ArrayList<>();
	        for (Document doc : collection.find(Filters.regex("city", city, "i"))) {
	            pois.add(mapDocumentToPoi(doc));
	        }
	        return pois;
	}

	@Override
	public boolean updatePoiId(POI poi) {
		try {
		    Document update = new Document();
		    Document setFields = new Document();

		    setFields.put("latitude", poi.getLatitude());
		    setFields.put("longitude", poi.getLongitude());
		    setFields.put("country", poi.getCountry());
		    setFields.put("city", poi.getCity());
		    setFields.put("description", poi.getDescription());
		    setFields.put("update_date", poi.getUpdate());
		    update.put("$set", setFields);
		    
		    collection.updateOne(Filters.eq("poiId", poi.getPoiId()), update);
		    return true;
		    
		} catch (Exception e) {
		    System.err.println("Error al actualizar POI: " + e.getMessage());
		    return false;
		}
	}

	@Override
	public int dropAll(boolean accept) {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		long deletedCount = collection.deleteMany(new Document()).getDeletedCount();
        return (int) deletedCount;
	}

	@Override
	public int dropOneById(int id, boolean accept) {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		 long deletedCount = collection.deleteOne(Filters.eq("poiId", id)).getDeletedCount();
	        return (int) deletedCount;
	}

	@Override
	public int dropIdRange(int min, int max, boolean accept) {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		long deletedCount = collection.deleteMany(Filters.and(Filters.gte("poiId", min), Filters.lte("poiId", max))).getDeletedCount();
        return (int) deletedCount;
	}

	@Override
	public int dropMonthModification(int month, boolean accept) {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		long deletedCount = collection.deleteMany(Filters.expr(
                new Document("$eq", List.of(new Document("$month", "$update_date"), month))
        )).getDeletedCount();
        return (int) deletedCount;
	}

	@Override
	public int dropByCity(String city, boolean accept) {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		long deletedCount = collection.deleteMany(Filters.regex("city", city, "i")).getDeletedCount();
        return (int) deletedCount;
	}
	
	 private POI mapDocumentToPoi(Document doc) {
	        int poiId = doc.getInteger("poiId");
	        Double latitude = doc.getDouble("latitude");
	        Double longitude = doc.getDouble("longitude");
	        String country = doc.getString("country");
	        String city = doc.getString("city");
	        String description = doc.getString("description");
	        Date update_date = doc.getDate("update_date");
	        return new POI(poiId, latitude, longitude, country, city, description, update_date);
	    }

}

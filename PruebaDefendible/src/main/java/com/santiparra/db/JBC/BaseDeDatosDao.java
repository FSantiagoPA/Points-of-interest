package com.santiparra.db.JBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import com.santiparra.exception.DataBasesException;

import interfaces.InterDAO;
import models.POI;

public class BaseDeDatosDao implements InterDAO {
	
	private static Connection conn;
	private static BaseDeDatosDao instance;
	
	private BaseDeDatosDao() throws SQLException {
		conn = BaseDeDatosSingleton.connect();
	}
	
	public static BaseDeDatosDao getInstance() throws SQLException{
		if(instance == null) {
			instance = new BaseDeDatosDao();
		}
		return instance;
	}

	@Override
	public int countElement() {
		 String query = "SELECT COUNT(*) AS total FROM FSPR;"; 
		    int count = 0;

		    try (PreparedStatement statement = conn.prepareStatement(query);
		         ResultSet resultSet = statement.executeQuery()) {

		        if (resultSet.next()) {
		            count = resultSet.getInt("total");
		        }

		    } catch (SQLException e) {
		        System.err.println("Error al contar elementos: " + e.getMessage());
		        e.printStackTrace();
		    }

		    return count;
	}

	@Override
	public boolean addPoi(POI poi) throws DataBasesException{
		String query = "INSERT INTO FSPR (poiId, latitude, longitude, country, city, description, update_date) VALUES (?,?,?,?,?,?,?)";
		try(PreparedStatement statement = conn.prepareStatement(query)){
			statement.setObject(1, poi.getPoiId());
			statement.setObject(2, poi.getLatitude());
			statement.setObject(3, poi.getLongitude());
			statement.setString(4, poi.getCountry());
			statement.setString(5, poi.getCity());
			statement.setString(6, poi.getDescription());
			if (poi.getUpdate() != null) {
	            statement.setDate(7, new java.sql.Date(poi.getUpdate().getTime()));
	        } else {
	            statement.setNull(7, java.sql.Types.DATE);
	        }
			int rowsAffected; 
			rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}catch (SQLException e) {
			throw new DataBasesException("No se han podido insertat el poi correctamente" + e.getMessage());
		}
	}

	@Override
	public ArrayList<POI> showAll() throws SQLException, DataBasesException {
		ArrayList<POI> poi = new ArrayList<>();
		String query = "SELECT * FROM FSPR ORDER BY poiId";
		try(Statement statement = conn.createStatement()){
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				poi.add(mapResultSetToPublication(resultSet));
			}
		}catch (SQLException e) {
			throw new DataBasesException("No se han podido mostrar listar los datos correctamente" + e.getMessage());
		}
		return poi;
	}

	@Override
	public POI showOneById(int id) throws SQLException, DataBasesException {
		String query = "SELECT * FROM FSPR WHERE poiId = ?";
		try(PreparedStatement statement = conn.prepareStatement(query)){
			statement.setObject(1, id);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				return mapResultSetToPublication(resultSet);
			}
		}catch(SQLException e) {
			throw new DataBasesException("No se ha podido mostrar los elementos por el id" + e.getMessage());
		}
		return null;
	}

	@Override
	public ArrayList<POI> showIdRange(int min, int max) throws SQLException, DataBasesException {
		ArrayList<POI> poi = new ArrayList<>();
		String query = "SELECT * FROM FSPR WHERE poiId BETWEEN ? AND ? ORDER BY poiId";
		try(PreparedStatement statement = conn.prepareStatement(query)){
			statement.setObject(1, min);
            statement.setObject(2, max);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                poi.add(mapResultSetToPublication(resultSet));
            }
		}catch(SQLException e) {
			throw new DataBasesException("No se ha podido mostart los elementos por rango" + e.getMessage());
		}
		return poi;
	}

	@Override
	public ArrayList<POI> showMonthModification(int month) throws SQLException, DataBasesException {
		ArrayList<POI> poi = new ArrayList<>();
		String query ="SELECT * FROM FSPR WHERE MONTH(update_date) = ?";
		try(PreparedStatement statement = conn.prepareStatement(query)){
			statement.setObject(1,month);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				poi.add(mapResultSetToPublication(resultSet));
			}
		}catch(SQLException e) {
			throw new DataBasesException("No se ha podido mostart los elementos" + e.getMessage());
		}
		return poi;
	}

	@Override
	public ArrayList<POI> showByCity(String city) throws SQLException, DataBasesException {
		ArrayList<POI> poi = new ArrayList<>();
		String query = "SELECT * FROM FSPR WHERE city = ?";
		try(PreparedStatement statement = conn.prepareStatement(query)){
			statement.setString(1, city);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				poi.add(mapResultSetToPublication(resultSet));
			}
		}catch(SQLException e ) {
			throw new DataBasesException("No se ha podido mostart los elementos" + e.getMessage());
		}
		return poi;
	}

	@Override
	public boolean updatePoiId(POI poi) throws SQLException, DataBasesException {
		String sql = "UPDATE FSPR SET poiId = ?, latitude = ?, longitude = ?, country = ?, city = ?, description = ?, update_date = ? WHERE latitude = ? AND longitude = ?";
	    try (PreparedStatement statement = conn.prepareStatement(sql)) {
	        statement.setObject(1, poi.getPoiId());
	        statement.setObject(2, poi.getLatitude());
	        statement.setObject(3, poi.getLongitude());
	        statement.setString(4, poi.getCountry());
			statement.setString(5, poi.getCity());
			statement.setString(6, poi.getDescription());
			statement.setDate(7, new java.sql.Date(poi.getUpdate().getTime()));
			
	        // Ejecutar la actualización y verificar si afectó filas
			int rowsAffected; 
			rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
	    }catch(SQLException e) {
	    	throw new DataBasesException("No se han podido actualizar los elementos" + e.getMessage());
	    }
	}

	@Override
	public int dropAll(boolean accept) throws SQLException, DataBasesException {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }

	    String query = "DELETE FROM FSPR";
	    try (PreparedStatement statement = conn.prepareStatement(query)) {
	        // Ejecutar la consulta y devolver el número de filas afectadas
	        int rowsDeleted = statement.executeUpdate();
	        System.out.println(rowsDeleted + " registros eliminados.");
	        return rowsDeleted;
	    }catch(SQLException e) {
	    	throw new DataBasesException("No se han podido eliminar los elementos" + e.getMessage());
	    }
	}

	@Override
	public int dropOneById(int id, boolean accept) throws SQLException, DataBasesException {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		
        String query = "DELETE FROM FSPR WHERE poiId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setObject(1, id);
            int rowsDeleted = statement.executeUpdate();
	        System.out.println(rowsDeleted + " registros eliminados.");
	        return rowsDeleted;
        }catch(SQLException e){
        	throw new DataBasesException("No se han podido eliminar los elementos" + e.getMessage());
        }
	}

	@Override
	public int dropIdRange(int min, int max, boolean accept) throws SQLException, DataBasesException {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		
        String query = "DELETE FROM FSPR WHERE poiId BETWEEN ? AND ? ORDER BY poiId";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setObject(1, min);
            statement.setObject(2, max);
            int rowsDeleted = statement.executeUpdate();
	        System.out.println(rowsDeleted + " registros eliminados.");
	        return rowsDeleted;
        }catch(SQLException e) {
        	throw new DataBasesException("No se han podido eliminar los elementos" + e.getMessage());
        }
	}

	@Override
	public int dropMonthModification(int month, boolean accept) throws SQLException, DataBasesException {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		
		String query ="DELETE FROM FSPR WHERE MONTH(update) = ?";
		 try (PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setObject(1, month);
	            int rowsDeleted = statement.executeUpdate();
		        System.out.println(rowsDeleted + " registros eliminados.");
		        return rowsDeleted;
	        }catch(SQLException e) {
	        	throw new DataBasesException("No se han podido eliminar los elementos" + e.getMessage());
	        }
	}

	@Override
	public int dropByCity(String city, boolean accept) throws SQLException, DataBasesException {
		if (!accept) {
	        System.out.println("Operación cancelada: El parámetro 'accept' no es verdadero.");
	        return 0;
	    }
		
		String query = "DELETE FROM FSPR WHERE city = ?";
		try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setObject(1, city);
            int rowsDeleted = statement.executeUpdate();
	        System.out.println(rowsDeleted + " registros eliminados.");
	        return rowsDeleted;
        }catch(SQLException e) {
        	throw new DataBasesException("No se han podido eliminar los elementos" + e.getMessage());
        }
	}
	
	private POI mapResultSetToPublication(ResultSet resultSet) throws SQLException {
        int poiId = (int) resultSet.getObject("poiId");
        Double latitude = null;
        if (resultSet.getObject("latitude") != null) {
            latitude = resultSet.getDouble("latitude");
        }

        Double longitude = null;
        if (resultSet.getObject("longitude") != null) {
            longitude = resultSet.getDouble("longitude");
        }
        String country = resultSet.getString("country");
        String city = resultSet.getString("city");
        String description = resultSet.getString("description");
        Date update_date = resultSet.getDate("update_date");
        return new POI(poiId, latitude, longitude, country, city, description,update_date );
    }
}

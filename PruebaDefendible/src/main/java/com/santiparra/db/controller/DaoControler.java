package com.santiparra.db.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import com.santiparra.db.JBC.BaseDeDatosDao;
import com.santiparra.db.JBC.BaseDeDatosSingleton;
import com.santiparra.db.mongodb.MongodbDao;
import com.santiparra.db.mongodb.MongodbSingleton;
import com.santiparra.exception.DataBasesException;

import interfaces.InterDAO;
import models.POI;

public class DaoControler implements InterDAO {

	/**
     * Enumeración que define los tipos de base de datos soportados.
     */
	private enum Type {
		SQL, MONGO
	}

	private BaseDeDatosDao baseSql;
	private MongodbDao baseMongo;
	private BaseDeDatosSingleton sqlSingleton;
	private MongodbSingleton mongoSingleton;
	private static Type type = Type.SQL;

	public DaoControler() throws SQLException {
		baseSql = BaseDeDatosDao.getInstance();
	}

	/**
     * Cambia la base de datos activa entre SQL y MongoDB.
     *
     * @throws SQLException si ocurre un error al inicializar la instancia de la base de datos.
     */
	public void changeDB() throws SQLException {
		if (type == Type.SQL) {
			baseMongo = MongodbDao.getInstance();
			type = Type.MONGO;
		} else if (type == Type.MONGO) {
			baseSql = BaseDeDatosDao.getInstance();
			type = Type.SQL;
		}
	}

	/**
     * Cuenta los elementos almacenados en la base de datos activa.
     *
     * @return el número de elementos en la base de datos.
     */
	@Override
	public int countElement() {
		if (type == Type.SQL) {
			return baseSql.countElement();
		}
		return baseMongo.countElement();

	}

	/**
     * Agrega un nuevo POI (Point of Interest) a la base de datos activa.
     *
     * @param poi el POI a agregar.
     * @return {@code true} si la operación fue exitosa, {@code false} en caso contrario.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public boolean addPoi(POI poi) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.addPoi(poi);
		}
		return baseMongo.addPoi(poi);
	}

	/**
     * Muestra todos los POIs almacenados en la base de datos activa.
     *
     * @return una lista de todos los POIs.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */

	@Override
	public ArrayList<POI> showAll() throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.showAll();
		}
		return baseMongo.showAll();
	}

	 /**
     * Muestra un POI por su ID.
     *
     * @param id el ID del POI a buscar.
     * @return el POI encontrado, o {@code null} si no existe.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public POI showOneById(int id) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.showOneById(id);
		}
		return baseMongo.showOneById(id);
	}

	/**
     * Muestra los POIs dentro de un rango de IDs.
     *
     * @param min el ID mínimo.
     * @param max el ID máximo.
     * @return una lista de POIs en el rango especificado.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public ArrayList<POI> showIdRange(int min, int max) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.showIdRange(min, max);
		}
		return baseMongo.showIdRange(min, max);
	}

	 /**
     * Muestra los POIs modificados en un mes específico.
     *
     * @param month el mes de modificación (1-12).
     * @return una lista de POIs modificados en el mes especificado.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public ArrayList<POI> showMonthModification(int month) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.showMonthModification(month);
		}
		return baseMongo.showMonthModification(month);
	}

	/**
     * Muestra los POIs de una ciudad específica.
     *
     * @param city el nombre de la ciudad.
     * @return una lista de POIs en la ciudad especificada.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public ArrayList<POI> showByCity(String city) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.showByCity(city);
		}
		return baseMongo.showByCity(city);
	}

	/**
     * Actualiza un POI identificado por su ID.
     *
     * @param poi el POI con los datos actualizados.
     * @return {@code true} si la operación fue exitosa, {@code false} en caso contrario.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public boolean updatePoiId(POI poi) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.updatePoiId(poi);
		}
		return baseMongo.updatePoiId(poi);
	}

	/**
     * Elimina todos los POIs de la base de datos activa.
     *
     * @param accept {@code true} para confirmar la operación, {@code false} para cancelarla.
     * @return el número de POIs eliminados.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public int dropAll(boolean accept) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.dropAll(accept);
		}
		return baseMongo.dropAll(accept);
	}

	 /**
     * Elimina un POI por su ID.
     *
     * @param id el ID del POI a eliminar.
     * @param accept {@code true} para confirmar la operación.
     * @return el número de POIs eliminados.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public int dropOneById(int id, boolean accept) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.dropOneById(id, accept);
		}
		return baseMongo.dropOneById(id, accept);
	}

	/**
     * Elimina los POIs dentro de un rango de IDs.
     *
     * @param min el ID mínimo.
     * @param max el ID máximo.
     * @param accept {@code true} para confirmar la operación.
     * @return el número de POIs eliminados.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public int dropIdRange(int min, int max, boolean accept) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.dropIdRange(min, max, accept);
		}
		return baseMongo.dropIdRange(min, max, accept);
	}

	/**
     * Elimina los POIs modificados en un mes específico.
     *
     * @param month el mes de modificación (1-12).
     * @param accept {@code true} para confirmar la operación.
     * @return el número de POIs eliminados.
	 * @throws SQLException 
	 * @throws DataBasesException 
     */
	@Override
	public int dropMonthModification(int month, boolean accept) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.dropMonthModification(month, accept);
		}
		return baseMongo.dropMonthModification(month, accept);
	}

	@Override
	public int dropByCity(String city, boolean accept) throws SQLException, DataBasesException {
		if (type == Type.SQL) {
			return baseSql.dropByCity(city, accept);
		}
		return baseMongo.dropByCity(city, accept);
	}
	
	@Override
	public String toString() {
	    if (type == Type.SQL) {
	        return "Base de datos SQL";
	    } else {
	        return "Base de datos MongoDB";
	    }
	}
}

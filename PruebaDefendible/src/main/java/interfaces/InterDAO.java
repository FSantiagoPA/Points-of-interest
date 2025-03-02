package interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.santiparra.exception.DataBasesException;

import models.POI;

public interface InterDAO {
	
	int countElement();
	boolean addPoi(POI poi) throws SQLException, DataBasesException;
	
	ArrayList<POI> showAll() throws SQLException, DataBasesException;
	POI showOneById(int id) throws SQLException, DataBasesException;
	ArrayList<POI> showIdRange(int min, int max) throws SQLException, DataBasesException;
	ArrayList<POI> showMonthModification(int month) throws SQLException, DataBasesException;
	ArrayList<POI> showByCity(String city) throws SQLException, DataBasesException;
	
	boolean updatePoiId(POI poi) throws SQLException,DataBasesException;
	
	int dropAll(boolean accept) throws SQLException,DataBasesException;
	int dropOneById(int id, boolean accept) throws SQLException,DataBasesException;
	int dropIdRange(int min, int max, boolean accept) throws SQLException,DataBasesException;
	int dropMonthModification(int month, boolean accept) throws SQLException,DataBasesException;
	int dropByCity(String city,boolean accept) throws SQLException,DataBasesException;
}

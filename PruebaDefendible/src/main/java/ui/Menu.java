package ui;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import com.santiparra.db.controller.DaoControler;
import com.santiparra.exception.DataBasesException;

import models.POI;

public class Menu {
	private static Scanner scanner = new Scanner(System.in);
	private static DaoControler daoControler;
	
	public static void main(String[] args) throws SQLException {
		daoControler = new DaoControler();
		printMenu();
	    }
	
	public static void printMenu() {
		int option = 0;
		int count = 0;
		do {
		count = daoControler.countElement();
		System.out.println("");
		System.out.println("*********************************");
		System.out.println("******* Choose an option ********");
		System.out.println("*********************************");
		System.out.println("|1. Añadir nuevos elementos      |");
		System.out.println("|2. Listar ítems ordenados por ID|");
		System.out.println("|3. Actualizar un ítem por ID    |");
		System.out.println("|4. Borrar ítems                 |");
		System.out.println("|5. Sincronizar bases de datos   |");
		System.out.println("|6. Cambiar base de datos        |");
		System.out.println("|0. Salir                        |");
		System.out.println("**********************************");
		System.out.println("Trabajando acutualmente con: " + daoControler.toString());
		System.out.println("Total de elementos: " + count);
		System.out.println();
        System.out.print("Elige una opción: ");
        
			try {
				option = Integer.parseInt(scanner.nextLine());

				switch (option) {
				case 1:
					agregarElemento();
					break;
				case 2:
					listarItems();
					break;
				case 3:
					actualizarItem();
					break;
				case 4:
					eliminarItems();
					break;
				case 5:
					//sincronizarBD();
					break;
				case 6:
					daoControler.changeDB();
					break;
				case 0:
					System.out.println("BYE...");
					break;
				}
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println("Enter number only" + e);
			}
		} while (option != 0);
	}
	public static void listarItems() {
        System.out.println("=== Listar ítems con filtros ===");
        System.out.println("Selecciona un filtro:");
        System.out.println("1. Todos los ítems");
        System.out.println("2. Buscar por ID");
        System.out.println("3. Buscar por rango de IDs");
        System.out.println("4. Buscar por mes de modificación");
        System.out.println("5. Buscar por ciudad");
        System.out.println("0. Para regresar al menu principal.");
        System.out.println("====================================");
        System.out.print("Elige una opción: ");
        
        int option = 0;

		do {
			try {
				option = Integer.parseInt(scanner.nextLine());

				switch (option) {
				case 1:
					listarTodos();
					break;
				case 2:
					buscarPorID();
					break;
				case 3:
					buscarPorRangoDeIDs();
					break;
				case 4:
					buscarPorMesDeModificacion();
					break;
				case 5:
					buscarPorCiudad();
					break;
				case 0:
					printMenu();
					break;
				}
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println("Enter number only" + e);
			}
		} while (option != 0);
	}
	
	public static void eliminarItems() {
        System.out.println("=== Eliminar ítems ===");
        System.out.println("Selecciona un filtro:");
        System.out.println("1. Todos los ítems");
        System.out.println("2. Eliminar por ID");
        System.out.println("3. Eliminar por rango de IDs");
        System.out.println("4. Eliminar por mes de modificación");
        System.out.println("5. Eliminar por ciudad");
        System.out.println("0. Para regresar al menu principal.");
        System.out.println("====================================");
        System.out.print("Elige una opción: ");
        
        int option = 0;

		do {
			try {
				option = Integer.parseInt(scanner.nextLine());

				switch (option) {
				case 1:
					eliminarTodos();
					break;
				case 2:
					eliminarItemsPorId();
					break;
				case 3:
					eliminarPorRango();
					break;
				case 4:
					eliminarPorMes();
					break;
				case 5:
					eliminarPorCiudad();
					break;
				case 0:
					printMenu();
					break;
				}
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println("Enter number only" + e);
			}
		} while (option != 0);
	}
	
	private static void agregarElemento() throws DataBasesException {
        try {
            System.out.println("\n=== Agregar Nuevo POI ===");
            System.out.print("Ingrese el ID (obligatorio): ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese la latitud (o presione Enter para dejar vacío): ");
            Double latitude = readNullableDouble();

            System.out.print("Ingrese la longitud (o presione Enter para dejar vacío): ");
            Double longitude = readNullableDouble();

            System.out.print("Ingrese el país (o presione Enter para dejar vacío): ");
            String country = readNullableString();

            System.out.print("Ingrese la ciudad (o presione Enter para dejar vacío): ");
            String city = readNullableString();

            System.out.print("Ingrese la descripción (o presione Enter para dejar vacío): ");
            String description = readNullableString();

            System.out.print("Ingrese la fecha de actualización (yyyy-MM-dd, o presione Enter para dejar vacío): ");
            Date updateDate = readNullableDate();

            POI poi = new POI(id, latitude, longitude, country, city, description, updateDate);
            if (daoControler.addPoi(poi)) {
                System.out.println("POI agregado exitosamente.");
            } else {
                System.out.println("Error al agregar el POI.");
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar el POI: " + e.getMessage());
        }
    }
	
	 private static void actualizarItem() throws DataBasesException {
	        try {
	            System.out.println("\n=== Actualizar POI por ID ===");
	            System.out.print("Ingrese el ID del POI a actualizar: ");
	            int id = Integer.parseInt(scanner.nextLine());

	            System.out.print("Ingrese la nueva latitud (o presione Enter para dejar igual): ");
	            Double latitude = readNullableDouble();

	            System.out.print("Ingrese la nueva longitud (o presione Enter para dejar igual): ");
	            Double longitude = readNullableDouble();

	            System.out.print("Ingrese el nuevo país (o presione Enter para dejar igual): ");
	            String country = readNullableString();

	            System.out.print("Ingrese la nueva ciudad (o presione Enter para dejar igual): ");
	            String city = readNullableString();

	            System.out.print("Ingrese la nueva descripción (o presione Enter para dejar igual): ");
	            String description = readNullableString();

	            System.out.print("Ingrese la nueva fecha de actualización (yyyy-MM-dd, o presione Enter para dejar igual): ");
	            Date updateDate = readNullableDate();

	            POI poi = new POI(id, latitude, longitude, country, city, description, updateDate);
	            if (daoControler.updatePoiId(poi)) {
	                System.out.println("POI actualizado exitosamente.");
	            } else {
	                System.out.println("Error al actualizar el POI.");
	            }
	        } catch (SQLException e) {
	        	throw new DataBasesException("Error al actualizar el POI: " + e.getMessage());
	        }
	    }
	 
	 private static void eliminarTodos() throws DataBasesException  {
		 try {
		    System.out.print("¿Estás seguro de eliminar todos los ítems? (true/false): ");
		    boolean confirm = Boolean.parseBoolean(scanner.nextLine());
		    int eliminados = daoControler.dropAll(confirm);
		    System.out.println(eliminados + " ítems eliminados.");
		 }catch(SQLException e) {
			 throw new DataBasesException("Los elementos no fueron eliminados correctamente: " + e.getMessage());
		 }
		}
	 
	 private static void eliminarItemsPorId() throws DataBasesException {
	        try {
	            System.out.println("\n=== Borrar POI ===");
	            System.out.print("Ingrese el ID del POI a borrar: ");
	            int id = Integer.parseInt(scanner.nextLine());

	            System.out.print("¿Está seguro? (true/false): ");
	            boolean confirm = Boolean.parseBoolean(scanner.nextLine());

	            int deleted = daoControler.dropOneById(id, confirm);
	            if (deleted > 0) {
	                System.out.println("POI eliminado exitosamente.");
	            } else {
	                System.out.println("No se eliminó el POI.");
	            }
	        } catch (SQLException e) {
	            throw new DataBasesException("Error al eliminar el POI: " + e.getMessage());
	        }
	    }
	 
	 private static void eliminarPorRango() throws DataBasesException {
		 try {
		    System.out.print("Ingrese el ID mínimo: ");
		    int minId = Integer.parseInt(scanner.nextLine());
		    System.out.print("Ingrese el ID máximo: ");
		    int maxId = Integer.parseInt(scanner.nextLine());
		    System.out.print("¿Está seguro? (true/false): ");
		    boolean confirm = Boolean.parseBoolean(scanner.nextLine());
		    int eliminados = daoControler.dropIdRange(minId, maxId, confirm);
		    System.out.println(eliminados + " ítems eliminados en el rango " + minId + " - " + maxId + ".");
		 }catch(SQLException e) {
			 throw new DataBasesException("Los elementos no fueron eliminados correctamente: " + e.getMessage());
		 }
		}
	 
	 private static void eliminarPorMes() throws DataBasesException {
		 try {
		    System.out.print("Ingrese el mes (1-12): ");
		    int mes = Integer.parseInt(scanner.nextLine());
		    System.out.print("¿Está seguro? (true/false): ");
		    boolean confirm = Boolean.parseBoolean(scanner.nextLine());
		    int eliminados = daoControler.dropMonthModification(mes, confirm);
		    System.out.println(eliminados + " ítems eliminados del mes " + mes + ".");
		 }catch(SQLException e) {
			 throw new DataBasesException("Los elementos no fueron eliminados correctamente: " + e.getMessage());
		 }
		}
	 
	 private static void eliminarPorCiudad() throws DataBasesException {
		 try {
		    System.out.print("Ingrese la ciudad: ");
		    String ciudad = scanner.nextLine();
		    System.out.print("¿Está seguro? (true/false): ");
		    boolean confirm = Boolean.parseBoolean(scanner.nextLine());
		    int eliminados = daoControler.dropByCity(ciudad, confirm);
		    System.out.println(eliminados + " ítems eliminados de la ciudad " + ciudad + ".");
		 }catch(SQLException e) {
			 throw new DataBasesException("Los elementos no fueron eliminados correctamente: " + e.getMessage());
		 }
		}
	 
	 private static void listarTodos() throws DataBasesException {
		    try {
		        System.out.println("=== Todos los Ítems ===");
		        List<POI> poiList = daoControler.showAll(); // Obtenemos la lista de ítems
		        if (poiList.isEmpty()) {
		            System.out.println("No hay ítems para mostrar.");
		        } else {
		            for (int i = 0; i < poiList.size(); i++) {
		                System.out.println("Ítem " + (i + 1) + ": " + poiList.get(i));
		            }
		        }
		    } catch (SQLException e) {
		        throw new DataBasesException("Error al listar todos los ítems: " + e.getMessage());
		    }
		}
	 
	 private static void buscarPorID() throws DataBasesException {
		    try {
		        System.out.print("Ingrese el ID: ");
		        int id = Integer.parseInt(scanner.nextLine());
		        POI poi = daoControler.showOneById(id);
		        if (poi != null) {
		            System.out.println(poi);
		        } else {
		            System.out.println("No se encontró ningún ítem con el ID especificado.");
		        }
		    } catch (NumberFormatException e) {
		        System.out.println("El ID debe ser un número.");
		    } catch (SQLException e) {
		        throw new DataBasesException("Error al buscar por ID: " + e.getMessage());
		    }
		}
	 
	 private static void buscarPorRangoDeIDs() throws DataBasesException {
		    try {
		        System.out.print("Ingrese el ID mínimo: ");
		        int minId = Integer.parseInt(scanner.nextLine());
		        System.out.print("Ingrese el ID máximo: ");
		        int maxId = Integer.parseInt(scanner.nextLine());
		        daoControler.showIdRange(minId, maxId).forEach(System.out::println);
		    } catch (NumberFormatException e) {
		        System.out.println("Los IDs deben ser números.");
		    } catch (SQLException e) {
		    	throw new DataBasesException("Error al buscar por rango de IDs: " + e.getMessage());
		    }
		}
	 
	 private static void buscarPorMesDeModificacion() throws DataBasesException {
		    try {
		        System.out.print("Ingrese el mes (1-12): ");
		        int mes = Integer.parseInt(scanner.nextLine());
		        List<POI> resultados = daoControler.showMonthModification(mes);

		        if (resultados.isEmpty()) {
		            System.out.println("No se encontraron resultados para el mes: " + mes);
		        } else {
		            for (POI resultado : resultados) {
		                System.out.println(resultado);
		            }
		        }
		    } catch (NumberFormatException e) {
		        System.out.println("El mes debe ser un número entre 1 y 12.");
		    } catch (SQLException e) {
		        throw new DataBasesException("Error al buscar por mes de modificación: " + e.getMessage());
		    }
		}
	 
	 private static void buscarPorCiudad() throws DataBasesException {
		    try {
		        System.out.print("Ingrese el nombre de la ciudad: ");
		        String ciudad = scanner.nextLine();
		        List<POI> resultados = daoControler.showByCity(ciudad);
		        
		        if (resultados.isEmpty()) {
		            System.out.println("No se encontraron resultados para la ciudad: " + ciudad);
		        } else {
		            for (POI resultado : resultados) {
		                System.out.println(resultado);
		            }
		        }
		    } catch (SQLException e) {
		        throw new DataBasesException("Error al buscar por ciudad: " + e.getMessage());
		    }
		}
	
	 private static String readNullableString() {
		    String input = scanner.nextLine();
		    if (input.isEmpty()) {
		        return null;
		    } else {
		        return input;
		    }
		}

	 private static Double readNullableDouble() {
		    try {
		        String input = scanner.nextLine();
		        if (input.isEmpty()) {
		            return null;
		        } else {
		            return Double.parseDouble(input);
		        }
		    } catch (NumberFormatException e) {
		        System.out.println("Por favor, ingrese un número válido.");
		        return readNullableDouble();
		    }
		}

		private static Date readNullableDate() {
		    try {
		        String input = scanner.nextLine();
		        if (input.isEmpty()) {
		            return null;
		        } else {
		            return java.sql.Date.valueOf(input); // Convierte de String a Date
		        }
		    } catch (Exception e) {
		        System.out.println("Fecha no válida. Intenta de nuevo.");
		        return readNullableDate();
		    }
		}
}

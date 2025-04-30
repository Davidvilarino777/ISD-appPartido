# Anotaciones Iteración 1

David Vilariño: Debido al motivo de colisiones y para hacer merge tuve que hacer un commit ([VARIOS]Commit para hacer pull)con la implementación de la funcionalidad 2, los cuales tenían que estar subidos por separado, de la siguiente manera:
	- AppServiceTest.java -> [FUNC-2]: Añadida prueba de ejecución para el caso de uso de búsqueda de partido entre fechas.
	- AbstractSqlPartidoDao -> [FUNC-2]: Añadida implementación de la función del DAO para el caso de uso de búsqueda de partido 			 entre fechas.
	 PartidoServiceImpl.java -> [FUNC-2]: Añadida implementación de la función del Servide (getPartidosPorFecha) para el caso de uso de		 búsqueda de partido entre fechas.
  
Raúl López: Debido a un problema de interpretación con el formato el commit [Varios] Añadida entidad Compra entrada, tendrían que ser dos por serparado:
	- [Varios] Añadida entidad Compra entrada
	- [Varios] Añadida entidad Partido
			
	El commit [Varios] Entidad Partido cambios en el constructor tendrían que ser dos separados:
		- [Varios] Entidad Partido cambios en el constructor
		- [Varios] Creado MySQLCreateTables.sql	
	El commit [FUNC-3] Implementada la operacion findId y remove en el DAO de partido tendrían que ser 3 por separado:
		- [FUNC-3] Implementada la operacion findId y remove en el DAO de partido 
		- [FUNC-3] Implementada getPartido en servicio de la capa Modelo
		- [FUNC-3] Añadida prueba de ejecución para getPartido
  
Manuel Fontenlos Mato:
	-El segundo,el tercero y el cuarto commit con el nombre: "Añadidos ficheros de implementación para el Dao de CompraEntrada" son erróneos 	(desestimar).
  	-El commit "Merge remote-tracking branch 'origin/master'" es tambien erróneo (desestimar).

# ws-app

## Installing the development environment

- [Instructions for Unix-like operating systems (spanish)] (https://github.com/udc-fic-isd/isd-entorno/blob/main/LEEME_UNIX.md).
- [Instructions for Windows (spanish)] (https://github.com/udc-fic-isd/isd-entorno/blob/main/LEEME_WINDOWS.md).

## Initializing the database and building the project

	mvn sql:execute install

## Running the project

It requires the database server to be running.

### Running the service with Maven/Jetty

	cd ws-app-service
	mvn jetty:run

### Running the service with Tomcat

- Copy the `.war` file (`ws-app-service/target/ws-app-service.war`)
  to Tomcat's `webapps` directory.

- Start Tomcat:

      cd <TOMCAT_HOME>/bin
      startup.sh

- Shutdown Tomcat:

      shutdown.sh

### Running the client application

Configure `ws-app-client/src/main/resources/ConfigurationParameters.properties`
to specify the client implementation (REST or Thrift) to be used and
the port number of the web server in the `endpointAddress` property
(9090 for Jetty, 8080 for Tomcat)

	cd ws-app-client

- Execute the client 

      mvn exec:java -Dexec.mainClass="..." -Dexec.args="..."


## Autora: Mauro Tapia

## Ejecución de la aplicación
1. Ejecutar mvn clean install en la carpeta raíz del proyecto
2. Ejecutar docker-compose up --build para arrancar los contenedores
3. Prueba de aplicación en:
   - Creación de promocion: http://localhost:8090/swagger-ui/index.html
   - Creación de productos de mate http://localhost:8091/swagger-ui/index.html
   - Consulta de productos de mate: http://localhost:8096/swagger-ui/index.html

NOTA: Al levantar todos los contenedores se van a auto crear los topics, 
este proyecto es solo una demo ya que se deberían configurar.
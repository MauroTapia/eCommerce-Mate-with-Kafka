import argparse
import logging
from report.api_client import APIClient
from report.data_processor import DataProcessor
from report.excel_exporter import ExcelExporter
from repoblation.topic_repopulator import verify_and_repopulate_topic
from repoblation.config import DB_CONFIG, KAFKA_CONFIG
from repoblation.avro_producer import AvroProducerHelper 
from repoblation.topic_repopulator import agrupar_por_categoria, get_products

# Configuración básica de logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")
logger = logging.getLogger(__name__)

def generar_reporte(output_file="reporte.xlsx"):
    """
    Genera un reporte en Excel a partir de los datos de productos y ventas.
    """
    logger.info("Iniciando generación de reporte...")

    try:
        # Consumir datos desde los endpoints
        logger.info("Obteniendo datos de productos...")
        productos = APIClient.get_productos()
        if not productos:
            logger.warning("No se recibieron datos de productos.")

        logger.info("Obteniendo datos de ventas...")
        ventas = APIClient.get_ventas()
        if not ventas:
            logger.warning("No se recibieron datos de ventas.")

        # Procesar datos
        logger.info("Procesando datos de productos...")
        df_productos = DataProcessor.procesar_productos(productos)

        logger.info("Procesando datos de ventas...")
        df_ventas = DataProcessor.procesar_ventas(ventas)

        if df_ventas.empty:
            logger.warning("No se encontraron datos procesados de ventas. No se generará el reporte.")
            return

        logger.info("Generando estadísticas de ventas...")
        estadisticas = DataProcessor.generar_estadisticas_ventas(df_ventas)

        # Guardar datos en Excel
        logger.info(f"Guardando datos en el archivo {output_file}...")
        ExcelExporter.guardar_en_excel(
            {
                "Productos": df_productos,
                "Ventas": df_ventas,
                "Estadísticas": estadisticas
            },
            output_file
        )
        logger.info(f"Reporte generado exitosamente: {output_file}")

    except Exception as e:
        logger.error(f"Error durante la generación del reporte: {e}")
        raise

def iniciar_servidor():
    """
    Inicia un servidor web (puedes implementar esta función según tus necesidades).
    """
    logger.info("Iniciando servidor web...")
    # Aquí puedes agregar la lógica para iniciar el servidor
    logger.info("Servidor web iniciado.")


def main():
    # Configurar el parser de argumentos
    parser = argparse.ArgumentParser(description="Herramienta para generar reportes, repoblar topics de Kafka o iniciar un servidor.")
    parser.add_argument('--reporte', action='store_true', help="Generar reporte en Excel")
    parser.add_argument('--output-file', type=str, default="reporte.xlsx", help="Nombre del archivo de salida para el reporte")
    parser.add_argument('--servidor', action='store_true', help="Iniciar servidor web")
    parser.add_argument('--repoblar-topic', action='store_true', help="Repoblar topic de Kafka")
    parser.add_argument('--topic-name', type=str, default="productos-mate-tipo", help="Nombre del topic de Kafka a repoblar")

    # Parsear los argumentos
    args = parser.parse_args()

    try:
        # Inicializa el productor de Avro
        avro_producer_helper = AvroProducerHelper(
            bootstrap_servers=KAFKA_CONFIG['bootstrap_servers'],  # Usa tu configuración de Kafka
            schema_registry_url=KAFKA_CONFIG['schema_registry_url']  # Usa tu configuración de Schema Registry
        )
        
        # Registra los esquemas
        avro_producer_helper.register_schemas()

        if args.reporte:
            # Llama a la función para generar el reporte en Excel
            generar_reporte(args.output_file)
        elif args.servidor:
            # Llama a la función para iniciar el servidor web
            iniciar_servidor()
        elif args.repoblar_topic:
            # Llama a la función que repuebla el topic con datos de la base de datos
            logger.info(f"Iniciando repoblación del topic '{args.topic_name}'...")
            verify_and_repopulate_topic(args.topic_name, DB_CONFIG, KAFKA_CONFIG)
        else:
            logger.warning("Por favor, selecciona una opción: --reporte, --servidor o --repoblar-topic")
    except Exception as e:
        logger.error(f"Error en la ejecución: {e}")

if __name__ == '__main__':
    main()

import argparse
import logging
from report.api_client import APIClient
from report.data_processor import DataProcessor
from report.excel_exporter import ExcelExporter

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

        # Procesar los datos tal cual
        logger.info("Procesando datos de productos...")
        df_productos = DataProcessor.procesar_productos(productos)

        logger.info("Procesando datos de ventas...")
        df_ventas = DataProcessor.procesar_ventas(ventas, df_productos)

        # Guardar datos en Excel
        logger.info(f"Guardando datos en el archivo {output_file}...")
        ExcelExporter.guardar_en_excel(
            {
                "Productos": df_productos,
                "Ventas": df_ventas
            },
            output_file
        )
        logger.info(f"Reporte generado exitosamente: {output_file}")

    except Exception as e:
        logger.error(f"Error durante la generación del reporte: {e}")
        raise

def main():
    # Configurar el parser de argumentos
    parser = argparse.ArgumentParser(description="Generar reportes en Excel")
    parser.add_argument('--reporte', action='store_true', help="Generar reporte en Excel")
    parser.add_argument('--output-file', type=str, default="reporte.xlsx", help="Nombre del archivo de salida para el reporte")

    # Parsear los argumentos
    args = parser.parse_args()

    if args.reporte:
        # Llama a la función para generar el reporte en Excel
        generar_reporte(args.output_file)
    else:
        logger.warning("Por favor, selecciona la opción --reporte")

if __name__ == '__main__':
    main()

import os
import datetime
from flask import Blueprint, send_file
from services.api_client import APIClient
from services.data_processor import DataProcessor
from services.excel_exporter import ExcelExporter

report_bp = Blueprint('report', __name__)
@report_bp.route('/api/reporte', methods=['GET'])
def generar_reporte():
    productos = APIClient.get_productos()
    ventas = APIClient.get_ventas()

    df_productos = DataProcessor.procesar_productos(productos)
    df_ventas = DataProcessor.procesar_ventas(ventas, df_productos)

    estadisticas = DataProcessor.generar_estadisticas_ventas(df_ventas)

    print("📊 Estadísticas de ventas:", estadisticas.head())

    static_folder = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'static')
    if not os.path.exists(static_folder):
        os.makedirs(static_folder) 

    fecha_hora = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
    nombre_archivo = f"reporte_{fecha_hora}.xlsx"
    ruta_reporte = os.path.join(static_folder, nombre_archivo)

    dfs = {'Productos': df_productos, 'Ventas': df_ventas, 'Estadísticas': estadisticas}
    ExcelExporter.guardar_en_excel(dfs, ruta_reporte)

    return send_file(ruta_reporte, as_attachment=True, download_name=nombre_archivo)
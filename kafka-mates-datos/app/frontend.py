from flask import Flask, render_template, request
from services.api_client import APIClient
from services.data_processor import DataProcessor
from services.excel_exporter import ExcelExporter

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/generar_reporte', methods=['POST'])
def generar_reporte():
    # Consumir API
    productos = APIClient.get_productos()
    ventas = APIClient.get_ventas()

    # Procesar datos
    df_productos = DataProcessor.procesar_productos(productos)
    df_ventas = DataProcessor.procesar_ventas(ventas)
    estadisticas = DataProcessor.generar_estadisticas_ventas(df_ventas)

    # Guardar en Excel
    dfs = {'Productos': df_productos, 'Ventas': df_ventas, 'Estadísticas': estadisticas}
    ExcelExporter.guardar_en_excel(dfs, 'reporte.xlsx')

    return "Reporte generado: <a href='/static/reporte.xlsx'>Descargar</a>"

if __name__ == '__main__':
    app.run(debug=True)

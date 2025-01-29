import pandas as pd
from datetime import datetime, timedelta

class DataProcessor:
    @staticmethod
    def procesar_ventas(data):
        if not data:
            print("Advertencia: No se recibieron datos de ventas.")
            return pd.DataFrame()

        df = pd.DataFrame(data)
        # Verifica que existan las columnas necesarias
        if 'categoria' not in df.columns or 'precioConImpuesto' not in df.columns:
            print("Advertencia: Falta información en los datos de ventas.")
            return pd.DataFrame()

        # Calcula el monto total por categoría
        df['monto'] = df['cantidad'] * df['precioConImpuesto']
        return df

    @staticmethod
    def procesar_productos(data):
        if not data:
            print("Advertencia: No se recibieron datos de productos.")
            return pd.DataFrame()

        df = pd.DataFrame(data)
        # Verifica que existan las columnas necesarias
        if 'categoria' not in df.columns or 'precioConImpuesto' not in df.columns:
            print("Advertencia: Falta información en los datos de productos.")
            return pd.DataFrame()

        # Si necesitas hacer algún cálculo o transformación, lo puedes agregar aquí
        return df

    @staticmethod
    def generar_estadisticas_ventas(df):
        if df.empty:
            print("Advertencia: Datos insuficientes para generar estadísticas.")
            return pd.DataFrame()

        if 'categoria' not in df.columns or 'monto' not in df.columns:
            print("Advertencia: Falta información para agrupar ventas por categoría.")
            return pd.DataFrame()

        # Agrupa por categoría y calcula el total de monto
        return df.groupby('categoria')['monto'].sum().reset_index()

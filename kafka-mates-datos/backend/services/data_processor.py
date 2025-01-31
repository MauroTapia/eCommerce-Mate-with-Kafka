import pandas as pd

class DataProcessor:
    @staticmethod
    def procesar_ventas(data, productos_df):
        if not data:
            print("Advertencia: No se recibieron datos de ventas.")
            return pd.DataFrame()

        df = pd.DataFrame(data)

        return df

    @staticmethod
    def procesar_productos(data):
        if not data:
            print("Advertencia: No se recibieron datos de productos.")
            return pd.DataFrame()

        return pd.DataFrame(data)

    @staticmethod
    def generar_estadisticas_ventas(df):
        # Agrupar por 'categoria' y calcular las sumas de cantidad y precios
        total_por_categoria = df.groupby('categoria').agg({
            'cantidad': 'sum',
            'precioConImpuesto': 'sum',
            'precioPromocionado': 'sum'
        }).reset_index()  # reset_index para mantener 'categoria' como columna y no como índice

        # Calcular el precio promedio
        total_por_categoria['precioPromocionadoPromedio'] = total_por_categoria['precioPromocionado'] / total_por_categoria['cantidad']
        total_por_categoria['precioConImpuestoPromedio'] = total_por_categoria['precioConImpuesto'] / total_por_categoria['cantidad']

        # Devolver el DataFrame con las estadísticas
        return total_por_categoria

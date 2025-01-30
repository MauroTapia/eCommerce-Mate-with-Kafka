import pandas as pd

class DataProcessor:
    @staticmethod
    def procesar_ventas(data, productos_df):
        if not data:
            print("Advertencia: No se recibieron datos de ventas.")
            return pd.DataFrame()

        # Convertir los datos de ventas a un DataFrame
        df = pd.DataFrame(data)

        # Si necesitas agregar datos adicionales de los productos (como precio), haces un merge
        # En este caso, solo estamos combinando para tener los datos completos.
        df = df.merge(productos_df[['categoria', 'producto', 'precioConImpuesto', 'precioPromocionado']], 
                      on=['categoria', 'producto'], how='left')

        return df

    @staticmethod
    def procesar_productos(data):
        if not data:
            print("Advertencia: No se recibieron datos de productos.")
            return pd.DataFrame()

        # Convertir los datos de productos a un DataFrame
        return pd.DataFrame(data)

    @staticmethod
    def generar_estadisticas_ventas(df):
        return df  # Solo devolver los datos tal como están (sin agrupar ni transformar)

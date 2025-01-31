import pandas as pd

class DataProcessor:
    @staticmethod
    def procesar_ventas(data, productos_df):
        if not data:
            print("⚠️ Advertencia: No se recibieron datos de ventas.")
            return pd.DataFrame(columns=['categoria', 'identificador', 'producto', 'cantidad', 'precioConImpuesto', 'precioPromocionado'])

        df_ventas = pd.DataFrame(data)

        df_ventas = df_ventas.merge(productos_df[['identificador', 'precioConImpuesto', 'precioPromocionado']], 
                                    on='identificador', how='left', suffixes=('', '_producto'))
        
        df_ventas['precioConImpuesto'].fillna(df_ventas.pop('precioConImpuesto_producto'), inplace=True)
        df_ventas['precioPromocionado'].fillna(df_ventas.pop('precioPromocionado_producto'), inplace=True)

        return df_ventas

    @staticmethod
    def procesar_productos(data):
        if not data:
            print("⚠️ Advertencia: No se recibieron datos de productos.")
            return pd.DataFrame(columns=['identificador', 'categoria', 'producto', 'precioConImpuesto', 'precioPromocionado', 'promocionTimestamp'])

        return pd.DataFrame(data)

    @staticmethod
    def generar_estadisticas_ventas(df):
        if df.empty:
            print("⚠️ Advertencia: No hay datos de ventas para generar estadísticas.")
            return pd.DataFrame(columns=['categoria', 'total_vendido', 'ingreso_total', 'precioPromocionadoPromedio'])

        df['ingreso_total'] = df['cantidad'] * df['precioPromocionado']

        estadisticas = df.groupby('categoria').agg(
            total_vendido=('cantidad', 'sum'),
            ingreso_total=('ingreso_total', 'sum'),
            precioPromocionadoPromedio=('precioPromocionado', 'mean')
        ).reset_index()

        return estadisticas
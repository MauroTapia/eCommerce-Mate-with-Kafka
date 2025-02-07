import pandas as pd

class DataProcessor:
    @staticmethod
    def procesar_ventas(data, productos_df):
        if not data:
            print("⚠️ Advertencia: No se recibieron datos de ventas.")
            return pd.DataFrame(columns=[
                'identificador', 'identificadorVenta',
                'cantidad', 'precioConImpuesto', 'precioPromocionado'
            ])
        
        df_ventas = pd.DataFrame(data)
        
        # Realizar merge solo con las columnas disponibles en productos_df
        # Se asume que productos_df contiene los campos 'identificador', 'precioConImpuesto' y 'precioPromocionado'
        df_ventas = df_ventas.merge(
            productos_df[['identificador', 'precioConImpuesto', 'precioPromocionado']],
            on='identificador', 
            how='left', 
            suffixes=('', '_producto')
        )
        
        # Si en la venta no se especificaron los precios, se rellenan con los del producto
        df_ventas['precioConImpuesto'] = df_ventas['precioConImpuesto'].fillna(df_ventas['precioConImpuesto_producto'])
        df_ventas['precioPromocionado'] = df_ventas['precioPromocionado'].fillna(df_ventas['precioPromocionado_producto'])
        
        # Se eliminan las columnas auxiliares
        df_ventas.drop(columns=['precioConImpuesto_producto', 'precioPromocionado_producto'], inplace=True)
        
        return df_ventas

    @staticmethod
    def procesar_productos(data):
        if not data:
            print("⚠️ Advertencia: No se recibieron datos de productos.")
            return pd.DataFrame(columns=[
                'identificador', 'precioConImpuesto', 'precioPromocionado', 'promocionTimestamp'
            ])
        return pd.DataFrame(data)

    @staticmethod
    def generar_estadisticas_ventas(df):
        if df.empty:
            print("⚠️ Advertencia: No hay datos de ventas para generar estadísticas.")
            return pd.DataFrame(columns=[
                'identificador', 'total_vendido', 'ingreso_total', 'precioPromocionadoPromedio'
            ])
        
        # Calcular el ingreso total de cada registro de venta
        df['ingreso_total'] = df['cantidad'] * df['precioPromocionado']
        
        # Agrupar por 'identificador' ya que es el id del producto
        estadisticas = df.groupby('identificador').agg(
            total_vendido=('cantidad', 'sum'),
            ingreso_total=('ingreso_total', 'sum'),
            precioPromocionadoPromedio=('precioPromocionado', 'mean')
        ).reset_index()
        
        return estadisticas

import pandas as pd

class ExcelExporter:
    @staticmethod
    def guardar_en_excel(dfs, nombre_archivo):
        """
        Guarda múltiples DataFrames en un archivo Excel.
        """
        try:
            with pd.ExcelWriter(nombre_archivo, engine='openpyxl') as writer:
                for nombre_hoja, df in dfs.items():
                    if isinstance(df, pd.DataFrame) and not df.empty:
                        df.to_excel(writer, sheet_name=nombre_hoja, index=False)
                    else:
                        print(f"Advertencia: La hoja {nombre_hoja} está vacía o no es un DataFrame.")
            print(f"Archivo Excel guardado exitosamente: {nombre_archivo}")
        except Exception as e:
            print(f"Error al guardar el archivo Excel: {e}")

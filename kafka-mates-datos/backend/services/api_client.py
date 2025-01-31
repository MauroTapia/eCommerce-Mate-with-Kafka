import requests

class APIClient:
    BASE_URL_PRODUCTOS = "http://localhost:8096"
    BASE_URL_VENTAS = "http://localhost:8089"

    @staticmethod
    def get_productos():
        try:
            response = requests.get(f"{APIClient.BASE_URL_PRODUCTOS}/productosMate")
            response.raise_for_status()
            return response.json()
        except requests.RequestException as e:
            print(f"Error al obtener productos: {e}")
            return []

    @staticmethod
    def get_ventas():
        try:
            response = requests.get(f"{APIClient.BASE_URL_VENTAS}/ventasMate")
            response.raise_for_status()
            return response.json()
        except requests.RequestException as e:
            print(f"Error al obtener ventas: {e}")
            return []

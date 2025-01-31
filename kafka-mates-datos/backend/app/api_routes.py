from flask import Blueprint, jsonify
from services.api_client import APIClient

bp = Blueprint('api', __name__)

@bp.route('/api/productos', methods=['GET'])
def get_productos():
    productos = APIClient.get_productos()
    return jsonify(productos)

@bp.route('/api/ventas', methods=['GET'])
def get_ventas():
    ventas = APIClient.get_ventas()
    return jsonify(ventas)

import psycopg2
from confluent_kafka import Consumer, KafkaException
import logging
from repoblation.avro_producer import AvroProducerHelper
import requests
import json
from collections import defaultdict

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# URL del Schema Registry
SCHEMA_REGISTRY_URL = "http://localhost:8081"  # Ajusta si es necesario

# Nombre del topic y el schema
TOPIC_NAME = "productos-mate-tipo-value"  # Asegúrate de que este nombre coincida con el que usas en Kafka

def is_topic_empty(consumer, topic):
    """
    Verifica si el topic de Kafka está vacío.
    """
    consumer.subscribe([topic])
    try:
        msg = consumer.poll(1.0)
        if msg is None:
            return True  # No hay mensajes en el topic
        elif msg.error():
            raise KafkaException(msg.error())
        else:
            return False  # El topic tiene mensajes
    finally:
        consumer.close()

def get_products(db_config):

    try:
        connection = psycopg2.connect(**db_config)
        with connection.cursor() as cursor:
            cursor.execute("""
                SELECT identificador, producto, categoria, precio_con_impuesto, precio_promocionado 
                FROM productos
            """)
            rows = cursor.fetchall()
            return [
                {
                    "identificador": row[0],
                    "producto": row[1],
                    "categoria": row[2],
                    "precioConImpuesto": row[3],
                    "precioPromocionado": row[4],
                }
                for row in rows
            ]
    except Exception as e:
        logger.error(f"Error al obtener productos de la base de datos: {e}")
        raise
    finally:
        if connection:
            connection.close()

def agrupar_por_categoria(productos):
    productos_por_categoria = defaultdict(list)

    for producto in productos:
        categoria = producto["categoria"]
        productos_por_categoria[categoria].append({
            "identificador": producto["identificador"],
            "producto": producto["producto"],
            "precioConImpuesto": producto["precioConImpuesto"],
            "precioPromocionado": producto["precioPromocionado"],
            "promocionTimestamp": producto.get("promocionTimestamp", None)
        })

    return productos_por_categoria

def verify_and_repopulate_topic(topic, db_config, kafka_config):
    consumer_config = {
        "bootstrap.servers": kafka_config["bootstrap_servers"],
        "group.id": "topic-checker",
        "auto.offset.reset": "earliest",
    }
    consumer = Consumer(consumer_config)

    avro_producer = AvroProducerHelper(
        bootstrap_servers=kafka_config["bootstrap_servers"],
        schema_registry_url=kafka_config["schema_registry_url"]
    )

    try:
        logger.info(f"Topic a verificar: {topic}")  # Verifica que 'topic' es una cadena válida
        if is_topic_empty(consumer, topic):
            logger.info(f"El topic '{topic}' está vacío. Repoblando...")

            products = get_products(db_config)  # Obtener productos desde la base de datos
            productos_por_categoria = agrupar_por_categoria(products)  # Agrupar los productos por categoría

            # Enviar los productos agrupados a Kafka
            for categoria, productos in productos_por_categoria.items():
                avro_producer.send_to_kafka(topic, categoria, productos)
            
            logger.info("Repoblación completada.")
        else:
            logger.info(f"El topic '{topic}' ya tiene mensajes. No se requiere repoblación.")
    except Exception as e:
        logger.error(f"Error durante la verificación y repoblación del topic: {e}")
        raise
    finally:
        consumer.close()

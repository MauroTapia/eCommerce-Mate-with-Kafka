from confluent_kafka import Producer
from fastavro import schemaless_writer
from io import BytesIO
import logging
import requests
import struct

logger = logging.getLogger(__name__)

class AvroProducerHelper:
    def __init__(self, bootstrap_servers, schema_registry_url):
        self.bootstrap_servers = bootstrap_servers
        self.schema_registry_url = schema_registry_url
        self.producer = self._create_producer()

    def _create_producer(self):
        """
        Crea y configura un productor de Kafka.
        """
        config = {
            'bootstrap.servers': self.bootstrap_servers
        }
        return Producer(config)

    def registrar_esquema(self, subject, schema):
        """
        Registra un esquema en el Schema Registry.
        """
        url = f"{self.schema_registry_url}/subjects/{subject}/versions"
        headers = {"Content-Type": "application/vnd.schemaregistry.v1+json"}
        payload = {"schema": schema}

        response = requests.post(url, headers=headers, json=payload)
        if response.status_code != 200:
            logger.error(f"Error registrando esquema para '{subject}': {response.status_code} - {response.text}")
            raise Exception(f"Error registrando esquema para '{subject}'")
        logger.info(f"Esquema registrado para '{subject}': {response.json()}")

    def _get_schema_id(self, subject):
        """
        Obtiene el ID del esquema desde el Schema Registry.
        """
        url = f"{self.schema_registry_url}/subjects/{subject}/versions/latest"
        response = requests.get(url)
        if response.status_code == 200:
            return response.json()["id"]
        else:
            raise Exception(f"Error obteniendo schema ID para '{subject}': {response.status_code} - {response.text}")

    def _serialize_with_schema_id(self, schema, record, schema_id):
        """
        Serializa un registro Avro con el encabezado que incluye el Schema ID.
        """
        try:
            # Escribe el encabezado con el byte de inicio y el Schema ID
            bytes_writer = BytesIO()
            bytes_writer.write(struct.pack(">bI", 0, schema_id))  # Encabezado Avro
            schemaless_writer(bytes_writer, schema, record)  # Datos serializados
            return bytes_writer.getvalue()
        except Exception as e:
            logger.error(f"Error al serializar el registro con Avro: {e}")
            raise

    def register_schemas(self):
        # Esquemas de la clave y valor
        key_schema = {
            "type": "record",
            "namespace": "com.hiberus.cursos.enviadorproductos.avro",
            "name": "AgrupadorCategoriaKey",
            "fields": [
                {"name": "categoria", "type": "string"}
            ]
        }

        value_schema = {
            "type": "record",
            "namespace": "com.hiberus.cursos.enviadorproductos.avro",
            "name": "AgrupadorCategoriaValue",
            "fields": [
                {
                    "name": "productos",
                    "type": {
                        "type": "array",
                        "items": {
                            "type": "record",
                            "name": "Producto",
                            "fields": [
                                {"name": "identificador", "type": "string"},
                                {"name": "producto", "type": "string"},
                                {"name": "precioConImpuesto", "type": "double"},
                                {"name": "precioPromocionado", "type": "double"},
                                {"name": "promocionTimestamp", "type": ["null", "long"], "default": None}
                            ]
                        }
                    }
                }
            ]
        }

        # Registrar los esquemas
        self.registrar_esquema("productos-mate-tipo-key", key_schema)
        self.registrar_esquema("productos-mate-tipo-value", value_schema)

        logger.info("Esquemas 'productos-mate-tipo-key' y 'productos-mate-tipo-value' registrados correctamente.")

    def serialize_avro_key(self, categoria):
        """
        Serializa la clave (key) en formato Avro.
        """
        KEY_SCHEMA = {
            "type": "record",
            "namespace": "com.hiberus.cursos.enviadorproductos.avro",
            "name": "AgrupadorCategoriaKey",
            "fields": [
                {"name": "categoria", "type": "string"}
            ]
        }
        return {"schema": KEY_SCHEMA, "record": {"categoria": categoria}}

    def serialize_avro_value(self, productos):
        """
        Serializa el valor (value) en formato Avro.
        """
        VALUE_SCHEMA = {
            "type": "record",
            "namespace": "com.hiberus.cursos.enviadorproductos.avro",
            "name": "AgrupadorCategoriaValue",
            "fields": [
                {
                    "name": "productos",
                    "type": {
                        "type": "array",
                        "items": {
                            "type": "record",
                            "name": "Producto",
                            "fields": [
                                {"name": "identificador", "type": "string"},
                                {"name": "producto", "type": "string"},
                                {"name": "precioConImpuesto", "type": "double"},
                                {"name": "precioPromocionado", "type": "double"},
                                {"name": "promocionTimestamp", "type": ["null", "long"], "default": None}
                            ]
                        }
                    }
                }
            ]
        }

        # Normalizar los productos
        productos = [
            {
                "identificador": str(producto["identificador"]),
                "producto": str(producto["producto"]),
                "precioConImpuesto": float(producto["precioConImpuesto"]),
                "precioPromocionado": float(producto["precioPromocionado"]),
                "promocionTimestamp": producto.get("promocionTimestamp", None)
            }
            for producto in productos
        ]

        # Validar datos faltantes
        for producto in productos:
            if any(value is None for value in producto.values()):
                logger.warning(f"Producto con datos faltantes: {producto}")

        return {"schema": VALUE_SCHEMA, "record": {"productos": productos}}

    def send_to_kafka(self, topic, categoria, productos):
        """
        Envía el mensaje serializado a Kafka con la clave 'categoria' y el valor 'productos'.
        """
        try:
            # Serializar clave y valor
            key_data = self.serialize_avro_key(categoria)
            value_data = self.serialize_avro_value(productos)

            # Obtener Schema IDs del Schema Registry
            key_schema_id = self._get_schema_id(f"{topic}-key")
            value_schema_id = self._get_schema_id(f"{topic}-value")

            # Serializar con Schema IDs
            key = self._serialize_with_schema_id(key_data["schema"], key_data["record"], key_schema_id)
            value = self._serialize_with_schema_id(value_data["schema"], value_data["record"], value_schema_id)

            # Enviar mensaje a Kafka
            self.producer.produce(topic=topic, key=key, value=value)
            self.producer.flush()

            logger.info(f"Mensaje enviado a Kafka en el topic '{topic}' con la clave '{categoria}'.")
        except Exception as e:
            logger.error(f"Error enviando mensaje a Kafka: {e}")
            raise

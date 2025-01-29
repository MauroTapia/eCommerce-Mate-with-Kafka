import os
import logging

# Configuración de logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Obtener configuración de la base de datos desde las variables de entorno
DB_CONFIG = {
    "host": os.getenv("DB_HOST", "localhost"),  # Por defecto localhost si no está configurado
    "database": os.getenv("DB_NAME", "mates_db"),
    "user": os.getenv("DB_USER", "user"),
    "password": os.getenv("DB_PASSWORD", "password")
}

# Obtener configuración de Kafka desde las variables de entorno
KAFKA_CONFIG = {
    "bootstrap_servers": os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"),
    "topic_name": os.getenv("KAFKA_TOPIC", "productos-mate-tipo"),
    "schema_registry_url": os.getenv("SCHEMA_REGISTRY_URL", "http://localhost:8081")
}

# Verificación de configuraciones de DB y Kafka
def validate_configurations():
    for key, value in DB_CONFIG.items():
        if not value:
            logger.error(f"Configuración de base de datos no válida para: {key}")
            raise ValueError(f"Configuración de base de datos no válida para: {key}")

    for key, value in KAFKA_CONFIG.items():
        if not value:
            logger.error(f"Configuración de Kafka no válida para: {key}")
            raise ValueError(f"Configuración de Kafka no válida para: {key}")

    logger.info("Todas las configuraciones son válidas.")

validate_configurations()

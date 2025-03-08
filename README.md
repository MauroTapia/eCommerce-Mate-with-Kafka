# Kafka Mates Hiberus - Guía de Ejecución
# PRESENTACION EN /eCommerce-Mate-with-Kafka/PROYECTO KAFKA.PDF

Este documento proporciona las instrucciones necesarias para ejecutar la aplicación Kafka Mates Hiberus. La aplicación está dockerizada y cuenta con un backend en Python y un frontend en React.

## Requisitos previos

Antes de comenzar, asegúrate de tener instalados los siguientes requisitos:

- [Docker](https://www.docker.com/get-started)
- [Python 3.x](https://www.python.org/downloads/)
- [Node.js y npm](https://nodejs.org/)

## Pasos para la ejecución

### 1. Iniciar los contenedores con Docker

Dentro del directorio donde se encuentra el archivo `docker-compose.yml`, ejecuta:

```sh
docker-compose up -d
```

Este comando iniciará los servicios necesarios, incluyendo Kafka.

### 2. Ejecutar el backend (Python)

Navega hasta la carpeta `kafka-mates-datos` y ejecuta el siguiente comando:

```sh
python app.py
```

Esto iniciará el backend que se comunicará con Kafka.

### 3. Ejecutar el frontend (React)

Dentro de la misma carpeta `kafka-mates-datos`, navega hasta el directorio del frontend y ejecuta:

```sh
npm install  # Solo la primera vez, para instalar dependencias
npm start
```

Esto levantará la interfaz de usuario de la aplicación.

### 4. Acceder a la aplicación

Una vez iniciado el frontend, puedes acceder a la aplicación desde tu navegador en:

```
http://localhost:3000
```

El backend correrá por defecto en el puerto `5000`, salvo que se indique lo contrario en `app.py`.

## Detener la aplicación

Para detener todos los servicios dockerizados, ejecuta:

```sh
docker-compose down
```

Si deseas detener los procesos manualmente:

- Para el backend, usa `Ctrl + C` en la terminal donde se ejecuta `app.py`.
- Para el frontend, usa `Ctrl + C` en la terminal donde se ejecuta `npm start`.

## Notas adicionales

- Asegúrate de que los puertos necesarios no estén en uso antes de iniciar la aplicación.
- Verifica que Kafka esté corriendo correctamente antes de probar la aplicación.
- Puedes modificar la configuración en los archivos correspondientes si deseas cambiar los puertos o la configuración de Kafka.

---

Con estas instrucciones, deberías poder ejecutar la aplicación sin problemas. ¡Disfruta trabajando con Kafka Mates Hiberus!


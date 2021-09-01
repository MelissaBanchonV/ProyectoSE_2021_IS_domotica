# Diseño e implementación de un sistema de seguridad domótico con Raspberry Pi y una aplicación móvil.
## Proyecto / Grupo 4
## Integrantes:
* Banchón V. Melissa
* Lazo R. Brandon 

Script para la activación de la cámara https://www.sigmdel.ca/michel/ha/rpi/streaming_en.html

Instalación de mqtt en Raspberry https://www.emqx.com/en/blog/use-mqtt-with-raspberry-pi
 - python3 --version
 - sudo apt install python3
 - git clone https://github.com/eclipse/paho.mqtt.python 
 - cd paho.mqtt.python 
 - python3 setup.py install
 - pip3 install paho-mqtt

Inicialización de la comunicación 
 - sudo python3 main.py

En la carpeta ProyectoSE_2021IS se encuentra el proyecto de Android Studio correspondiente. La configuración de la IP de la raspberry para la conexión de la cámara debe realizarse tanto en exteriorFragment como en camera. 

JARVISec.apk es la aplicación de celular. 

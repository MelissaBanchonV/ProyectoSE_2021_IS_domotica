#!/usr/bin/env python3

from pygame import mixer
from subprocess import call
import RPi.GPIO as GPIO
from time import sleep

def action(topic,message):
    print(topic)
    print(message)
    sep = topic.split("/")
    if sep[1] == "cocina":
       print("luces de la cocina")
       luces(message,20)
    elif sep[1] == "sala":
       print("luces de la sala")
       luces(message,16)
    elif sep[1] == "dormitorio":
       print("luces del dormitorio")
       luces(message,13)
    elif sep[1] == "bano":
       print("luces del baño")
       luces(message,21)
    elif sep[1] == "exterior":
       if sep[2] == "luz":
          print("luces exteriores")
          luces(message,19)
       elif sep[2] == "motor":
           print("motor exterior")
           motor(message)
       elif sep[2] == "audio":
           text = "./speech.sh "
           call([text+message], shell=True)
    elif sep[1] == "todo":
       luces(message,16)
       luces(message,19)
       luces(message,20)
       luces(message,13)
       luces(message,21) 

def luces(action,led):
    if action == "encender":
       print("ENCENDIDO",led)
       GPIO.output(led,False)
    elif action == "apagar":
       print("APAGADO",led)
       GPIO.output(led,True)

def motor(action):
    servo= GPIO.PWM(26, 50) #Para crear una instancia de PWM (Canal,frecuencia)
    servo.start(2.5)
    if action == "abrir":
         print("cerradura abierta")
         for i in range(100, -1, -1):
             servo.ChangeDutyCycle(100-i) # Para cambiar el ciclo de trabajo de 0 a 100
             sleep(0.02)
    elif action == "cerrar":
         print("cerradura cerrada")
         for i in range(100, -1, -1):
             servo.ChangeDutyCycle(i) # Para cambiar el ciclo de trabajo de 0 a 100
             sleep(0.02)

def setup():
      GPIO.setmode(GPIO.BCM)
      GPIO.setup(16, GPIO.OUT)
      GPIO.setup(19, GPIO.OUT)
      GPIO.setup(20, GPIO.OUT)
      GPIO.setup(13, GPIO.OUT)
      GPIO.setup(26, GPIO.OUT)
      GPIO.setup(21, GPIO.OUT)
    

setup()


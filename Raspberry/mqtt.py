#!/usr/bin/env python3

import paho.mqtt.client as mqtt
from control import *
import time
from pygame import mixer

############
def on_message(client, userdata, message):
    print("message received " ,str(message.payload.decode("utf-8")))
    print("message topic=",message.topic)
    print("message qos=",message.qos)
    print("message retain flag=",message.retain)
    action(message.topic,str(message.payload.decode("utf-8")))

########################################

def run():
    client.subscribe("nuGWuCsEaAPeld7/#")
    time.sleep(4) # wait
    #client.loop_stop() #stop the loop

broker_address="broker.hivemq.com"
print("creating new instance")
client = mqtt.Client("Raspi") #create new instance
client.on_message=on_message #attach function to callback
print("connecting to broker")
client.connect(broker_address) #connect to broker
client.loop_start() #start the loop
print("Subscribing to topic","nuGWuCsEaAPeld7")

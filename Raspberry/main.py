from mqtt import *

try:
   while True:
      run()
except KeyboardInterrupt:
   GPIO.cleanup()


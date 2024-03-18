import socket
import time
import os

port = 5432

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
while True:
    try:
        s.connect(('localhost', port))
        s.close()
        break
    except socket.error as ex:
        time.sleep(1)
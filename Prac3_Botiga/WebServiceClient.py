import urllib2
import getpass

ip = raw_input("IP:")
port=raw_input("Port:")
user=raw_input("Usuari:")
password=getpass.getpass("Contrasenya:")
saldo=raw_input("Quantitat a afegir:")

response = urllib2.urlopen("http://"+ip+":"+port+"/Botiga/augsaldo?user="+user+"&pass="+password+"&saldo="+saldo)
print response.read()

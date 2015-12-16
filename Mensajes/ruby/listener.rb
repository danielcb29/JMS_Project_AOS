#Envio de mensajes ruby
#Daniel Correa, Cristina Extremera
#Lado listener, encargado de la escucha de mensajes
 
require 'rubygems'
require 'stomp'

#Variables de configuracion para la comunicacion
user = ENV["APOLLO_USER"] || "admin"
password = ENV["APOLLO_PASSWORD"] || "password"
host = ENV["APOLLO_HOST"] || "localhost"
port = ENV["APOLLO_PORT"] || 61613
destination = $*[0] || "/topic/event"

#Conexion bajo STOMP
conn = Stomp::Connection.open user, password, host, port, false 
#Contador de mensajes
count = 0

#Suscripcion para escucha de mensajes
conn.subscribe destination, { :ack =>"auto" }

#Conteo de tiempo
start = Time.now

#Informe de que estas listo para recibir mensajes
puts "Estas listo para recibir mensajes!"

#Ciclo que permite recibir mensajes constantemente
while true 
	#Mensaje recibido
	msg = conn.receive

	if msg.command == "MESSAGE" 
		if msg.body == "SHUTDOWN"
			#Si el mensaje es shutdown se termina la conexion
			diff = Time.now - start
			puts "El envio de mensajes ha finalizado!"
			puts "Recibidos #{count} mensajes en #{diff} segundos"
			exit 0
	
		else
			#Caso contrario se cuenta el mensaje y se muestra
		  	start = Time.now if count==0 
			count += 1;
			puts "Has recibido: " + msg.body
		end
	else
		#Si lo que recibimos no es un mensaje informamos que es
 		puts "#{msg.command}: #{msg.body}"
	end
end
#Cierre de la conexion
conn.disconnect

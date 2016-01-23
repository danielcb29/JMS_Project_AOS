#Envio de mensajes ruby
#Daniel Correa, Cristina Extremera
#Lado publisher, encargado del envio de mensajes
 
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

#Informe de que ya se pueden enviar mensajes en consola
puts "El envio de mensajes ha iniciado!, digita el mensaje y pulsa enter"

#Ciclo que permite hacer el envio constante de mensajes
while true
	#Se obtiene una linea escrita en consola
	msn = gets.chomp
	
	#Informe del mensaje que has enviado
	puts "Tu has enviado: " + msn

	#Envio del mensaje
	conn.publish destination, msn, {'persistent'=>'false'}

	#Si el mensaje es SHUTDOWN se termina la comunicacion
	if msn == "SHUTDOWN"
		conn.disconnect
		puts "El envio de mensajes ha finalizado!"
		exit 0
	end
end


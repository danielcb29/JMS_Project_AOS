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
destination = $*[0] || "/queue/cola_jms"

#Cliente STOMP
conn = Stomp::Client.open(user, password, host, port, false)

#Informe de que estas listo para recibir mensajes
puts "Estas listo para recibir mensajes!"

# Suscripcion
conn.subscribe(destination) do |msg|
  puts msg.body
  if msg.body == "SHUTDOWN"
    #Si el mensaje es shutdown se termina el programa y se cierra la suscripcion
    puts "El envio de mensajes ha finalizado!"
    exit 0
  end
end
conn.join

Prerequisitos
=======

- Instalar la gem stomp.  Ejecuta: gem install stomp

Overview of listener.rb and publisher.rb 
==========================================

La idea es un envío de mensajes entre dos clientes Ruby, para ello ejecutas primero

	ruby listener.rb 

Donde tendras el cliente que recibe los mensajes, luego ejecutas

	ruby publisher.rb

Donde podras escribir mensajes por consola que seran recibidos por listener

Para finalizar el envío de mensajes deben digitar 

	SHUTDWON

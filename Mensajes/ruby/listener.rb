#!/usr/bin/env ruby
# ------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ------------------------------------------------------------------------
 
require 'rubygems'
require 'stomp'

user = ENV["APOLLO_USER"] || "admin"
password = ENV["APOLLO_PASSWORD"] || "password"
host = ENV["APOLLO_HOST"] || "localhost"
port = ENV["APOLLO_PORT"] || 61613
destination = $*[0] || "/topic/event"

conn = Stomp::Connection.open user, password, host, port, false 
count = 0

conn.subscribe destination, { :ack =>"auto" }
start = Time.now
puts "Estas listo para recibir mensajes!"
while true 
	msg = conn.receive
	if msg.command == "MESSAGE" 
		if msg.body == "SHUTDOWN"
			diff = Time.now - start
			puts "El envio de mensajes ha finalizado!"
			puts "Recibidos #{count} mensajes en #{diff} segundos"
			exit 0
	
		else
		  	start = Time.now if count==0 
			count += 1;
			puts "Has recibido: " + msg.body
		end
	else
 		puts "#{msg.command}: #{msg.body}"
	end
end

conn.disconnect
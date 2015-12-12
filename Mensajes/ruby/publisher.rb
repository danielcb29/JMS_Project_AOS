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

messages = 10000
size = 256

user = ENV["APOLLO_USER"] || "admin"
password = ENV["APOLLO_PASSWORD"] || "password"
host = ENV["APOLLO_HOST"] || "localhost"
port = ENV["APOLLO_PORT"] || 61613
destination = $*[0] || "/topic/event"

conn = Stomp::Connection.open user, password, host, port, false 
puts "El envio de mensajes a iniciado!, digita el mensaje y pulsa enter"
while true
	msn = gets.chomp
	puts "Tu has enviado: " + msn
	conn.publish destination, msn, {'persistent'=>'false'}
	if msn == "SHUTDOWN"
		conn.disconnect
		puts "El envio de mensajes ha finalizado!"
		exit 0
	end
end


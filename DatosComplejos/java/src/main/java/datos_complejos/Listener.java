/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package datos_complejos;

import org.fusesource.stomp.jms.*;
import javax.jms.*;

class Listener {

    public static void main(String []args) throws JMSException {

        String user = env("APOLLO_USER", "admin");
        String password = env("APOLLO_PASSWORD", "password");
        String host = env("APOLLO_HOST", "localhost");
        int port = Integer.parseInt(env("APOLLO_PORT", "61613"));
        String destination = arg(args, 0, "/topic/event");

        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setBrokerURI("tcp://" + host + ":" + port);

        Connection connection = factory.createConnection(user, password);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new StompJmsDestination(destination);

        MessageConsumer consumer = session.createConsumer(dest);
        long start = System.currentTimeMillis();
        long count = 0;
        System.out.println("Estas listo para recibir mensajes!");
        while(true) {
            /*Message msg = consumer.receive();
            if( msg instanceof  TextMessage ) {
                String body = ((TextMessage) msg).getText();
                if( "SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    System.out.println(String.format("Recibidos %d mensajes en %.2f segundos", count, (1.0*diff/1000.0)));
                    break;
                } else {
                    count++;
                    if( count == 0 ) {
                        start = System.currentTimeMillis();
                    }
                    System.out.println("Has recibido: "+body);
                }

            } else {
                System.out.println("Unexpected message type: "+msg.getClass());
            }*/
            Message msg = consumer.receive();
            if( msg instanceof  ObjectMessage ) {
                Hora body = (Hora)((ObjectMessage) msg).getObject();
                System.out.println(body.getMinutos());
                System.out.println(body.getSegundos());
                break;
                /*if( "SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    System.out.println(String.format("Recibidos %d mensajes en %.2f segundos", count, (1.0*diff/1000.0)));
                    break;
                } else {
                    count++;
                    if( count == 0 ) {
                        start = System.currentTimeMillis();
                    }
                    System.out.println("Has recibido: "+body);
                }*/

            } else {
                System.out.println("Unexpected message type: "+msg.getClass());
            }
        }
        connection.close();
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }
}

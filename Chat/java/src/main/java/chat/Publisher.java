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
package chat;

import org.fusesource.stomp.jms.*;
import javax.jms.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


class Publisher {

    public static void main(String []args) throws JMSException {

        String user = env("APOLLO_USER", "admin");
        String password = env("APOLLO_PASSWORD", "password");
        String host = env("APOLLO_HOST", "localhost");
        int port = Integer.parseInt(env("APOLLO_PORT", "61613"));
        String destination = arg(args, 0, "/topic/event");

        int messages = 10000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body="";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
        try{
            body=in.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setBrokerURI("tcp://" + host + ":" + port);

        Connection connection = factory.createConnection(user, password);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new StompJmsDestination(destination);
        MessageProducer producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for( int i=1; i <= messages; i ++) {
            TextMessage msg = session.createTextMessage(body);
            msg.setIntProperty("id", i);
            producer.send(msg);
            if( (i % 1000) == 0) {
                System.out.println(String.format("Sent %d messages", i));
            }
        }

        producer.send(session.createTextMessage("SHUTDOWN"));
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
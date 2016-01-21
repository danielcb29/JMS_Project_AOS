/**
 * Envio de mensajes con Java 
 * Daniel Correa, Cristina Extremera
 */
package mensajes;

import org.fusesource.stomp.jms.*;
import javax.jms.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Clase encargada del envio de mensajes
 * @author Daniel Correa, Cristina Extremera
 */
class Publisher {

    public static void main(String []args) throws JMSException {

        //Configuracion inicial para la comunicacion 
        String user = env("APOLLO_USER", "admin");
        String password = env("APOLLO_PASSWORD", "password");
        String host = env("APOLLO_HOST", "localhost");
        int port = Integer.parseInt(env("APOLLO_PORT", "61613"));
        String destination = arg(args, 0, "/topic/event");

        //Creacion de fabrica de conexiones y seteo de variable URI del broker
        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setBrokerURI("tcp://" + host + ":" + port);

        //Creacion de la conexion con el usuario y contraseña correspondiente 
        Connection connection = factory.createConnection(user, password);
        //Inicio de la conexion
        connection.start();

        //Creacion de la sesion        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new StompJmsDestination(destination);

        //Se crea el productor de mensajes
        MessageProducer producer = session.createProducer(dest);
        //Se inicializa el modo de no persistencia
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
        //Se informa de que el envio de mensajes esta listo
        System.out.println("El envio de mensajes ha iniciado!, digita el mensaje y pulsa enter");

        //Ciclo de envio de mensajes
        while(true){

            //Variables para realizar la lectura desde el teclado
        	String body="";
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 


            try{
                //Lectura del mensaje por teclado
                body=in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            //Creacion del mensaje
            TextMessage msg = session.createTextMessage(body);

            //Envio del mensaje
            producer.send(msg);

            //En el caso de que el mensaje fuese SHUTDOWN se cierra la comunicacion
            if (body.equals("SHUTDOWN")){
            	connection.close();
            	break;
            }
        }
        
        
        

    }


    /**
     * Metodo auxiliar que permite saber si hay alguna variable en el ambito de trabajo con nombre key,
     * en caso de no exisitir dicha variable se retorna defaultValue
     * @param key: Nombre de la variable que esta en el ambiente
     * @param defaultValue: Valor por defecto
     */
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }


    /**
     * Busca el argumento almacenado en la posicion index y en caso de no exisitir se retorna defaultValue
     * @param args: argumentos de la aplicacion
     * @param index: posicion del argumento
     * @param defaultValue: valor por defecto
     */
    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}

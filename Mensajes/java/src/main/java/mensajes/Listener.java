/**
 * Envio de mensajes con Java
 * Daniel Correa, Cristina Extremera
 */
package mensajes;

import org.fusesource.stomp.jms.*;
import javax.jms.*;

/**
 * Clase que recibe los mensajes
 * @author Daniel Correa, Cristina Extremera
 */
class Listener {

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

        //Se crea el consumidor de mensajes
        MessageConsumer consumer = session.createConsumer(dest);

        //Obtenemos el tiempo actual para que cuando se termine la conexion sepamos cuanto tiempo ha durado esta
        long start = System.currentTimeMillis();
        //Contador que iremos incrementando por cada mensaje recibido
        long count = 0;

        //Se informa la espera de mensajes
        System.out.println("Estas listo para recibir mensajes!");

        //Ciclo de espera de mensajes
        while(true) {

            //Mensaje recibido
            Message msg = consumer.receive();

            //Verificacion que es un mensaje de texto
            if( msg instanceof  TextMessage ) {
                //Cuerpo del mensaje
                String body = ((TextMessage) msg).getText();

                //Si es shutdown acaba la comunicacion
                if( "SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    System.out.println(String.format("Recibidos %d mensajes en %.2f segundos", count, (1.0*diff/1000.0)));
                    break;
                } else {
                    //Caso contrario se imprime el mensaje recibido
                    count++;
                    if( count == 0 ) { //Si es el primer mensaje que recibo, obtengo el tiempo actual
                        start = System.currentTimeMillis();
                    }
                    System.out.println("Has recibido: "+body);
                }

            } else {
                //Si no es una instancia de texto, es un mensaje inesperado
                System.out.println("Unexpected message type: "+msg.getClass());
            }
        }

        //Cierre de la conexion
        connection.close();
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

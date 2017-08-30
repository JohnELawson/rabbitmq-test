package com.john;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * Created by john on 30/08/2017.
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] arg) { //throws java.io.IOException {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("error - " + ex + "\n");
            for(int i=0; i<ex.getStackTrace().length; i++){
                System.out.println(ex.getStackTrace()[i]);
            }
        }

    }
}
package com.john;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by john on 30/08/2017.
 */
public class Recv {

    private final static String QUEUE_NAME = "hello2";

    public static void main(String[] arg) { //throws java.io.IOException, java.lang.InterruptedException {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
//            factory.setPort(5672);
//            factory.setUsername("test");
//            factory.setPassword("test");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // accept only one unack-ed message at a time (see below)
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            boolean durable = true;
            channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");

                    try {
                        doWork(message);
                    } catch (Exception ex) {
                        System.out.println(" [x] Error - " + ex);
                    } finally {
                        System.out.println(" [x] Done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);

        } catch (Exception ex) {
            System.out.println("error - " + ex + "\n");
            for(int i=0; i<ex.getStackTrace().length; i++){
                System.out.println("\t" + ex.getStackTrace()[i]);
            }
        }
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}

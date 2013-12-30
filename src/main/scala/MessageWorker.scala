package ardlema.rabbitmqmessageworker

import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}

object MessageWorker {
  def main(args: Array[String]) {
    val queueName = "taskQueue"
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.queueDeclare(queueName, true, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")

    channel.basicQos(1)

    val consumer = new QueueingConsumer(channel)
    channel.basicConsume(queueName, false, consumer)

    while (true) {
      val delivery = consumer.nextDelivery()
      val message = new String(delivery.getBody())

      println(" [x] Received '" + message + "'")
      doWork(message)
      println(" [x] Done" )

      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false)
    }

    def doWork(task: String) {
      for (char <- task
          if char.equals(".")) {
            Thread.sleep(1000)
      }


    }
  }
}

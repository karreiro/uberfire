/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.java.nio.fs.jgit.ws.cluster;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.java.nio.file.Path;
import org.uberfire.java.nio.file.WatchEvent;

public class JGitEventsBroadcast {

    public static final String DEFAULT_APPFORMER_TOPIC = "default-appformer-topic";
    private static final Logger LOGGER = LoggerFactory.getLogger(JGitEventsBroadcast.class);
    private Session session;
    private String nodeId = UUID.randomUUID().toString();
    private ClusterParameters clusterParameters;
    private Consumer<WatchEventsWrapper> eventsPublisher;
    private Connection connection;
    private ConcurrentHashMap<String, Optional<TopicConsumerProducerWrapper>> topics = new ConcurrentHashMap<>();
    private TopicConsumerProducerWrapper defaultTopic;

    public JGitEventsBroadcast(ClusterParameters clusterParameters,
                               Consumer<WatchEventsWrapper> eventsPublisher) {
        this.clusterParameters = clusterParameters;
        this.eventsPublisher = eventsPublisher;
        setupJMSSession();
    }

    private void setupJMSSession() {
        try {
            InitialContext context = createInitialContext();
            ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactoryJMS");

            connection = factory.createConnection(clusterParameters.getJmsUserName(),
                                                  clusterParameters.getJmsPassword());
            connection.setExceptionListener(new MyExceptionListener());
            connection.start();

            session = connection.createSession(false,
                                               Session.AUTO_ACKNOWLEDGE);
            createTopicConsumerProducerWrapper(DEFAULT_APPFORMER_TOPIC);

            Optional<TopicConsumerProducerWrapper> topicConsumerProducerWrapper = createTopicConsumerProducerWrapper(DEFAULT_APPFORMER_TOPIC);

            if (topicConsumerProducerWrapper.isPresent()) {
                defaultTopic = topicConsumerProducerWrapper.get();
            } else {
                throw new RuntimeException("Exception creating Topic Consumer Producer Wrapper");
            }
        } catch (Exception e) {
            LOGGER.error("Error connecting on JMS " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Optional<TopicConsumerProducerWrapper> createTopicConsumerProducerWrapper(String topicName) {
        try {
            Destination topic = session.createTopic(topicName);
            MessageProducer messageProducer = session.createProducer(topic);
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(this::messageListener);
            return Optional.of(new TopicConsumerProducerWrapper(messageProducer,
                                                                consumer));
        } catch (JMSException e) {
            LOGGER.error("Exception creating Topic Consumer Producer Wrapper:  " + e.getMessage());
            return Optional.empty();
        }
    }

    private void messageListener(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                Serializable object = ((ObjectMessage) message).getObject();
                if (object instanceof WatchEventsWrapper) {
                    WatchEventsWrapper messageWrapper = (WatchEventsWrapper) object;
                    if (!messageWrapper.getNodeId().equals(nodeId)) {
                        eventsPublisher.accept(messageWrapper);
                    }
                }
            } catch (JMSException e) {
                LOGGER.error("Exception receiving JMS message: " + e.getMessage());
            }
        }
    }

    private InitialContext createInitialContext() throws Exception {
        Hashtable<String, String> jndiProps = new Hashtable<>();
        jndiProps.put("connectionFactory.ConnectionFactoryJMS",
                      clusterParameters.getJmsURL());
        jndiProps.put("java.naming.factory.initial",
                      clusterParameters.getJmsConnectionFactory());
        return new InitialContext(jndiProps);
    }

    public void broadcast(String fsName,
                          Path watchable,
                          List<WatchEvent<?>> events) {

        try {
            MessageProducer messageProducer = register(fsName)
                    .map(TopicConsumerProducerWrapper::getMessageProducer)
                    .orElse(defaultTopic.getMessageProducer());

            WatchEventsWrapper serializable = new WatchEventsWrapper(nodeId,
                                                                     fsName,
                                                                     watchable,
                                                                     events);

            ObjectMessage objectMessage = session.createObjectMessage(serializable);
            messageProducer.send(objectMessage);
        } catch (JMSException e) {
            LOGGER.error("Exception on JMS connection: " + e.getMessage());
        }
    }

    public Optional<TopicConsumerProducerWrapper> register(String fsName) {

        if (fsName.contains("/")) {
            String topicName = fsName.substring(0,
                                                fsName.indexOf("/"));
            Optional<TopicConsumerProducerWrapper> topicConsumerProducerWrapper = topics.computeIfAbsent(topicName,
                                                                                                         this::createTopicConsumerProducerWrapper);
            return topicConsumerProducerWrapper;
        }
        return Optional.empty();
    }

    public void close() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            LOGGER.error("Exception closing JMS connection and session: " + e.getMessage());
        }
    }

    private static class MyExceptionListener implements ExceptionListener {

        @Override
        public void onException(JMSException e) {
            System.out.println();
        }
    }

    private class TopicConsumerProducerWrapper {

        private final MessageProducer messageProducer;
        private final MessageConsumer consumer;

        public TopicConsumerProducerWrapper(MessageProducer messageProducer,
                                            MessageConsumer consumer) {

            this.messageProducer = messageProducer;
            this.consumer = consumer;
        }

        public MessageProducer getMessageProducer() {
            return messageProducer;
        }

        public MessageConsumer getConsumer() {
            return consumer;
        }
    }
}

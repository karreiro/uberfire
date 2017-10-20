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

public class ClusterParameters {

    public static final String APPFORMER_CLUSTER = "appformer-cluster";
    public static final String APPFORMER_DEFAULT_CLUSTER_CONFIGS = "appformer-default-cluster-configs";
    public static final String APPFORMER_JMS_URL = "appformer-jms-url";
    public static final String APPFORMER_JMS_CONNECTION_FACTORY = "appformer-jms-connection-factory";
    public static final String APPFORMER_JMS_USERNAME = "appformer-jms-username";
    public static final String APPFORMER_JMS_PASSWORD = "appformer-jms-password";
    private Boolean appFormerClustered;
    private String jmsURL;
    private String jmsConnectionFactory;
    private String jmsUserName;
    private String jmsPassword;

    public ClusterParameters() {

        this.appFormerClustered = Boolean.valueOf(System.getProperty(APPFORMER_CLUSTER,
                                                                     "false"));
        if (appFormerClustered) {

            Boolean defaultConfigs = Boolean.valueOf(System.getProperty(APPFORMER_DEFAULT_CLUSTER_CONFIGS,
                                                                        "false"));
            if (defaultConfigs) {
                setupDefaultConfigs();
            } else {
                loadConfigs();
            }
        }
    }

    private void setupDefaultConfigs() {
        this.jmsURL = "tcp://localhost:61616";
        this.jmsConnectionFactory = "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory";
        this.jmsUserName = "admin";
        this.jmsPassword = "admin";
    }

    private void loadConfigs() {
        this.jmsURL = System.getProperty(APPFORMER_JMS_URL);
        this.jmsConnectionFactory = System.getProperty(APPFORMER_JMS_CONNECTION_FACTORY);
        this.jmsUserName = System.getProperty(APPFORMER_JMS_USERNAME);
        this.jmsPassword = System.getProperty(APPFORMER_JMS_PASSWORD);
    }

    public Boolean isAppFormerClustered() {
        return appFormerClustered;
    }

    public String getJmsURL() {
        return jmsURL;
    }

    public String getJmsConnectionFactory() {
        return jmsConnectionFactory;
    }

    public String getJmsUserName() {
        return jmsUserName;
    }

    public String getJmsPassword() {
        return jmsPassword;
    }
}

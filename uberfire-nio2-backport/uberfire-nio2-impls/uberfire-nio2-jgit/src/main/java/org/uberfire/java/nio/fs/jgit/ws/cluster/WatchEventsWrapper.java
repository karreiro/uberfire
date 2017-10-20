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
import java.net.URI;
import java.util.List;

import org.uberfire.java.nio.file.Path;
import org.uberfire.java.nio.file.Paths;
import org.uberfire.java.nio.file.WatchEvent;

public class WatchEventsWrapper implements Serializable {

    private final String nodeId;
    private final List<WatchEvent<?>> events;
    private final URI watchable;
    private final String fsName;

    public WatchEventsWrapper(String nodeId,
                              String fsName,
                              Path watchable,
                              List<WatchEvent<?>> events) {

        this.nodeId = nodeId;
        this.fsName = fsName;
        this.events = events;
        this.watchable = watchable != null ? watchable.toUri() : null;
    }

    public String getFsName() {
        return fsName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public List<WatchEvent<?>> getEvents() {
        return events;
    }

    public Path getWatchable() {
        if (watchable == null) {
            return null;
        }
        try {
            return Paths.get(watchable);
        } catch (Exception e) {
            return null;
        }
    }
}
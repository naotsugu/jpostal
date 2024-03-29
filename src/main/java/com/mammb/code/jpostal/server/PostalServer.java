/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mammb.code.jpostal.server;

import com.mammb.code.jpostal.Postal;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * PostalServer.
 *
 * @author naotsugu
 */
public class PostalServer {

    private static final System.Logger log = System.getLogger(PostalServer.class.getName());

    private final HttpServer server;


    private PostalServer(Postal postal, int port) {
        try {
            String contextRoot = "/postal";
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            this.server.createContext(contextRoot, new PostalHandler(contextRoot, postal));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Create the server instance.
     * @param postal the Postal
     * @return PostalServer
     */
    public static PostalServer of(Postal postal) {
        return new PostalServer(postal, 8080);
    }


    /**
     * Create the server instance.
     * @param postal the Postal
     * @param port the port to listen on
     * @return PostalServer
     */
    public static PostalServer of(Postal postal, int port) {
        return new PostalServer(postal, port);
    }


    /**
     * Starts this server.
     */
    public void start() {
        server.start();
    }

    /**
     * Stops this server.
     */
    public void stop() {
        if (Objects.nonNull(server)) {
            server.stop(0);
        }
    }

}

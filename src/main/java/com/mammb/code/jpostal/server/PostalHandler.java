/*
 * Copyright 2002-2016 the original author or authors.
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

import com.mammb.code.jpostal.Address;
import com.mammb.code.jpostal.Postal;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PostalHandler implements HttpHandler {

    private static final Logger log = Logger.getLogger(PostalHandler.class.getName());

    private final String contextRoot;
    private final Postal postal;
    private final Pattern pattern;


    PostalHandler(String contextRoot, Postal postal) {
        this.contextRoot = contextRoot;
        this.postal = postal;
        this.pattern = this.postal.leftMatchSupport()
                ? Pattern.compile(contextRoot + "/([0-9\\-]++)")
                : Pattern.compile(contextRoot + "/(\\d{7}|\\d{3}-\\d{4})");
    }


    @Override
    public void handle(HttpExchange exchange) {
        try {

            String path = exchange.getRequestURI().normalize().getPath();
            if (path.equals(contextRoot + "/console.html")) {
                writePage("/console.html", exchange);
                return;
            }

            StringBuilder sb = new StringBuilder();
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                String code = matcher.group(1).replace("-", "");
                Collection<Address> addresses = postal.get(code);
                if (Objects.nonNull(addresses) && addresses.size() > 0) {
                    sb.append("[");
                    for (Address address : addresses) {
                        sb.append(address.toJsonString());
                        sb.append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    sb.append("]");
                }
            }
            writeResponse(200, sb.toString(), exchange);

        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            writeResponse(500, "Server Error.", exchange);
        }
    }


    private void writeResponse(int rCode, String res, HttpExchange exchange) {
        byte[] bytes = res.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset="
                    + StandardCharsets.UTF_8.toString());
            exchange.sendResponseHeaders(rCode, bytes.length);
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void writePage(String path, HttpExchange exchange) {
        try (InputStream is = getClass().getResourceAsStream(path);
             OutputStream os = exchange.getResponseBody()) {
            byte[] bytes = is.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset="
                    + StandardCharsets.UTF_8.toString());
            exchange.sendResponseHeaders(200, bytes.length);
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

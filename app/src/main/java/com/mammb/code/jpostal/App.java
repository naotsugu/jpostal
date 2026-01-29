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
package com.mammb.code.jpostal;

import com.mammb.code.jpostal.server.PostalServer;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.Logger.Level.*;

/**
 * Application entry point.
 *
 * @author naotsugu
 */
public class App {

    /** The logger. */
    private static final System.Logger log = System.getLogger(App.class.getName());

    /**
     * Start tha App.
     * @param args the args
     */
    public static void main(String[] args) {

        if (args != null && args.length > 1 && args[0].equals("-o")) {
            writeCsv(args[1]);
            System.exit(0);
        }

        Postal postal = Postal.of()
            .useLegacySource(false)
            .fineAddressSupport(true)
            .leftMatchSupport(true)
            .leftMatchLimitCount(20)
            .officeSourceSupport(false)
            .autoUpdateSupport(true);

        log.log(INFO, "initializing...");

        postal.initialize();

        log.log(INFO, "initialized");

        log.log(INFO, "start server..");
        PostalServer server = PostalServer.of(postal);
        server.start();

        log.log(INFO, "http://localhost:8080/postal/console.html");

    }


    private static void writeCsv(String pathString) {

        if (pathString == null || !pathString.endsWith(".csv")) {
            log.log(ERROR, "illegal path [{0}]", pathString);
            System.exit(1);
        }

        Path path = Path.of(pathString);
        if (Files.isDirectory(path) || !path.toString().endsWith(".csv")) {
            log.log(ERROR, "Illegal path.[{0}]", path);
            System.exit(1);
        }
        if (Files.exists(path)) {
            log.log(ERROR, "File already exists.[{0}]", path);
            System.exit(1);
        }

        Postal postal = Postal.of();

        log.log(INFO, "initializing...");

        postal.initialize();

        log.log(INFO, "initialized");

        log.log(INFO, "write csv [{0}]", path.toString());
        postal.writerCsv(path);

    }

}

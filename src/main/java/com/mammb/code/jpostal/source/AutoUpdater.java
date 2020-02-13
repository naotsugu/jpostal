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
package com.mammb.code.jpostal.source;

import com.mammb.code.jpostal.Postal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Postal auto updater.
 *
 * @author naotsugu
 */
public class AutoUpdater {

    private static final Logger log = Logger.getLogger(AutoUpdater.class.getName());

    private final ScheduledExecutorService executor;
    private final Postal postal;


    private AutoUpdater(Postal postal) {
        this.postal = postal;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }


    /**
     * Create the {@code AutoUpdater} instance.
     * @param postal tha Postal
     * @return the {@code AutoUpdater} instance
     */
    public static AutoUpdater of(Postal postal) {
        return new AutoUpdater(postal);
    }


    /**
     * Submits a next update task.
     */
    public void schedule() {
        LocalDate date = LocalDate.now().withDayOfMonth(1).plusMonths(1L);
        LocalDateTime dt = date.atStartOfDay().plusMinutes(new Random().nextInt(60));
        log.info("next update scheduled - " + dt.toString());
        executor.schedule(
                postal::initializeAll,
                ChronoUnit.SECONDS.between(LocalDateTime.now(), dt),
                TimeUnit.SECONDS);
    }

}

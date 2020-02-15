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
package com.mammb.code.jpostal;

import com.mammb.code.jpostal.source.AutoUpdater;
import com.mammb.code.jpostal.source.PostalSource;
import com.mammb.code.jpostal.source.PostalSourceFetcher;
import com.mammb.code.jpostal.source.PostalSourceReader;
import com.mammb.code.jpostal.source.Settings;
import com.mammb.code.jpostal.source.SourceLine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Postal.
 *
 * @author naotsugu
 */
public class Postal {

    private static final Logger log = Logger.getLogger(Postal.class.getName());

    private final Map<PostalCode, Collection<Address>> map;
    private final NavigableSet<String> index;
    private final ReadWriteLock lock;
    private final Settings settings;
    private final AutoUpdater autoUpdater;


    private Postal(Settings settings) {
        this.map = new HashMap<>();
        this.index = new TreeSet<>();
        this.lock = new ReentrantReadWriteLock();
        this.settings = settings;
        this.autoUpdater = AutoUpdater.of(this);
    }


    /**
     * Create the {@code Postal} instance.
     * @return {@code Postal}
     */
    public static Postal of() {
        return new Postal(Settings.of());
    }


    /**
     * Set the fineAddressSupport.
     * @param support the fineAddressSupport
     * @return {@code Postal}
     */
    public Postal fineAddressSupport(boolean support) {
        this.settings.fineAddressSupport(support);
        return this;
    }


    /**
     * Set the leftMatchSupport.
     * @param support the leftMatchSupport
     * @return {@code Postal}
     */
    public Postal leftMatchSupport(boolean support) {
        this.settings.leftMatchSupport(support);
        return this;
    }


    /**
     * Set the officeSourceSupport.
     * @param support the officeSourceSupport
     * @return {@code Postal}
     */
    public Postal officeSourceSupport(boolean support) {
        this.settings.officeSourceSupport(support);
        return this;
    }


    /**
     * Set the autoUpdateSupport.
     * @param support the autoUpdateSupport
     * @return {@code Postal}
     */
    public Postal autoUpdateSupport(boolean support) {
        this.settings.autoUpdateSupport(support);
        return this;
    }


    /**
     * Set the leftMatchLimitCount.
     * @param count the leftMatchLimitCount
     * @return {@code Postal}
     */
    public Postal leftMatchLimitCount(int count) {
        this.settings.leftMatchLimitCount(count);
        return this;
    }


    /**
     * Initialize postal.
     */
    public void initialize() {
        final Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            build(settings.standardSource(), true);
            build(settings.officeSource(), true);
            if (settings.autoUpdateSupport()) {
                autoUpdater.schedule();
            }
        } finally {
            writeLock.unlock();
        }
    }


    /**
     * Full initialize.
     */
    public void initializeAll() {
        final Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            index.clear();
            map.clear();
            build(settings.standardSource(), false);
            build(settings.officeSource(), false);
            if (settings.autoUpdateSupport()) {
                autoUpdater.schedule();
            }
        } finally {
            writeLock.unlock();
        }
    }


    /**
     * Get the address list by the given postal code.
     * @param code the postal code
     * @return the address list
     */
    public Collection<Address> get(PostalCode code) {
        final Lock readLock = lock.readLock();
        try {
            readLock.lock();
            return map.get(code);
        } finally {
            readLock.unlock();
        }
    }


    /**
     * Get the address list by the given query.
     * @param query the given query
     * @return the address list
     */
    public Collection<Address> get(String query) {
        final Lock readLock = lock.readLock();
        try {
            readLock.lock();
            if (!settings.leftMatchSupport() || query.length() == 7) {
                return get(PostalCode.of(query));
            } else {
                return leftMatch(query, settings.leftMatchLimitCount());
            }
        } finally {
            readLock.unlock();
        }

    }


    /**
     * Get the setting of leftMatchSupport.
     * @return the setting of leftMatchSupport
     */
    public boolean leftMatchSupport() {
        return settings.leftMatchSupport();
    }


    private List<Address> leftMatch(String query, int limit) {
        List<Address> candidates = new ArrayList<>();
        for (String code : index.tailSet(query)) {
            if (code.startsWith(query)) {
                candidates.addAll(map.get(PostalCode.of(code)));
            } else {
                break;
            }

            if (candidates.size() >= limit) {
                return candidates.subList(0, limit);
            }
        }
        return candidates;
    }


    private void build(PostalSource source, boolean recycleFetchedFile) {

        if (Objects.isNull(source)) {
            return;
        }

        Path path = recycleFetchedFile
                ? PostalSourceFetcher.recycleOf(source).fetch()
                : PostalSourceFetcher.of(source).fetch();

        try (PostalSourceReader reader = source.reader(path)) {
            for (;;) {
                SourceLine line = reader.readNext();
                if (Objects.isNull(line)) {
                    log.log(Level.INFO, "imported " + source + ". [" + map.size() + "]");
                    break;
                }
                line.getAddress().forEach(this::put);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void put(final Address address) {

        PostalCode code = address.getCode();

        if (map.containsKey(code)) {
            map.get(code).add(address);
        } else {
            Set<Address> list = new LinkedHashSet<>();
            list.add(address);
            map.put(code, list);
        }

        if (settings.leftMatchSupport()) {
            index.add(code.getCode());
        }
    }

}

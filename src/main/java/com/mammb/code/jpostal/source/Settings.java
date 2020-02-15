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

/**
 * Settings.
 *
 * @author naotsugu
 */
public class Settings {

    private final PostalSource standardSource;
    private final PostalSource officeSource;

    private boolean leftMatchSupport;
    private boolean officeSourceSupport;
    private boolean autoUpdateSupport;
    private boolean fineAddressSupport;
    private int leftMatchLimitCount;


    private Settings(
            PostalSource standardSource,
            PostalSource officeSource,
            boolean fineAddressSupport,
            boolean leftMatchSupport,
            boolean officeSourceSupport,
            boolean autoUpdateSupport,
            int leftMatchLimitCount) {
        this.standardSource = standardSource;
        this.officeSource = officeSource;
        this.fineAddressSupport = fineAddressSupport;
        this.leftMatchSupport = leftMatchSupport;
        this.officeSourceSupport = officeSourceSupport;
        this.autoUpdateSupport = autoUpdateSupport;
        this.leftMatchLimitCount = leftMatchLimitCount;
    }


    /**
     * Create the {@code Settings} instance.
     * @return {@code Settings} instance
     */
    public static Settings of() {
        return new Settings(
                PostalSource.standardSource(),
                PostalSource.officeSource(),
                true,
                true,
                false,
                false,
                20);
    }


    /**
     * Set the leftMatchSupport.
     * @param support leftMatchSupport
     */
    public void leftMatchSupport(boolean support) {
        leftMatchSupport = support;
    }

    /**
     * Set the fineAddressSupport.
     * @param support fineAddressSupport
     */
    public void fineAddressSupport(boolean support) {
        fineAddressSupport = support;
    }

    /**
     * Set the autoUpdateSupport.
     * @param support autoUpdateSupport
     */
    public void autoUpdateSupport(boolean support) {
        autoUpdateSupport = support;
    }

    /**
     * Set the officeSourceSupport.
     * @param support officeSourceSupport
     */
    public void officeSourceSupport(boolean support) {
        officeSourceSupport = support;
    }

    /**
     * Set the leftMatchLimitCount.
     * @param count leftMatchLimitCount
     */
    public void leftMatchLimitCount(int count) {
        leftMatchLimitCount = count;
    }

    /**
     * Get the standardSource.
     * @return standardSource
     */
    public PostalSource standardSource() {
        if (fineAddressSupport) {
            standardSource.with(TownEditor.standardEditors());
        } else {
            standardSource.with(TownEditor.simpleEditors());
        }
        return standardSource;
    }

    /**
     * Get the officeSource.
     * @return officeSource
     */
    public PostalSource officeSource() {
        return officeSourceSupport ? officeSource : null;
    }

    /**
     * Get the leftMatchSupport.
     * @return leftMatchSupport
     */
    public boolean leftMatchSupport() {
        return leftMatchSupport;
    }

    /**
     * Get the autoUpdateSupport.
     * @return autoUpdateSupport
     */
    public boolean autoUpdateSupport() {
        return autoUpdateSupport;
    }

    /**
     * Get the leftMatchLimitCount.
     * @return leftMatchLimitCount
     */
    public int leftMatchLimitCount() {
        return leftMatchLimitCount;
    }
}

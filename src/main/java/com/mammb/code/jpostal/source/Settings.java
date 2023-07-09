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
package com.mammb.code.jpostal.source;

/**
 * Settings.
 *
 * @author naotsugu
 */
public class Settings {

    private boolean leftMatchSupport = true;
    private boolean officeSourceSupport = false;
    private boolean autoUpdateSupport = false;
    private boolean fineAddressSupport = true;
    private int leftMatchLimitCount = 20;
    private boolean useLegacySource = false;


    /**
     * Create the {@code Settings} instance.
     * @return {@code Settings} instance
     */
    public static Settings of() {
        return new Settings();
    }

    /**
     * Set the useLegacySource.
     * @param legacy useLegacySource
     */
    public void useLegacySource(boolean legacy) {
        useLegacySource = legacy;
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
        PostalSource postalSource = useLegacySource
            ? PostalSource.standardSource()
            : PostalSource.standardUtfSource();
        postalSource.with(fineAddressSupport
            ? TownEditor.standardEditors()
            : TownEditor.simpleEditors());
        return postalSource;
    }

    /**
     * Get the officeSource.
     * @return officeSource
     */
    public PostalSource officeSource() {
        return officeSourceSupport ? PostalSource.officeSource() : null;
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

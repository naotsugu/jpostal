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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * PostalSourceFetcher.
 *
 * @author naotsugu
 */
public class PostalSourceFetcher {

    private final Path baseDir;
    private final boolean recycle;
    private final String sourceUrl;


    private PostalSourceFetcher(Path baseDir, String sourceUrl, boolean recycle) {
        this.baseDir = baseDir;
        this.recycle = recycle;
        this.sourceUrl = sourceUrl;
    }


    /**
     * Create the {@code PostalSourceFetcher} instance.
     * @param postalSource the source of postal
     * @return the {@code PostalSourceFetcher} instance
     */
    public static PostalSourceFetcher of(PostalSource postalSource) {
        return new PostalSourceFetcher(Paths.get("./"), postalSource.url(), false);
    }


    /**
     * Create the {@code PostalSourceFetcher} instance.
     * @param postalSource the source of postal
     * @return the {@code PostalSourceFetcher} instance
     */
    public static PostalSourceFetcher recycleOf(PostalSource postalSource) {
        return new PostalSourceFetcher(Paths.get("./"), postalSource.url(), true);
    }


    /**
     * Fetch the postal dictionary csv.
     * @return the path of fetched file
     */
    public Path fetch() {
        Path zipPath = baseDir.resolve(fetchedPath(sourceUrl));
        if (!(recycle && Files.exists(zipPath))) {
            zipPath = fetch(sourceUrl, zipPath);
        }
        return unzip(zipPath);
    }


    private static Path fetchedPath(String url) {
        final String fileName = url.substring(url.lastIndexOf("/") + 1);
        return Paths.get(fileName);
    }


    private static Path fetch(String url, Path toPath) {
        try (ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
             FileOutputStream os = new FileOutputStream(toPath.toFile())) {
            os.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toPath;
    }


    private static Path unzip(final Path zipPath) {
        try {
            Path tempDir = Files.createTempDirectory(PostalSource.class.getSimpleName() + ".");
            unzip(zipPath, tempDir, StandardCharsets.UTF_8);
            return Files.list(tempDir)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".csv"))
                    .findFirst()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void unzip(
            final Path zipPath,
            final Path unzipLocation,
            final Charset charset) throws IOException {

        if (!Files.exists(unzipLocation)) {
            Files.createDirectories(unzipLocation);
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(
                Files.newInputStream(zipPath),
                charset)) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                Path filePath = unzipLocation.resolve(entry.getName());
                if (!entry.isDirectory()) {
                    unzipFiles(zipInputStream, filePath);
                } else {
                    Files.createDirectories(filePath);
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        }
    }


    private static void unzipFiles(
            final ZipInputStream zipInputStream,
            final Path unzipFilePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(unzipFilePath.toAbsolutePath().toString()))) {
            byte[] bytesIn = new byte[1024 * 5];
            int read;
            while ((read = zipInputStream.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

}

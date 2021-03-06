/*-
 * #%L
 * PhantomJS Maven Core
 * %%
 * Copyright (C) 2013 - 2017 Kyle Lieber
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package com.github.klieber.phantomjs.download;

import com.github.klieber.phantomjs.archive.Archive;
import com.github.klieber.phantomjs.cache.ArchiveCache;
import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class WebDownloader implements Downloader {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebDownloader.class);

  private static final String DOWNLOADING = "Downloading archive from {}";
  private static final String UNABLE_TO_DOWNLOAD = "Unable to download archive from ";

  private final ArchiveCache archiveCache;

  public WebDownloader(ArchiveCache archiveCache) {
    this.archiveCache = archiveCache;
  }

  @Override
  public File download(Archive archive) throws DownloadException {
    File target = archiveCache.getFile(archive);
    if (!target.exists()) {
      String url = archive.getUrl();
      try {
        URL downloadLocation = new URL(url);

        LOGGER.info(DOWNLOADING, url);
        copyURLToFile(downloadLocation, target);

        if (target.length() <= 0) {
          throw new DownloadException(UNABLE_TO_DOWNLOAD+url);
        }
      } catch (IOException e) {
        throw new DownloadException(UNABLE_TO_DOWNLOAD+url, e);
      }
    }
    return target;
  }

  @VisibleForTesting
  void copyURLToFile(URL url, File file) throws IOException {
    FileUtils.copyURLToFile(url, file);
  }
}

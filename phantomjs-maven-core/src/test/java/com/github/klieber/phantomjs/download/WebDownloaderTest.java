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

import com.github.klieber.phantomjs.archive.PhantomJSArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebDownloaderTest {

  private static final String FILE_PATH = "file.zip";
  private static final String DOWNLOAD_URL = "http://example.org/" + FILE_PATH;

  @Mock
  private PhantomJSArchive phantomJSArchive;

  @Mock
  private File file;

  private WebDownloader downloader;

  @Before
  public void before() {
    downloader = spy(new WebDownloader(file));
  }

  @Test
  public void shouldDownload() throws Exception {
    when(phantomJSArchive.getUrl()).thenReturn(DOWNLOAD_URL);
    when(file.length()).thenReturn(100L);

    doNothing().when(downloader).copyURLToFile(any(URL.class), eq(file));

    downloader.download(phantomJSArchive);

    verify(downloader).copyURLToFile(any(URL.class), eq(file));
  }

  @Test
  public void shouldFailDueToEmptyFile() throws Exception {
    when(phantomJSArchive.getUrl()).thenReturn(DOWNLOAD_URL);
    when(file.length()).thenReturn(0L);
    doNothing().when(downloader).copyURLToFile(any(URL.class), eq(file));
    assertThatThrownBy(() -> downloader.download(phantomJSArchive)).isInstanceOf(DownloadException.class);
  }

  @Test
  public void shouldFailDueToIOException() throws Exception {
    when(phantomJSArchive.getUrl()).thenReturn(DOWNLOAD_URL);
    doThrow(new IOException()).when(downloader).copyURLToFile(any(URL.class), eq(file));
    assertThatThrownBy(() -> downloader.download(phantomJSArchive)).isInstanceOf(DownloadException.class);
  }

  @Test
  public void shouldFailDueToMalformedUrl() throws Exception {
    when(phantomJSArchive.getUrl()).thenReturn("invalid-base-url");
    assertThatThrownBy(() -> downloader.download(phantomJSArchive)).isInstanceOf(DownloadException.class);
  }
}

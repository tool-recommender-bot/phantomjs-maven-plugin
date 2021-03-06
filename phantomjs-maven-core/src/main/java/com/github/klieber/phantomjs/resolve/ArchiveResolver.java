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
package com.github.klieber.phantomjs.resolve;

import com.github.klieber.phantomjs.archive.Archive;
import com.github.klieber.phantomjs.install.InstallationException;
import com.github.klieber.phantomjs.install.Installer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchiveResolver implements Resolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveResolver.class);

  private final Installer installer;
  private final Archive archive;

  public ArchiveResolver(Installer installer, Archive archive) {
    this.installer = installer;
    this.archive = archive;
  }

  @Override
  public String resolve() {
    String location = null;
    try {
      location = installer.install(archive);
    } catch(InstallationException e) {
      LOGGER.error("Unable to resolve phantomjs binary",e);
    }
    return location;
  }
}

/*
 *  Copyright 2019 Insurance Australia Group Limited
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mark59.core;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mark59.core.utils.ScreenshotLoggingHelper;

/**
 * Wrapper class used to encapsulate an arbitrary Driver object, where the
 * Driver object knows how to perform an arbitrary set of functions necessary to
 * execute the test.
 * 
 * <p>A driver is also expect to knows how to take a screenshot accessible 
 * through {@link #driverTakeScreenshot()} 
 * 
 * <p>For a selenium implementation of a driver, it would be expected to be a type of WebDriver  
 * 
 * @param <T> Concrete Driver to be wrapped
 * @author Michael Cohen
 * Written: Australian Winter 2019
 */
public abstract class Mark59Driver<T> {

	private static final Logger LOG = LogManager.getLogger(Mark59Driver.class);
	
	private final String driverClass;
	private final T driver;
	
	/**
	 * map of captured screenshot as a byte array (keyed by name)
	 */
	protected Map<String, byte[]> bufferedArtifacts = new HashMap<>();
	

	@SuppressWarnings("unused")
	private Mark59Driver() {
		this.driver = null;
		this.driverClass = null;
	}

	/**
	 * Constructor for the DriverWrapper.
	 * 
	 * @param driver Concrete Driver to be wrapped
	 */
	public Mark59Driver(T driver) {
		this.driver = driver;
		this.driverClass = driver.getClass().getName();
	}

	/**
	 * Returns the class name of the encapsulated Driver.
	 * 
	 * @return String
	 */
	public String getDriverClass() {
		return driverClass;
	}

	/**
	 * Returns the concrete arbitrary driver encapsulated by this
	 * 
	 * @return driverPackage.
	 */
	public T getDriverPackage() {
		return driver;
	}

	/**
	 * Handles any needed cleanup once the driver is finished with, if any cleanup is required.
	 */
	public abstract void driverDispose();
	
	/**
	 * Used to return any logs captured by the Driver.
	 * 
	 * @return String
	 */
	public abstract String getDriverLogs();
	
	/**
	 * Clears logs previously captured by the Driver.
	 * <p>
	 * Useful if the driver is capturing more logs than are needed (for instance,
	 * only being interested in logs for the most recent event, in case of a
	 * failure).
	 * </p>
	 */
	public abstract void clearDriverLogs();
	
	
	/**
	 * log an exception state
	 * Specifically intended for logging or similar actions.
	 * 
	 * @param e supplied exception
	 */
	public void documentExceptionState(Exception e) {
		bufferScreenshot("EXCEPTION");
		writeBufferedArtifacts();
		
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString(); 
				
		ScreenshotLoggingHelper.writeScreenshotLog(
				new File(ScreenshotLoggingHelper.buildFullyQualifiedImageName("EXCEPTION", "txt")),
				StringUtils.isNotBlank(stackTrace) ? stackTrace.getBytes() : null);
	}
	


	/**
	 * @return captured screenshot as a byte array (abstract)
	 */
	protected abstract byte[] driverTakeScreenshot();


	/**
	 * calls: protected byte[] driverTakeScreenshot() for the concrete
	 * implementation of taking a screenshot for a given driver.
	 * 
	 * Increments screenshotCounter to give each screenshot a unique identifier
	 * 
	 * @return captured screenshot as a byte array
	 */
	protected byte[] takeScreenshot() {
		return driverTakeScreenshot();
	}


	/**
	 * Capture and immediately save screenshot. Use with caution. in a Performance
	 * and Volume context, misuse of this method may produce many more screenshots
	 * than intended. Instead, we recommend using {@link #bufferScreenshot(String)} and
	 * {@link #writeBufferedArtifacts()} 
	 *  
	 * @param imageName filename to use for the screenshot
	 * @return this
	 */
	public Mark59Driver<T> takeScreenshot(String imageName ) {
		if (LOG.isTraceEnabled()) LOG.trace(Thread.currentThread().getName() + " : taking screenshot with (partial) imageName = " + imageName);

		ScreenshotLoggingHelper.writeScreenshotLog(new File(ScreenshotLoggingHelper.buildFullyQualifiedImageName(imageName)), takeScreenshot());
		return this;
	}

	/**
	 * Stores screenshot in memory, ready to be written to file later.
	 * 
	 * If you want to immediately write a screenshot to file, use {@link #takeScreenshot()} instead.
	 * 
	 * @param imageName filename to use for the screenshot
	 * @return this (ScreenshotEnabledDriverWrapper)
	 */
	public Mark59Driver<T> bufferScreenshot(String imageName) {
		if (LOG.isDebugEnabled()) LOG.debug(MessageFormat.format("Buffering screenshot {0} for thread {1}", imageName,	Thread.currentThread().getName()));

		bufferedArtifacts.put(ScreenshotLoggingHelper.buildFullyQualifiedImageName(imageName), takeScreenshot());
		return this;
	}


	/**
	 * Writes all buffered screenshots to disk
	 * 
	 * @return this (ScreenshotEnabledDriverWrapper)
	 */
	public Mark59Driver<T> writeBufferedArtifacts() {
		LOG.info(MessageFormat.format("Writing {0} buffered data to disk for thread {1}", bufferedArtifacts.size(), Thread.currentThread().getName()));

		for (Entry<String, byte[]> bufferedArtifact : bufferedArtifacts.entrySet()) {
			ScreenshotLoggingHelper.writeScreenshotLog(new File(bufferedArtifact.getKey()) , bufferedArtifact.getValue());
		}

		bufferedArtifacts.clear();
		return this;
	}
	
	/**
	 * @return a map of the buffered screenshots (keyed by name) 
	 */
	public Map<String, byte[]> getBufferedScreenshots() {
		return bufferedArtifacts;
	}

	
	
}

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

package com.mark59.selenium.corejmeterimpl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mark59.core.utils.Mark59Constants;
import com.mark59.core.utils.PropertiesReader;
import com.mark59.core.utils.ScreenshotLoggingHelper;
import com.mark59.core.utils.StaticCounter;

/**
 * Static properties file reader, loading the properties file into memory just once per run to reduce disk I/O.
 * 
 * @author Michael Cohen
 * @author Philip Webb
 * Written: Australian Winter 2019  
 */
public class ScreenshotLoggingHelperSelenium extends ScreenshotLoggingHelper {
	private static final Logger LOG = LogManager.getLogger(ScreenshotLoggingHelper.class);	
	
	protected static final String SCREENSHOT_COUNTER = "SCREENSHOT_COUNTER";
	private static final String DEFAULT_LOGNAMES_FORMAT = Mark59Constants.THREAD_GROUP + "," + Mark59Constants.LABEL;
	
	private static ScreenshotLoggingHelperSelenium instance;
	
	private static Path screenshotDirectory = null;
	private static String screenshotLogNamesFormat = DEFAULT_LOGNAMES_FORMAT;
	
	

	/**
	 * private constructor to ensure deleteDirectory can only be executed in this class (to prevent multiple calls to deleteDirectory)
	 * <p>The screenshot directory is set.
	 * @see #initialiseDirectory()
	 */
//	private  ScreenshotLoggingHelperSelenium() throws IOException {
	private  ScreenshotLoggingHelperSelenium()  {
		super();
	}


	
	/**
	 * Returns a fully qualified name for the image, including assigning an arbitrary file extension.
	 * 
	 * <p>Returned names take the pattern {Directory}/{ThreadName}_{Image Number}_{imageName}.{extension}</p>
	 * 
	 * @param fileNameEnding filename to use for the screenshot
	 * @param extension file extension
	 * @return fully qualified image name
	 */
	public static String buildFullyQualifiedImageName(String fileNameEnding, String extension) {

		String fullyQualifiedImageName = MessageFormat.format("{0}{1}{2}_{3}_{4}.{5}", 
											getScreenshotDirectory(),
											File.separator, 
											Thread.currentThread().getName(), 
											String.format("%03d", StaticCounter.readCount(SCREENSHOT_COUNTER)),
											fileNameEnding, 
											extension);

		if (LOG.isTraceEnabled()) LOG.trace(Thread.currentThread().getName() + " : fullyQualifiedImageName = " + fullyQualifiedImageName);
		
		// increment counter ready for next image
		StaticCounter.incrementCount(SCREENSHOT_COUNTER);
		
		return fullyQualifiedImageName;
	}
	
		
	public static void writeExceptionLog(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString(); 
				
		writeScreenshotLog(	new File(buildFullyQualifiedImageName("EXCEPTION", "txt")),
				StringUtils.isNotBlank(stackTrace) ? stackTrace.getBytes() : null);		
		
		ScreenshotLoggingHelper.writeScreenshotLog(
				new File(ScreenshotLoggingHelper.buildFullyQualifiedImageName("EXCEPTION", "txt")),
				StringUtils.isNotBlank(stackTrace) ? stackTrace.getBytes() : null);
	}
	
	
	/**
	 * @return path of screenshotDirectory
	 */
	public static String getScreenshotDirectory() {
		if (screenshotDirectory == null)
			return null;
		return screenshotDirectory.toString();
	}

			
	/**
	 * @return an existing or otherwise new ScreenshotLoggingHelper
	 * @throws IOException when trying to read the properties file
	 */
	public static synchronized ScreenshotLoggingHelperSelenium initialiseDirectory() throws IOException {
		if (instance == null) {
			instance = new ScreenshotLoggingHelperSelenium();
		}
		return instance;
	}	
	
	
	/**
	 * Deprecated.  Please use {@link #initialiseDirectory()} <br>
	 * Left for compatibility with mark59 v4.1 and earlier
	 * 
	 * @param pr PropertiesReader
	 * @return an existing or otherwise new ScreenshotLoggingHelper
	 * @throws IOException when trying to read the properties file
	 */
	@Deprecated
	public static synchronized ScreenshotLoggingHelper initialiseDirectory(PropertiesReader pr) throws IOException {
		if(instance == null) {
			instance = new ScreenshotLoggingHelperSelenium();
		}
		return instance;
	}
	
}

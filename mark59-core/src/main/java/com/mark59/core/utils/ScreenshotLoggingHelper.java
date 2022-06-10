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

package com.mark59.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Static utility helper class for log file creation.
 * 
 * @author Michael Cohen
 * @author Philip Webb
 * Written: Australian Winter 2019  
 */
public class ScreenshotLoggingHelper {
	private static final Logger LOG = LogManager.getLogger(ScreenshotLoggingHelper.class);	
	
	protected static final String SCREENSHOT_COUNTER = "SCREENSHOT_COUNTER";
	
	
	protected  ScreenshotLoggingHelper(){	}

	
	/**
	 * Returns a fully qualified File name for the image.
	 * 
	 * <p>Returned names take the pattern:<br>
	 *  {Directory}/{As defined by {@link PropertiesKeys#MARK59_PROP_LOGNAME_FORMAT}_{Image Number}_{imageName}.{extension}</p>
	 * 
	 * @param fileNameEnding filename to use for the screenshot
	 * @param extension file extension
	 * @return fully qualified image name
	 */
//	public static String buildFullyQualifiedImageName(String fileNameEnding, String extension) {
//		
//		String fullyQualifiedImageName = MessageFormat.format("{0}{1}{2}_{3}_{4}.{5}", 
//											"this dier", //getScreenshotDirectory(),
//											File.separator, 
//											Thread.currentThread().getName(), 
//											String.format("%03d", StaticCounter.readCount(SCREENSHOT_COUNTER)),
//											fileNameEnding, 
//											extension);
//
//		if (LOG.isTraceEnabled()) LOG.trace(Thread.currentThread().getName() + " : fullyQualifiedImageName = " + fullyQualifiedImageName);
//		
//		// increment counter ready for next image
//		StaticCounter.incrementCount(SCREENSHOT_COUNTER);
//		
//		return fullyQualifiedImageName;
//	}
//	
//	
//	/**
//	 * Save the byte[] to the specified file name, creating the parent directory if  missing (ie initial directory creation)
//	 * 
//	 * @param screenshotLogFilename filename to use for the screenshot
//	 * @param screenshotLogFileData the screenshot data 
//	 */
//	public static void writeScreenshotLog(File screenshotLogFilename, byte[] screenshotLogFileData) {
//
//		new File(screenshotLogFilename.getParent()).mkdirs();
//
//		LOG.info(MessageFormat.format("Writing image to disk: {0}", screenshotLogFilename));
//		System.out.println("[" + Thread.currentThread().getName() + "]  Writing image to disk:" + screenshotLogFilename);
//
//		if (screenshotLogFileData == null ) {
//			screenshotLogFileData = "(null)".getBytes();
//		}
//		
//		try (OutputStream stream = new FileOutputStream(screenshotLogFilename)) {
//			stream.write(screenshotLogFileData);
//
//		} catch (IOException e) {
//			LOG.error("Caught " + e.getClass().getName() + " with message: " + e.getMessage());
//		}
//	}
//	
//	
//	public static void writeExceptionLog(Exception e) {
//		StringWriter sw = new StringWriter();
//		e.printStackTrace(new PrintWriter(sw));
//		String stackTrace = sw.toString(); 
//		
//		ScreenshotLoggingHelper.writeScreenshotLog(
//				new File(ScreenshotLoggingHelper.buildFullyQualifiedImageName("EXCEPTION", "txt")),
//				StringUtils.isNotBlank(stackTrace) ? stackTrace.getBytes() : null);
//	}
	
}

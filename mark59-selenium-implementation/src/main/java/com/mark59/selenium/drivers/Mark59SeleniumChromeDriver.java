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

package com.mark59.selenium.drivers;

import java.io.File;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.mark59.core.utils.ScreenshotLoggingHelper;
import com.mark59.selenium.interfaces.Mark59SeleniumDriver;

/**
 * 
 * Chrom(ium) implementation of a Mark59SeleniumDriver.  
 * 
 * @author Michael Cohen
 * @author Philip Webb
 * Written: Australian Winter 2019  
 */
public class Mark59SeleniumChromeDriver implements Mark59SeleniumDriver<ChromeDriver>  {
	
	private static final Logger LOG = LogManager.getLogger(Mark59SeleniumChromeDriver.class);

	WebDriver webDriver;

	
	/**
	 * @param webDriver the WebDriver to package
	 */
	public Mark59SeleniumChromeDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	
	@Override
	public WebDriver getDriver() {
		return webDriver;
	}
	
	
	@Override
	public String getDriverClass() {
		return this.getDriver().getClass().getName();  
	}
	

	@Override
	public String getDriverLogs() {
		if (!this.getDriver().manage().logs().getAvailableLogTypes().contains(LogType.PERFORMANCE))
			return null;

		List<LogEntry> logs = this.getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();

		StringBuilder allEntriesLogBuilder = new StringBuilder();

		for (LogEntry entry : logs) {
			allEntriesLogBuilder.append(entry.toString()).append("\n");
		}

		return allEntriesLogBuilder.toString();
	}

	
	@Override
	public void clearDriverLogs() {
		if (!this.getDriver().manage().logs().getAvailableLogTypes().contains(LogType.PERFORMANCE))
			return;

		this.getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
	}

	
	@Override
	public void writeDriverLogs(String textFileName) {
		if (LOG.isTraceEnabled())
			LOG.trace(Thread.currentThread().getName() + " : writing driver log, (partial) name " + textFileName);
		
		ScreenshotLoggingHelper.writeScreenshotLog(
					new File(ScreenshotLoggingHelper.buildFullyQualifiedImageName(textFileName, "txt")), getDriverLogBytes());
	}
	
	
	@Override	
	public void bufferDriverLogs(String textFileName) {
		bufferedArtifacts.put(ScreenshotLoggingHelper.buildFullyQualifiedImageName(textFileName, "txt"), getDriverLogBytes());
	}

		
	private byte[] getDriverLogBytes() {
		String allEntriesLog = this.getDriverLogs();
		return StringUtils.isNotBlank(allEntriesLog) ? allEntriesLog.getBytes() : null;
	}
	
	
	/**
	 * Doing a close() before quit() appears to help chromeDriver cleanup its temp directories
	 * https://stackoverflow.com/questions/43289035/chromedriver-not-deleting-scoped-dir-in-temp-folder-after-test-is-complete/
	 */
	@Override
	public void driverDispose() {
		try {
			this.getDriver().close();
			this.getDriver().quit();
		} catch (Exception e) {
			LOG.info("unexpected error trying to close the chomeDriver" + e.getMessage() );
			System.out.println("unexpected error trying to close the chomeDriver" + e.getMessage() );
		}
	}
	
}

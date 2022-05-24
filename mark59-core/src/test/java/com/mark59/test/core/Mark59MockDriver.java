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

package com.mark59.test.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mark59.core.Mark59Driver;

/**
* @author Michael Cohen
* Written: Australian Winter 2019 
*/
public class Mark59MockDriver implements Mark59Driver<MockDriver> {

	private static final Logger LOG = LogManager.getLogger(Mark59MockDriver.class);
	
	
	MockDriver mockbDriver;
	
	/**
	 * @param webDriver the WebDriver to package
	 */
	public Mark59MockDriver(MockDriver mockbDriver) {
		this.mockbDriver = mockbDriver;
	}
	
	
	@Override
	public MockDriver getDriver() {
		return this.mockbDriver;
	}


	@Override
	public byte[] driverTakeScreenshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void driverDispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDriverLogs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearDriverLogs() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getDriverClass() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void documentExceptionState(Exception e) {
		// TODO Auto-generated method stub
	}

}

/*
 *  Copyright 2019 Mark59.com
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

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mark59.core.interfaces.DriverFunctions;
/**
* @author Michael Cohen
* Written: Australian Winter 2019 
*/
public class DriverFunctionMockTest {

	@Mock
	private final MockDriver mockDriver = Mockito.mock(MockDriver.class);
	
	@Test
	public final void bufferScreenshot_bufferAScreenshotWithASpecifiedName_screenshotBufferContainsScreenshot() {
		DriverFunctions<?> w = new DriverFunctionMock(mockDriver);
		w.captureScreenshot();
		assert(w.captureScreenshot() != null);
	}
}
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

package com.mark59.metrics.forms;

/**
 * @author Philip Webb
 * Written: Australian Spring 2020
 * 
 */
public class CommandParameter {
	
	String paramName;
	String paramValue;
	String paramDuplicated;
	
	public CommandParameter() {
	}

	public CommandParameter(String paramName, String paramValue, String paramDuplicated){
		this.paramName = paramName;
		this.paramValue = paramValue;
		this.paramDuplicated = paramDuplicated;
	}
	
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDuplicated() {
		return paramDuplicated;
	}

	public void setParamDuplicated(String paramDuplicated) {
		this.paramDuplicated = paramDuplicated;
	}

	@Override
    public String toString() {
        return   "[paramName="+ paramName + 
        		", paramValue="+ paramValue + 
        		", paramDuplicated="+ paramDuplicated + 
        		"]";
	}
		
}

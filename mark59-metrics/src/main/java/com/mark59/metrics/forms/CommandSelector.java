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
public class CommandSelector {
	
	String commandName;
	boolean commandChecked;
	String executor;
	
	public CommandSelector() {
	}

	public CommandSelector(String commandName, boolean commandChecked, String executor) {
		this.commandName = commandName;
		this.commandChecked = commandChecked;
		this.executor = executor;
	}

	
	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public boolean isCommandChecked() {
		return commandChecked;
	}

	public void setCommandChecked(boolean commandChecked) {
		this.commandChecked = commandChecked;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	@Override
    public String toString() {
        return   "[commandName="+ commandName + 
        		", commandChecked="+ commandChecked + 
        		", executor="+ executor + 
        		"]";
	}
		
}

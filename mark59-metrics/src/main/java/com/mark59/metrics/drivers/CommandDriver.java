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

package com.mark59.metrics.drivers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mark59.core.utils.Mark59Utils;
import com.mark59.metrics.data.beans.Command;
import com.mark59.metrics.data.beans.ServerProfile;
import com.mark59.metrics.pojos.CommandDriverResponse;
import com.mark59.metrics.utils.ServerMetricsWebUtils;
import com.mark59.metrics.utils.AppConstantsServerMetricsWeb.CommandExecutorDatatypes;
import com.mark59.metrics.utils.AppConstantsServerMetricsWeb.OS;

/**
* @author Michael Cohen
* @author Philip Webb
* Written: Australian Winter 2019 
*/
public interface CommandDriver {
	
	Logger LOG = LogManager.getLogger(CommandDriver.class);
	
	String HOSTID = "HOSTID";
	
	
	static String obtainReportedServerId(String server, String alternateServerId) {
		String reportedServerId = server;
		if ( "localhost".equalsIgnoreCase(server) && HOSTID.equals(alternateServerId) ) {

			if (OS.WINDOWS.getOsName().equals(ServerMetricsWebUtils.obtainOperatingSystemForLocalhost())){				
				reportedServerId = System.getenv("COMPUTERNAME");
			} else { 
				reportedServerId = System.getenv("HOSTNAME");
			}
			if (StringUtils.isAllBlank(reportedServerId)){	  // use IP Host Name as a alternative .
				try {
					reportedServerId = InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e) {
					reportedServerId = "unknown";
				}
			}
			
		} else if (StringUtils.isNotEmpty(alternateServerId)) {
			reportedServerId = alternateServerId;
		}
		return reportedServerId; 
	}
	
	
	static CommandDriver init(String commandExecutor, ServerProfile serverProfile) {
		CommandDriver driver;
		if (CommandExecutorDatatypes.WMIC_WINDOWS.getExecutorText().equals(commandExecutor)) {
			driver = new CommandDriverWinWmicImpl(serverProfile);
		} else if (CommandExecutorDatatypes.SSH_LINIX_UNIX.getExecutorText().equals(commandExecutor)){  
			driver = new CommandDriverNixSshImpl(serverProfile);
		} else {  // GROOVY_SCRIPT
			driver = new CommandDriverGroovyScriptImpl(serverProfile);
		}
		return driver;
	}
	
	
	CommandDriverResponse executeCommand(Command command);

	
	static CommandDriverResponse executeRuntimeCommand(String runtimeCommand, String ingoreStderr, CommandExecutorDatatypes executorType) {
		LOG.debug("executeRuntimeCommand : " + runtimeCommand );
		
		CommandDriverResponse commandDriverResponse = new CommandDriverResponse();   
		commandDriverResponse.setCommandFailure(false);
		List<String> rawCommandResponseLines = new ArrayList<>();
		String commandLog = "";
		String line;
				
		Process p = null;
		BufferedReader errors = null;
		BufferedReader reader = null;
		
		try {
			if (CommandExecutorDatatypes.WMIC_WINDOWS.equals(executorType)){
				p = Runtime.getRuntime().exec(runtimeCommand);
			} else {// *nix
				p = Runtime.getRuntime().exec(new String[]{"sh", "-c" , runtimeCommand});	
			}
			p.waitFor();
			
			if ( ! Mark59Utils.resolvesToTrue(ingoreStderr)){
				errors = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while ((line = errors.readLine()) != null) {
					if (line.length() > 0) {
						commandDriverResponse.setCommandFailure(true);
						rawCommandResponseLines.add(line.trim());
					}
				}
				errors.close();
			} 			

			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					rawCommandResponseLines.add(line.trim());
				}
			}
			reader.close();
			p.destroy();
			
		} catch (Exception e) {
			commandDriverResponse.setCommandFailure(true);			
			StringWriter stackTrace = new StringWriter();
			e.printStackTrace(new PrintWriter(stackTrace));
			commandLog+= "<br>A faiure has occured attempting to execute the command : " + e.getMessage() + "<br>" + stackTrace.toString() + "<br>";
			LOG.debug("Command failure : " + runtimeCommand + ":\n" + e.getMessage() + stackTrace.toString());
			try {Objects.requireNonNull(errors).close();} catch (Exception ignored){}
			try {Objects.requireNonNull(reader).close();} catch (Exception ignored){}
			try {Objects.requireNonNull(p).destroy();} catch (Exception ignored){}
		}
		
		rawCommandResponseLines.forEach(LOG::debug);
		commandDriverResponse.setRawCommandResponseLines(rawCommandResponseLines);
		commandDriverResponse.setCommandLog(commandLog);
		return commandDriverResponse;
	}
	
}

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

package com.mark59.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mark59.core.utils.Mark59Constants.DatabaseTxnTypes;
import com.mark59.core.utils.Mark59Constants.JMeterFileDatatypes;

/**
 * General catch-all class for useful functions
 *  
 *  @author Philip Webb
 *  Written: Australian Summer 2020 
 */
public class Mark59Utils {
	
	private static final Logger LOG = LogManager.getLogger(Mark59Utils.class);

	/**
	 * Allows a map with additional or entries that exist in a base map to be combined, or where the same key exists in both maps, the 
	 * value in the override map will be used.  
	 * <p>returned as Jmeter Arguments 
	 * 
	 * @param baseMap  Map of base key values 
	 * @param additionalEntriesMap  additional of override key values
	 * @return jmeterArguments
	 */
	public static Arguments mergeMapWithAnOverrideMap(Map<String,String> baseMap, Map<String, String> additionalEntriesMap) {
		Arguments jmeterArguments = new Arguments();
		Map<String, String> jmeterArgumentsMap = new LinkedHashMap<>();
		Map<String,String> baseMapMergedWithAdditionalEntriesMap = new LinkedHashMap<>();
		
		for (Map.Entry<String, String> defaultEntry : baseMap.entrySet()) {
			if (additionalEntriesMap.containsKey(defaultEntry.getKey())){
				baseMapMergedWithAdditionalEntriesMap.put(defaultEntry.getKey(), additionalEntriesMap.get(defaultEntry.getKey()));
				additionalEntriesMap.remove(defaultEntry.getKey());
			} else {
				baseMapMergedWithAdditionalEntriesMap.put(defaultEntry.getKey(), baseMap.get(defaultEntry.getKey()));
			}
		}
		
		jmeterArgumentsMap.putAll(additionalEntriesMap);
		jmeterArgumentsMap.putAll(baseMapMergedWithAdditionalEntriesMap);
		
		for (Map.Entry<String, String> parameter : jmeterArgumentsMap.entrySet()) {
			jmeterArguments.addArgument(parameter.getKey(), parameter.getValue());
		}
		
		if (LOG.isDebugEnabled()){LOG.debug("jmeter arguments at end of mergeMapWithAnOverrideMap : " + Arrays.toString(jmeterArguments.getArgumentsAsMap().entrySet().toArray()));} 
		return jmeterArguments;
	}
	
	
	/**
	 * Strings starting with 't', 'T', 'y', 'Y' are assumed to mean true,
	 * all other values will return false
	 *
	 * @param str  the string to be resolved to TRUE for FALSE
	 * @return boolean true or false
	 */
	public static boolean resolvesToTrue(final String str) {
		if (StringUtils.isBlank(str)) {	return false;}
		if (str.trim().toLowerCase().startsWith("t")) {return true;}
		return str.trim().toLowerCase().startsWith("y");
	}
   
	
	/**
	 * Convenience method to print out a Map  
	 * 
	 * @param <K> Map entry key 
	 * @param <V> Map entry value
	 * @param map map to pretty print 
	 * @return formatted string representation of the map
	 */
	public static <K,V> String prettyPrintMap (final Map<K,V> map) {
	    String prettyOut = "\n    ------------------------------- ";
	    
	    if (map != null && !map.isEmpty() ){
	    
			for (Entry<K,V> mapEntry: map.entrySet()) {
				prettyOut += "\n   | " + mapEntry.getKey() + " | " + mapEntry.getValue() + " | " ;
			}
	    } else {
			prettyOut+= "\n   |        empty or null map     | " ;
	    }
	    return prettyOut+= "\n    ------------------------------- \n";	
	}
	


	/**
	 * Maps JMeter file 'dt' (datatype) column to the TXN_TYPE values use in the Mark59 database tables
	 *  <p><b>Mapping Summary  (JMeter file type 'maps to'  database TXN_TYPE) :</b>
	 *  <table>
	 *  <caption>__________________________________________________________________________________</caption>  
  	 *	<tr><td>CPU_UTIL  </td><td> --&gt; </td><td>CPU_UTIL   		</td><td></td></tr>
  	 *	<tr><td>MEMORY    </td><td> --&gt; </td><td>MEMORY     		</td><td></td></tr>
  	 *	<tr><td>DATAPOINT </td><td> --&gt; </td><td>DATAPOINT  		</td><td></td></tr>
  	 *	<tr><td>CDP       </td><td> --&gt; </td><td>TRANSACTION		</td><td>but tagged as a DevTools (CDP) transaction *</td></tr>
  	 *	<tr><td>'' (blank)</td><td> --&gt; </td><td>TRANSACTION		</td><td>a standard transaction</td></tr>
  	 *	<tr><td>(unmapped)</td><td> --&gt; </td><td>TRANSACTION		</td><td>default catch-all</td></tr>
  	 *  </table>
  	 *  <p>*  A separate check needs to be done when processing JMeter files for CDP tagging  
  	 *  
	 * @see Mark59Constants.DatabaseTxnTypes 
	 * @see Mark59Constants.JMeterFileDatatypes 
	 * @param jmeterFileDatatype on of the possible datatype (dt) values on the JMeter results file
	 * @return DatabaseDatatypes (string value)
	 */
	public static String convertJMeterFileDatatypeToDbTxntype(String jmeterFileDatatype) {
		if ( JMeterFileDatatypes.TRANSACTION.getDatatypeText().equals(jmeterFileDatatype)){    //expected to map any blank to transaction
			return DatabaseTxnTypes.TRANSACTION.name();
		} else if ( JMeterFileDatatypes.CDP.getDatatypeText().equals(jmeterFileDatatype)){    
			return DatabaseTxnTypes.TRANSACTION.name();			
		} else if ( JMeterFileDatatypes.CPU_UTIL.getDatatypeText().equals(jmeterFileDatatype)){    
			return DatabaseTxnTypes.CPU_UTIL.name();
		} else if ( JMeterFileDatatypes.MEMORY.getDatatypeText().equals(jmeterFileDatatype)){   
			return DatabaseTxnTypes.MEMORY.name();
		} else if ( JMeterFileDatatypes.DATAPOINT.getDatatypeText().equals(jmeterFileDatatype)){   
			return DatabaseTxnTypes.DATAPOINT.name();
		} else {
			return DatabaseTxnTypes.TRANSACTION.name();   // just assume its a transaction (so a 'PARENT' would become a transaction on the db) 			
		}
	}	
	

	/**
	 * Constructs metric transaction names based on server id and rules (using data that can be obtained from commandResponseParser) 
	 * 
	 * <p>A key element of creating metric transaction ids is the mapping of Mark59 Metric Transaction Types
	 * as the transaction id prefix.  The table below summarizes the relationships.
	 * 
	 *  <p><b>Mapping Summary  (Meter Transaction Type 'maps to' transaction id prefix)</b>
	 *  <table>
	 *  <caption>_________________________________</caption>
  	 *	<tr><td>CPU_UTIL </td><td> --&gt; </td><td>CPU_</td></tr>	   
  	 *	<tr><td>MEMORY   </td><td> --&gt; </td><td>Memory_</td></tr>	   
  	 *	<tr><td>DATAPOINT</td><td> --&gt; </td><td>no prefix</td></tr>
  	 *	</table>
	 * 
	 * <p>Suffixes are added after the server name, if entered.
	 * 
	 * <p>The general format is : <code>prefix + reported server id + suffix (if provided)</code>   
	 * 
	 * @param metricTxnType Metric Transaction Types as recorded in the database
	 * @param reportedServerId     Server Name as it will appear in the transaction
	 * @param metricNameSuffix     Optional suffix appended to the end of the transaction name  
	 * @return the candidate transactionId to be used 
	 */
	public static String constructCandidateTxnIdforMetric(String metricTxnType, String reportedServerId, String metricNameSuffix ) {
		String txnIdPrefix = "";
		if ( DatabaseTxnTypes.CPU_UTIL.name().equals(metricTxnType)){  
			txnIdPrefix = "CPU_";	
		} else if ( DatabaseTxnTypes.MEMORY.name().equals(metricTxnType)){    
			txnIdPrefix = "Memory_";
		} 
		
		String candidateTxnId = txnIdPrefix + reportedServerId;
		
		if 	(StringUtils.isNotBlank(metricNameSuffix)){
			candidateTxnId = candidateTxnId + "_" + metricNameSuffix;
		}
		return candidateTxnId;
	}
	
	
	/**
	 * @param commaDelimitedString string with comma(",") being used as a field delimiter 
	 * @return the Set of split strings 
	 */
	public static Set<String> commaDelimStringToStringSet(String commaDelimitedString) {
		List<String> listOfStrings = commaDelimStringToStringList(commaDelimitedString);
		return new HashSet<>(listOfStrings);
	}
	
	/**
	 * @param commaDelimitedString string with comma(",") being used as a field delimiter 
	 * @return the split list of strings
	 */
	public static List<String> commaDelimStringToStringList(String commaDelimitedString) {
		List<String> listOfStrings = new ArrayList<>();
		if ( StringUtils.isNotBlank(commaDelimitedString)){
			listOfStrings =  Arrays.asList(StringUtils.stripAll(StringUtils.split(commaDelimitedString, ",")));
		} 
		return listOfStrings;
	}
	
	/**
	 * @param pipeDelimitedString string with pipe char ("|") being used as a field delimiter 
	 * @return the split list of strings 
	 */
	public static List<String> pipeDelimStringToStringList(String pipeDelimitedString) {
		List<String> listOfStrings = new ArrayList<>();
		if ( StringUtils.isNotBlank(pipeDelimitedString)){
			listOfStrings =  Arrays.asList(StringUtils.stripAll(StringUtils.split(pipeDelimitedString, "|")));
		} 
		return listOfStrings;
	}


	/**
	 * @return LINUX is returned as the default, other options are WINDOWS and MAC 
	 */
	public static String obtainOperatingSystemForLocalhost() {	
		String operatingSystem = Mark59Constants.OS.LINUX.getOsName();
		
		String osNameProp = System.getProperty("os.name", Mark59Constants.OS.LINUX.getOsName())
				.toUpperCase(java.util.Locale.ENGLISH);
		
		if ( osNameProp.indexOf("WIN") >= 0 ){
			operatingSystem = Mark59Constants.OS.WINDOWS.getOsName();
		} else if ( osNameProp.indexOf("MAC") >= 0 ) {
			operatingSystem = Mark59Constants.OS.MAC.getOsName();
		} 
		return operatingSystem;
	}
	
}

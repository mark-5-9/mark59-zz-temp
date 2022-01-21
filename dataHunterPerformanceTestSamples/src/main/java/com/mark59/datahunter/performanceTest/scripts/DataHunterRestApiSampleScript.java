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

package com.mark59.datahunter.performanceTest.scripts;


import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.logging.log4j.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.mark59.core.utils.Log4jConfigurationHelper;
import com.mark59.core.utils.Mark59Constants;
import com.mark59.dataHunterRestApiDSL.DataHunterRestApiInvoker;
import com.mark59.dataHunterRestApiDSL.DataHunterRestApiResponsePojo;
import com.mark59.datahunter.performanceTest.dsl.helpers.DslConstants;
import com.mark59.selenium.corejmeterimpl.JmeterFunctionsForSeleniumScripts;
import com.mark59.selenium.corejmeterimpl.KeepBrowserOpen;
import com.mark59.selenium.corejmeterimpl.SeleniumAbstractJavaSamplerClient;
import com.mark59.selenium.drivers.SeleniumDriverFactory;

/**
 *  Simple Demo of DataHunter Rest API usage.  The example shows usage within a Mark59 Selenium script, but the Rest API is can use used
 *  within any Java program.  
 *  
 * @see SeleniumAbstractJavaSamplerClient
 * 
 * @author Philip Webb
 * Written: Australian Summer 2021/22
 * 
 */
public class DataHunterRestApiSampleScript  extends SeleniumAbstractJavaSamplerClient {


	@Override
	protected Map<String, String> additionalTestParameters() {
		Map<String, String> jmeterAdditionalParameters = new LinkedHashMap<>();
		jmeterAdditionalParameters.put("DATAHUNTER_URL",			"http://localhost:8081/dataHunter");
		jmeterAdditionalParameters.put("DATAHUNTER_APPLICATION_ID", "DATAHUNTER_PV_TEST_API");
		jmeterAdditionalParameters.put(SeleniumDriverFactory.DRIVER, Mark59Constants.CHROME);
		jmeterAdditionalParameters.put(SeleniumDriverFactory.HEADLESS_MODE, String.valueOf(false));
		jmeterAdditionalParameters.put(SeleniumDriverFactory.PROXY, "");
		jmeterAdditionalParameters.put(SeleniumDriverFactory.ADDITIONAL_OPTIONS, "");
		return jmeterAdditionalParameters;			
	}
	

	@Override
	protected void runSeleniumTest(JavaSamplerContext context, JmeterFunctionsForSeleniumScripts jm,  WebDriver driver){

		String dataHunterUrl 	= context.getParameter("DATAHUNTER_URL");
		String application 		= context.getParameter("DATAHUNTER_APPLICATION_ID");
		String lifecycle		= "apitest";

// 		delete any existing policies for this api testing, then check all is ok by adding a policy via the web application
		
		jm.startTransaction("DH_apitest_0001_loadInitialPage");
		driver.get(dataHunterUrl + DslConstants.DELETE_MULTIPLE_POLICIES_URL_PATH + "?application=" + application);
		jm.endTransaction("DH_apitest_0001_loadInitialPage");
		
		driver.findElement(By.id("lifecycle")).sendKeys(lifecycle);
		
		jm.startTransaction("DH_lifecycle_0100_deleteMultiplePolicies");
		driver.findElement(By.id("submit")).submit();
		checkSqlOk(driver.findElement(By.id("sqlResult")));
		jm.endTransaction("DH_lifecycle_0100_deleteMultiplePolicies");	

		driver.get(dataHunterUrl + DslConstants.ADD_POLICY_URL_PATH + "?application=" + application);

		driver.findElement(By.id("identifier")).sendKeys("ID-ADDED-USING-WEB-PAGE"); 		
		driver.findElement(By.id("lifecycle")).sendKeys(lifecycle); 		
		Select dropdown = new Select(driver.findElement(By.id("useability")));
		dropdown.selectByVisibleText(DslConstants.UNUSED);
		
		jm.startTransaction("DH_lifecycle_0200_addPolicy");
		driver.findElement(By.id("submit")).submit();
		checkSqlOk(driver.findElement(By.id("sqlResult")));
		jm.endTransaction("DH_lifecycle_0200_addPolicy");

		assertEquals(1L, Long.parseLong(driver.findElement(By.id("rowsAffected")).getText()));
		
		
// 		start going thru invocations of the the DataHunter Rest API functions
		
		DataHunterRestApiInvoker restApi = new DataHunterRestApiInvoker(dataHunterUrl);
		
		
		DataHunterRestApiResponsePojo response = restApi.printPolicy(application, "ID-ADDED-USING-WEB-PAGE", lifecycle );
		
		System.out.println("FailMsg = " + response.getFailMsg());
		System.out.println("Succes = " + response.getSucces()  );
		System.out.println("Policies = " + response.getPolicies() );
		
		
		
		

		driver.findElement(By.linkText("Back")).click();
		
//		jm.writeBufferedArtifacts();
	}
	

	private void checkSqlOk(WebElement sqlResultWebElement) {
		String sqlResultText = sqlResultWebElement.getText();
		if ( !"PASS".equals(sqlResultText) ) {
			throw new RuntimeException("SQL issue (" + sqlResultText + ")");   
		}
	}

	
	/**
	 *  Simple Demo of DataHunter Rest API usage  
	 */
	public static void main(String[] args) {
		Log4jConfigurationHelper.init(Level.INFO) ;
		DataHunterRestApiSampleScript thisTest = new DataHunterRestApiSampleScript();
		thisTest.runSeleniumTest(KeepBrowserOpen.ONFAILURE);
	}
		
}

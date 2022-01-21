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

package com.mark59.datahunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mark59.datahunter.application.SqlWithParms;
import com.mark59.datahunter.data.beans.Policies;
import com.mark59.datahunter.data.policies.dao.PoliciesDAO;
import com.mark59.datahunter.model.DataHunterRestApiResponsePojo;
import com.mark59.datahunter.model.PolicySelectionCriteria;

/**
 * Controls API calls from the server metrics web application, and from any JMeter Java Sampler implementation using a direct API call
 * to obtain metrics from a server (see ServerMetricsCaptureViaWeb).
 *   
 * @author Philip Webb
 * Written: Australian Spring 2021  
 */

@RestController
@RequestMapping("/api")
public class DataHunterRestController {
	
	@Autowired
	PoliciesDAO policiesDAO;	

	
	/**
	 *  REST Service call to DataHunter.
	 *  <p>Controls DataHunter database functions to satisfy requests from the DataHunter REST Api.
	 *  <p>With the mark59 framework, it can be invoked from any test script requiring data stored on the DataHunter database 
	 *  <p> For example, assuming data for applicationId "DATAHUNTER_PV_TEST" exists on the database, and assuming a dataHunter 
	 *  localhost instance is running, go get a random entry out of the UNUSED rows for id DATAHUNTER_PV_TEST, the Url used would be <br>
	 *  http://localhost:8081/dataHunter/api/metric?reqApplicatinIdXxxxxxxx=DATAHUNTER_PV_TEST&....??????????
	 *  
	 * @param aaaaaaaaaaaaaaaaa
	 * @param bbbbbbbbbbbbbbbbbbbb
	 * @return org.springframework.http.ResponseEntity (Json format)
	 * @see xxxxxxxxxxxxxxxxxx
	 */
	@GetMapping(path = "/printPolicy")
	public ResponseEntity<Object> printPolicy(@RequestParam String application, @RequestParam String identifier, @RequestParam(required=false) String lifecycle){
	
		PolicySelectionCriteria policySelectionCriteria = new PolicySelectionCriteria();
		policySelectionCriteria.setApplication(application);
		policySelectionCriteria.setIdentifier(identifier);
		policySelectionCriteria.setLifecycle(lifecycle);
		
		policySelectionCriteria.setSelectClause(" application, identifier, lifecycle, useability,otherdata, created, updated, epochtime ");
		SqlWithParms sqlWithParms = policiesDAO.constructSelectPolicySql(policySelectionCriteria);
		List<Policies> policiesList = policiesDAO.runSelectPolicieSql(sqlWithParms);
		
//		return ResponseEntity.ok(ServerProfileRunner.commandsResponse(reqServerProfileName, reqTestMode, 
//				serverProfilesDAO, serverCommandLinksDAO, commandsDAO, commandParserLinksDAO, commandResponseParsersDAO));	
		
		DataHunterRestApiResponsePojo repsonse = new DataHunterRestApiResponsePojo();
		repsonse.setPolicies(policiesList);
		repsonse.setSucces(String.valueOf(true)); 
		repsonse.setFailMsg("");
		return ResponseEntity.ok(repsonse);	
	}
	
}

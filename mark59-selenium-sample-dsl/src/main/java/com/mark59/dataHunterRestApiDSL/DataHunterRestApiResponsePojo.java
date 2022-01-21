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

package com.mark59.dataHunterRestApiDSL;

import java.util.List;


/**
 * @author Philip Webb
 * Written: Australian Summer 2021/22
 * 
 * Used to provide the response back to the client for the DataHunter Rest API call.
 * <p>Note this class (DataHunterRestApiResponsePojo) and 'Policies' have copies in the dataHunter web 
 * project and the mark59-selenium-sample-dsl project.  Done to keeps the dataHunter project
 * (SpringBoot dependencies) and the rest of the framework (eg JMeter dependencies) independent 
 * of each other. May change this in the future.   
 *      
 */
public class DataHunterRestApiResponsePojo {

	private List<Policies> policies;
	private String succes;
	private String failMsg;	

	
	public DataHunterRestApiResponsePojo() {
	}


	public List<Policies> getPolicies() {
		return policies;
	}


	public void setPolicies(List<Policies> policies) {
		this.policies = policies;
	}

	public String getSucces() {
		return succes;
	}

	public void setSucces(String succes) {
		this.succes = succes;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	@Override
    public String toString() {
       return   "[policies" + policies
        		+ ", succes="+ succes
        		+ ", failMsg="+ failMsg
        		+ "]";
	}
		
}

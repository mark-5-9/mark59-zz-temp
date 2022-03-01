package com.mark59.datahunter.restapi.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.mark59.datahunter.application.DataHunterConstants;
import com.mark59.datahunter.data.beans.Policies;
import com.mark59.datahunter.model.AsyncMessageaAnalyzerResult;
import com.mark59.datahunter.model.CountPoliciesBreakdown;
import com.mark59.datahunter.model.DataHunterRestApiResponsePojo;
import com.mark59.datahunter.restapi.DataHunterRestApiClient;


/**
 * Detailed use cases and verifications for the DataHunter Rest API client and service.:
 * 
 * @author Philip Webb
 * Written: Australian Summer 2021/22
 *
 */
public class DataHunterRestApiClientSampleUsage {
	
	
	/**
	 * This is functionally equivalent to the DataHunterSeleniumFunctionalTest.asyncLifeCycleTestWithUseabilityUpdate() web application test 
	 * in the dataHunterFunctionalTest project (held on the mark-5-9/mark59-xtras GitHub repo), but running the REST API instead of the web 
	 * 'Asynchronous Message Analyzer' function.
	 *  
	 * @param dhApiClient DataHunterRestApiClient
	 * @see <a href="https://github.com/mark-5-9/mark59-xtras/blob/master/dataHunterFunctionalTest/src/main/java/com/mark59/datahunter/functionalTest/scripts/DataHunterSeleniumFunctionalTest.java#L47">Web App Equivalent Test</a>  
	 */
	public void asyncLifeCycleTestWithUseabilityUpdate(DataHunterRestApiClient dhApiClient) {

		dhApiClient.deleteMultiplePolicies("TESTAPI_ASYNC_TOUSED", null, null);
		dhApiClient.addPolicy( new Policies("TESTAPI_ASYNC_TOUSED", "T99-testonly-01", "FIRSTONE", "UNPAIRED", "", 1460613152000L));
		dhApiClient.addPolicy( new Policies("TESTAPI_ASYNC_TOUSED", "T99-testonly-01", "between",  "UNPAIRED", "", 1460613152009L));
		dhApiClient.addPolicy( new Policies("TESTAPI_ASYNC_TOUSED", "T99-testonly-01", "LASTONE",  "UNPAIRED", "", 1460613153001L));
		dhApiClient.addPolicy( new Policies("TESTAPI_ASYNC_TOUSED", "T99-testonly-02", "FIRSTONE", "UNPAIRED", "", 1460613153000L));
		dhApiClient.addPolicy( new Policies("TESTAPI_ASYNC_TOUSED", "T99-testonly-02", "LASTONE",  "UNPAIRED", "", 1460613155001L));
		
		DataHunterRestApiResponsePojo response = dhApiClient.asyncMessageAnalyzer(DataHunterConstants.EQUALS,"TESTAPI_ASYNC_TOUSED", null, null, "USED");

		int i=0;
		List<AsyncMessageaAnalyzerResult>  asyncResults = response.getAsyncMessageaAnalyzerResults();
		System.out.println( "    asyncMessageAnalyzerPrintResults  (" + asyncResults.size() + ") - asyncLifeCycleTestWithUseabilityUpdate" );		
		System.out.println( "    ------------------------------- ");		
		for (AsyncMessageaAnalyzerResult asyncResult : asyncResults) {
			System.out.println("    " +  ++i + "   " + asyncResult);
		}
		
		assertEquals(2, asyncResults.size());		
		assertEquals("[application=TESTAPI_ASYNC_TOUSED, startsWith=null, identifier=T99-testonly-02, lifecycle=null, useability=USED, selectOrder=null], starttm= 1460613153000, endtm= 1460613155001, differencetm= 2001]", asyncResults.get(0).toString());
		assertEquals("[application=TESTAPI_ASYNC_TOUSED, startsWith=null, identifier=T99-testonly-01, lifecycle=null, useability=USED, selectOrder=null], starttm= 1460613152000, endtm= 1460613153001, differencetm= 1001]", asyncResults.get(1).toString());
		
		for (AsyncMessageaAnalyzerResult  pairedAsyncTxn : asyncResults ) {
			// example of a typical transaction name you could set (and its response time)
			System.out.println( "    Txn Name :  "  + pairedAsyncTxn.getApplication() + "_" + pairedAsyncTxn.getIdentifier() + "  Respsonse time (Assumed msecs) : "  + pairedAsyncTxn.getDifferencetm()  );				
		}
 	    System.out.println( "    --------------------------------------------");	
 	    // clean up     
		assertEquals(new Integer(5), dhApiClient.deleteMultiplePolicies("TESTAPI_ASYNC_TOUSED", null, null).getRowsAffected()); 	
	}
	
	
	public void basicPolicyAddPrintDeleteChecks(DataHunterRestApiClient dhApiClient){
//		System.out.println("DataHunterRestApiResponsePojo =" + response  );

		DataHunterRestApiResponsePojo response = dhApiClient.deletePolicy("testapi", "id1", "");	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		response = dhApiClient.deletePolicy("testapi", "id2", null);	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		response = dhApiClient.deletePolicy("testapi", "id3", "setepochtime");	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		response = dhApiClient.deletePolicy("testapi", "id3", "setepochtime");	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		
		response = dhApiClient.addPolicy(new Policies("testapi","id1", "", "USED", null, null));			
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		
		response = dhApiClient.printPolicy("testapi", "id1");			
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","id1", "", "USED", "", null), response.getPolicies().get(0));
		
		response = dhApiClient.addPolicy(new Policies("testapi","id1", "duplicatedid", "USED", "", null));	
		
		response = dhApiClient.printPolicy("testapi", "id1");	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(1), response.getRowsAffected());
		assertEquals(1, response.getPolicies().size()); 		
		assertsOnPolicy(new Policies("testapi","id1", "", "USED", "", null), response.getPolicies().get(0));
		
		response = dhApiClient.printPolicy("testapi", "id1", "");	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","id1", "", "USED", "", null), response.getPolicies().get(0));
		
		response = dhApiClient.printPolicy("testapi", "id1", null);	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","id1", "", "USED", "", null), response.getPolicies().get(0));
		
		response = dhApiClient.printPolicy("testapi", "id1", "duplicatedid");	
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","id1", "duplicatedid", "USED", "", null), response.getPolicies().get(0));
		
		response = dhApiClient.printPolicy("testapi", "doesnotexist", "duplicatedid");	
		assertEquals(String.valueOf(false), response.getSuccess()); 
		assertEquals(new Integer(0), response.getRowsAffected()); 		
		assertEquals(0, response.getPolicies().size()); 
		
		dhApiClient.addPolicy(new Policies("testapi","id2", "", "USED", "", null));	
		
		response = dhApiClient.deletePolicy("testapi", "id1", ""); 
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertEquals(1, response.getPolicies().size()); 
		assertEquals("", response.getFailMsg()); 
		assertsOnPolicy(new Policies("testapi","id1", "", null, null, null), response.getPolicies().get(0));

		response = dhApiClient.deletePolicy("testapi", "id1","duplicatedid"); 
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertEquals(1, response.getPolicies().size()); 
		assertEquals("", response.getFailMsg()); 
		assertsOnPolicy(new Policies("testapi","id1", "duplicatedid", null, null, null), response.getPolicies().get(0));	

		response = dhApiClient.deletePolicy("testapi", "id1","duplicatedid"); 
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(0), response.getRowsAffected()); 		
		assertEquals(1, response.getPolicies().size()); 
		assertEquals("No rows matching the selection.", response.getFailMsg()); 
		assertsOnPolicy(new Policies("testapi","id1", "duplicatedid", null, null, null), response.getPolicies().get(0));			
	
		response = dhApiClient.deletePolicy("testapi", "id2",""); 
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertEquals("", response.getFailMsg()); 
		assertsOnPolicy(new Policies("testapi","id2", "", null, null, null), response.getPolicies().get(0));	
		
		response = dhApiClient.addPolicy(new Policies("testapi","id3", "setepochtime", "UNUSED", "otherstuff", 1643673346936L));			
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(1, response.getPolicies().size()); 
		assertsOnPolicy(new Policies("testapi","id3", "setepochtime", "UNUSED", "otherstuff", 1643673346936L), response.getPolicies().get(0));
		
		response = dhApiClient.printPolicy("testapi","id3", "setepochtime");			
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(1, response.getPolicies().size()); 
		assertsOnPolicy(new Policies("testapi","id3", "setepochtime", "UNUSED", "otherstuff", 1643673346936L), response.getPolicies().get(0));

		response = dhApiClient.printPolicy("testapi","id3");			
		assertEquals(String.valueOf(false), response.getSuccess()); 
		assertEquals(0, response.getPolicies().size()); 
		assertEquals("No rows matching the selection.", response.getFailMsg()); 
		
		response = dhApiClient.addPolicy(new Policies("testapi","id3", "setepochtime", "USED", "ALREADYEXISTS!!", 1643673346936L));			
		assertEquals(String.valueOf(false), response.getSuccess()); 
		assertEquals(1, response.getPolicies().size()); 
		assertsOnPolicy(new Policies("testapi","id3", "setepochtime", "UNUSED", "ALREADYEXISTS!!", 1643673346936L), response.getPolicies().get(0));
		assertTrue("error should contain application (testapi)" , response.getFailMsg().contains("testapi") ); 
		assertTrue("error should contain idenifier (id3)" , response.getFailMsg().contains("id3") ); 
		assertTrue("error should contain lifecycle (setepochtime)" , response.getFailMsg().contains("setepochtime") ); 
		
		response = dhApiClient.deletePolicy("testapi", "id3", "setepochtime");	
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(1), response.getRowsAffected()); 			
	}
	
	
	public void workingWithMultiplePolicies(DataHunterRestApiClient dhApiClient){
		
		create6testPolices(dhApiClient);	
		DataHunterRestApiResponsePojo response = dhApiClient.deleteMultiplePolicies("nonexistingapp", null, null);
		assertEquals(String.valueOf(true), response.getSuccess() ); 
		assertEquals(new Integer(0), response.getRowsAffected()); 	
		response = dhApiClient.deleteMultiplePolicies("testapi", null, "USED");
		assertEquals(new Integer(3), response.getRowsAffected()); 	
		response = dhApiClient.deleteMultiplePolicies("testapi", null, "USED");
		assertEquals(new Integer(0), response.getRowsAffected());
		response = dhApiClient.deleteMultiplePolicies("testapi", null, "");
		assertEquals(new Integer(2), response.getRowsAffected());
		response = dhApiClient.deleteMultiplePolicies("otherapp", "", "");
		assertEquals(new Integer(1), response.getRowsAffected());

		
		create6testPolices(dhApiClient);	
		response = dhApiClient.printSelectedPolicies("testapi", null, null);
		assertEquals(new Integer(5), response.getRowsAffected());
		assertEquals(5, response.getPolicies().size());
		assertTrue(response.getPolicies().get(0).toString().startsWith("[application=testapi, identifier=im4, lifecycle=nonblanklc, useability=UNUSED, otherdata=,"));
		assertTrue(response.getPolicies().get(1).toString().startsWith("[application=testapi, identifier=im3, lifecycle=duplicatedid, useability=REUSABLE, otherdata=duplicated id,"));
		assertTrue(response.getPolicies().get(2).toString().startsWith("[application=testapi, identifier=im3, lifecycle=nonblanklc, useability=USED, otherdata=otherdata3,"));
		assertTrue(response.getPolicies().get(3).toString().startsWith("[application=testapi, identifier=im2, lifecycle=, useability=USED, otherdata=,"));
		assertTrue(response.getPolicies().get(4).toString().startsWith("[application=testapi, identifier=im1, lifecycle=, useability=USED, otherdata=,"));
		
		response = dhApiClient.printSelectedPolicies("testapi", null, "USED");
		assertEquals(3, response.getPolicies().size());		
		assertTrue(response.getPolicies().get(0).toString().startsWith("[application=testapi, identifier=im3, lifecycle=nonblanklc, useability=USED, otherdata=otherdata3,"));
		assertTrue(response.getPolicies().get(1).toString().startsWith("[application=testapi, identifier=im2, lifecycle=, useability=USED, otherdata=,"));
		assertTrue(response.getPolicies().get(2).toString().startsWith("[application=testapi, identifier=im1, lifecycle=, useability=USED, otherdata=,"));		
	
		response = dhApiClient.printSelectedPolicies("testapi", "nonblanklc", "");
		assertEquals(2, response.getPolicies().size());		
		assertTrue(response.getPolicies().get(0).toString().startsWith("[application=testapi, identifier=im4, lifecycle=nonblanklc, useability=UNUSED, otherdata=,"));
		assertTrue(response.getPolicies().get(1).toString().startsWith("[application=testapi, identifier=im3, lifecycle=nonblanklc, useability=USED, otherdata=otherdata3,"));	

		response = dhApiClient.printSelectedPolicies("testapi", "nonblanklc", "USED");
		assertEquals(1, response.getPolicies().size());		
		assertTrue(response.getPolicies().get(0).toString().startsWith("[application=testapi, identifier=im3, lifecycle=nonblanklc, useability=USED, otherdata=otherdata3,"));

		response = dhApiClient.printSelectedPolicies("doesntexist", "nonblanklc", "USED");	
		assertEquals(0, response.getPolicies().size());				
		
		
		response = dhApiClient.deleteMultiplePolicies("testapi", "nonblanklc", null);
		assertEquals(new Integer(2), response.getRowsAffected()); 	
		response = dhApiClient.printSelectedPolicies("testapi", "", "");
		assertEquals(new Integer(3), response.getRowsAffected());
		response = dhApiClient.deleteMultiplePolicies("testapi", "", "USED");
		assertEquals(new Integer(2), response.getRowsAffected());
		response = dhApiClient.printSelectedPolicies("testapi", null, null);
		assertEquals(new Integer(1), response.getRowsAffected());
		assertTrue(response.getPolicies().get(0).toString().startsWith("[application=testapi, identifier=im3, lifecycle=duplicatedid, useability=REUSABLE, otherdata=duplicated id,"));
	}


	
	public void policyCountsAndBreakdowns(DataHunterRestApiClient dhApiClient){
		create6testPolices(dhApiClient);
		
		DataHunterRestApiResponsePojo response = dhApiClient.countPolicies("testapi", "nonblanklc", "USED");
		assertEquals(new Integer(1), response.getRowsAffected());
		assertEquals("[[application=testapi, identifier=null, lifecycle=nonblanklc, useability=USED, otherdata=null, created=null, updated=null, epochtime=null]]", response.getPolicies().toString()); 
		
		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", "nonblanklc", "USED");
		assertEquals(new Integer(1), response.getRowsAffected());
		assertEquals("[[application=testapi, identifier=null, lifecycle=nonblanklc, useability=USED, otherdata=EQUALS, created=null, updated=null, epochtime=null]]", response.getPolicies().toString());
		assertEquals(1, response.getCountPoliciesBreakdown().size());	
		assertEquals("[[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]]", response.getCountPoliciesBreakdown().toString());
	
		assertEquals(new Integer(5), dhApiClient.countPolicies("testapi", null, null).getRowsAffected());

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", null, null);
		assertEquals(4, response.getCountPoliciesBreakdown().size());		
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=, useability=USED, selectOrder=null], rowCount=2]", 				response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=duplicatedid, useability=REUSABLE, selectOrder=null], rowCount=1]", response.getCountPoliciesBreakdown().get(1).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=UNUSED, selectOrder=null], rowCount=1]", 	response.getCountPoliciesBreakdown().get(2).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		response.getCountPoliciesBreakdown().get(3).toString());
				
		assertEquals(new Integer(5), dhApiClient.countPolicies("testapi", "", "").getRowsAffected() ); 

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", "", "");
		assertEquals(4, response.getCountPoliciesBreakdown().size());		
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=, useability=USED, selectOrder=null], rowCount=2]", 				response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=duplicatedid, useability=REUSABLE, selectOrder=null], rowCount=1]", response.getCountPoliciesBreakdown().get(1).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=UNUSED, selectOrder=null], rowCount=1]", 	response.getCountPoliciesBreakdown().get(2).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		response.getCountPoliciesBreakdown().get(3).toString());
		
		assertEquals(new Integer(0), dhApiClient.countPolicies("nonexisting", "", "").getRowsAffected() ); 

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "nonexisting", "", "");
		assertEquals(0, response.getCountPoliciesBreakdown().size());		
		assertEquals("sql execution OK, but no rows matched the selection criteria.",  response.getFailMsg());		
		assertEquals(String.valueOf(true), response.getSuccess());		
		
		assertEquals(new Integer(3), dhApiClient.countPolicies("testapi", "",  "USED").getRowsAffected() ); 
		
		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", "", "USED");
		assertEquals(2, response.getCountPoliciesBreakdown().size());		
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=, useability=USED, selectOrder=null], rowCount=2]", 				response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		response.getCountPoliciesBreakdown().get(1).toString());

		assertEquals(new Integer(0), dhApiClient.countPolicies("testapi", "nonexistingc",  "").getRowsAffected() ); 
		assertEquals(0, dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", "nonexistingc",  "").getCountPoliciesBreakdown().size()); 

		assertEquals(new Integer(1), dhApiClient.countPolicies("testapi", null,  "UNUSED").getRowsAffected() ); 

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", null,  "UNUSED");
		assertEquals(1, response.getCountPoliciesBreakdown().size());		
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=UNUSED, selectOrder=null], rowCount=1]", 	response.getCountPoliciesBreakdown().get(0).toString());
		
		assertEquals(new Integer(2), dhApiClient.countPolicies("testapi", "nonblanklc", "").getRowsAffected() );

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.EQUALS, "testapi", "nonblanklc", "");
		assertEquals(2, response.getCountPoliciesBreakdown().size());		
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=UNUSED, selectOrder=null], rowCount=1]", 	response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		response.getCountPoliciesBreakdown().get(1).toString());
	}	
	
	
	/**
	 * Note: this method clears the DataHunter database of all existing data
	 * @param dhApiClient
	 */
	public void policyCountBreakdownsUsingStartWith(DataHunterRestApiClient dhApiClient){

		DataHunterRestApiResponsePojo response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "", null, null);		
		List<CountPoliciesBreakdown> countPoliciesBreakdownList = response.getCountPoliciesBreakdown();
		for (CountPoliciesBreakdown cpb : countPoliciesBreakdownList) {
			dhApiClient.deleteMultiplePolicies(cpb.getApplication(), cpb.getLifecycle(), cpb.getUseability()); 
		}
		assertEquals(0, dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "", null, null).getCountPoliciesBreakdown().size()); 

		create6testPolices(dhApiClient);
		assertEquals(5, dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "", null, null).getCountPoliciesBreakdown().size()); 
		
		dhApiClient.addPolicy(new Policies("test api","ex1", "", "UNUSED", "", null)); 			
		dhApiClient.addPolicy(new Policies("testaB_pi","ex2", "nonblanklc", "USED", "", null));
		dhApiClient.addPolicy(new Policies("testaC%pi:&? @=+","ex3", "lc with$char-s", "USED", "", null)); 
		dhApiClient.addPolicy(new Policies("testaC%pi:&? @=+","ex4", "lc with$char-s", "USED", "", null));
		
		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "", null, null);		
		assertEquals(8, response.getCountPoliciesBreakdown().size()); 
		assertEquals("[application=otherapp, startsWith=null, identifier=null, lifecycle=, useability=UNUSED, selectOrder=null], rowCount=1]", 					 response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=test api, startsWith=null, identifier=null, lifecycle=, useability=UNUSED, selectOrder=null], rowCount=1]", 					 response.getCountPoliciesBreakdown().get(1).toString());
		assertEquals("[application=testaB_pi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		 response.getCountPoliciesBreakdown().get(2).toString());
		assertEquals("[application=testaC%pi:&? @=+, startsWith=null, identifier=null, lifecycle=lc with$char-s, useability=USED, selectOrder=null], rowCount=2]",response.getCountPoliciesBreakdown().get(3).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=, useability=USED, selectOrder=null], rowCount=2]", 					 response.getCountPoliciesBreakdown().get(4).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=duplicatedid, useability=REUSABLE, selectOrder=null], rowCount=1]", 	 response.getCountPoliciesBreakdown().get(5).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=UNUSED, selectOrder=null], rowCount=1]", 		 response.getCountPoliciesBreakdown().get(6).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 			 response.getCountPoliciesBreakdown().get(7).toString());

		assertEquals(7, dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "test", null, null).getCountPoliciesBreakdown().size()); 
		assertEquals(4, dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "testapi", null, null).getCountPoliciesBreakdown().size()); 
		assertEquals(1, dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "test ", null, null).getCountPoliciesBreakdown().size()); 

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "test", "lc with$char-s", null);		
		assertEquals(1, response.getCountPoliciesBreakdown().size()); 
		assertEquals("[application=testaC%pi:&? @=+, startsWith=null, identifier=null, lifecycle=lc with$char-s, useability=USED, selectOrder=null], rowCount=2]",response.getCountPoliciesBreakdown().get(0).toString());

		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "", null, "USED");		
		assertEquals(4, response.getCountPoliciesBreakdown().size()); 
		assertEquals("[application=testaB_pi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		 response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=testaC%pi:&? @=+, startsWith=null, identifier=null, lifecycle=lc with$char-s, useability=USED, selectOrder=null], rowCount=2]",response.getCountPoliciesBreakdown().get(1).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=, useability=USED, selectOrder=null], rowCount=2]", 					 response.getCountPoliciesBreakdown().get(2).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 			 response.getCountPoliciesBreakdown().get(3).toString());
		
		response = dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "testa", "nonblanklc", "USED");		
		assertEquals(2, response.getCountPoliciesBreakdown().size()); 
		assertEquals("[application=testaB_pi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 		 response.getCountPoliciesBreakdown().get(0).toString());
		assertEquals("[application=testapi, startsWith=null, identifier=null, lifecycle=nonblanklc, useability=USED, selectOrder=null], rowCount=1]", 			 response.getCountPoliciesBreakdown().get(1).toString());
		
		//clean up
		assertEquals(new Integer(1), dhApiClient.deleteMultiplePolicies("otherapp", null, null).getRowsAffected()); 
		assertEquals(new Integer(1), dhApiClient.deleteMultiplePolicies("test api", null, null).getRowsAffected()); 
		assertEquals(new Integer(1), dhApiClient.deleteMultiplePolicies("testaB_pi", null, null).getRowsAffected()); 
		assertEquals(new Integer(2), dhApiClient.deleteMultiplePolicies("testaC%pi:&? @=+", null, null).getRowsAffected()); 
		assertEquals(new Integer(5), dhApiClient.deleteMultiplePolicies("testapi", null, null).getRowsAffected()); 
		assertEquals(0, dhApiClient.countPoliciesBreakdown(DataHunterConstants.STARTS_WITH, "", null, null).getCountPoliciesBreakdown().size()); 
	}	
	
	

	public void workingWithUseStateChanges(DataHunterRestApiClient dhApiClient){
		create6testPolices(dhApiClient);
		DataHunterRestApiResponsePojo response = dhApiClient.updatePoliciesUseState("testapi", null, "USED", "UNUSED", null);
		assertEquals(new Integer(3), response.getRowsAffected());

		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_MOST_RECENTLY_ADDED);
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","im4", "nonblanklc", "UNUSED", "", null), response.getPolicies().get(0));		

		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_MOST_RECENTLY_ADDED);
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","im3", "nonblanklc", "UNUSED", "otherdata3", null), response.getPolicies().get(0));
	
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_MOST_RECENTLY_ADDED);
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","im2", "", "UNUSED", "", null), response.getPolicies().get(0));

		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_MOST_RECENTLY_ADDED);
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(1), response.getRowsAffected()); 		
		assertsOnPolicy(new Policies("testapi","im1", "", "UNUSED", "", null), response.getPolicies().get(0));
		
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_MOST_RECENTLY_ADDED);
		assertEquals(String.valueOf(false), response.getSuccess()); 
		assertEquals(new Integer(0), response.getRowsAffected()); 	
		assertEquals("No rows matching the selection.  Possibly we have ran out of data for application:[testapi]", response.getFailMsg()); 	
		 
		
		response = dhApiClient.useNextPolicy("testapi", null, "REUSABLE", DataHunterConstants.SELECT_MOST_RECENTLY_ADDED);
		assertEquals(String.valueOf(true), response.getSuccess()); 
		assertEquals(new Integer(0), response.getRowsAffected());
		assertsOnPolicy(new Policies("testapi","im3", "duplicatedid", "REUSABLE", "duplicated id", null), response.getPolicies().get(0));		
		assertEquals("Policy im3 NOT updated as it is marked as REUSABLE", response.getFailMsg());		
		
		
		create6testPolices(dhApiClient);
		response = dhApiClient.updatePoliciesUseState("testapi", null, "", "UNUSED", null);
		assertEquals(new Integer(5), response.getRowsAffected());
		response = dhApiClient.lookupNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im1", "", "UNUSED", "", null), response.getPolicies().get(0));		
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im1", "", "UNUSED", "", null), response.getPolicies().get(0));		
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im2", "", "UNUSED", "", null), response.getPolicies().get(0));	
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);		
		assertsOnPolicy(new Policies("testapi","im3", "nonblanklc", "UNUSED", "otherdata3", null), response.getPolicies().get(0));	
		response = dhApiClient.lookupNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im3", "duplicatedid", "UNUSED", "duplicated id", null), response.getPolicies().get(0));	
		response = dhApiClient.lookupNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im3", "duplicatedid", "UNUSED", "duplicated id", null), response.getPolicies().get(0));	
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im3", "duplicatedid", "UNUSED", "duplicated id", null), response.getPolicies().get(0));			
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im4", "nonblanklc", "UNUSED", "", null), response.getPolicies().get(0));
		response = dhApiClient.lookupNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertEquals("No rows matching the selection.  Possibly we have ran out of data for application:[testapi]", response.getFailMsg()); 		
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertEquals("No rows matching the selection.  Possibly we have ran out of data for application:[testapi]", response.getFailMsg()); 			
		
		
		response = dhApiClient.updatePoliciesUseState("testapi", "im3", "USED", "UNUSED", null);
		assertEquals(new Integer(2), response.getRowsAffected());
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);		
		assertsOnPolicy(new Policies("testapi","im3", "nonblanklc", "UNUSED", "otherdata3", null), response.getPolicies().get(0));		
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im3", "duplicatedid", "UNUSED", "duplicated id", null), response.getPolicies().get(0));			
		response = dhApiClient.useNextPolicy("testapi", null, "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertEquals("No rows matching the selection.  Possibly we have ran out of data for application:[testapi]", response.getFailMsg()); 		
	
		
		assertEquals(new Integer(2), dhApiClient.updatePoliciesUseState("testapi", "im3", "USED", "UNUSED", null).getRowsAffected()); 
		assertEquals(new Integer(1), dhApiClient.updatePoliciesUseState("testapi", "im4", "USED", "UNUSED", null).getRowsAffected()); 
		response = dhApiClient.useNextPolicy("testapi", "nonblanklc", "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);		
		assertsOnPolicy(new Policies("testapi","im3", "nonblanklc", "UNUSED", "otherdata3", null), response.getPolicies().get(0));	
		response = dhApiClient.useNextPolicy("testapi", "nonblanklc", "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertsOnPolicy(new Policies("testapi","im4", "nonblanklc", "UNUSED", "", null), response.getPolicies().get(0));	
		response = dhApiClient.useNextPolicy("testapi", "nonblanklc", "UNUSED", DataHunterConstants.SELECT_OLDEST_ENTRY);
		assertEquals("No rows matching the selection.  Possibly we have ran out of data for application:[testapi]", response.getFailMsg()); 		
		
		 System.out.println( "######## next : " + response);
		
		// assertEquals(new Integer(5), dhApiClient.updatePoliciesUseState("testapi", null, "", "UNPAIRED", null).getRowsAffected());
	}	
	
	
	public void workingWithAsyncMessagesX(DataHunterRestApiClient dhApiClient) {
		
		DataHunterRestApiResponsePojo response = dhApiClient.deleteMultiplePolicies("TESTAPI_ASYNC_HIGH_VOL", null, null);
		assertEquals(String.valueOf(true), response.getSuccess() ); 

		massPolicyInsert(dhApiClient, 5000);
		
		response = dhApiClient.asyncMessageAnalyzer(DataHunterConstants.STARTS_WITH,"TESTAPI_ASYNC_HIGH_VOL", null, "UNPAIRED", "USED");
		
		System.out.println(">> $$$ " );
		
		int i=0;
		List<AsyncMessageaAnalyzerResult>  res = response.getAsyncMessageaAnalyzerResults();
		for (AsyncMessageaAnalyzerResult asyncMessageaAnalyzerResult : res) {
			System.out.println("       " +  ++i + "   " + asyncMessageaAnalyzerResult  );
		}
		
		System.out.println("<< $$$ " );		

	}


	private void massPolicyInsert(DataHunterRestApiClient dhApiClient, int numPoliciesSetsToBeCreate) {
		System.out.println("massPolicyInsert inserting ...");
		for (int i = 1; i <= numPoliciesSetsToBeCreate; i++) {
			dhApiClient.addPolicy( new Policies("testapi", "T99-testonly-" + i, "FIRSTONE", "UNPAIRED", "", null));
			dhApiClient.addPolicy( new Policies("testapi", "T99-testonly-" + i, "between", "UNPAIRED", "", null));
			dhApiClient.addPolicy( new Policies("testapi", "T99-testother-" + i,  "other",  "UNUNUSED", "", null));			
			dhApiClient.addPolicy( new Policies("testapi", "T99-testanohter-" + i, "other",  "UNUNUSED", "", null));
		} 
		for (int i = 1; i <= numPoliciesSetsToBeCreate; i++) {
			dhApiClient.addPolicy( new Policies("testapi", "T99-testonly-" + i, "LASTONE",  "UNPAIRED", "", null));		
		} 		
		System.out.println("... massPolicyInsert done");
	}
	
	
	private void create6testPolices(DataHunterRestApiClient dhApiClient) {
		dhApiClient.deleteMultiplePolicies("testapi", null, null);
		dhApiClient.deleteMultiplePolicies("otherapp", null, null);
		dhApiClient.addPolicy(new Policies("testapi","im1", "", "USED", "", null));	
		try {Thread.sleep(2);} catch (Exception e){};   // guarantee no two rows have matching 'created' (for sorts)   
		dhApiClient.addPolicy(new Policies("testapi","im2", "", "USED", null, null));
		try {Thread.sleep(2);} catch (Exception e){}; 
		dhApiClient.addPolicy(new Policies("testapi","im3", "nonblanklc", "USED", "otherdata3", null));	
		try {Thread.sleep(2);} catch (Exception e){}; 
		dhApiClient.addPolicy(new Policies("testapi","im3", "duplicatedid", "REUSABLE", "duplicated id", null));
		try {Thread.sleep(2);} catch (Exception e){}; 
		dhApiClient.addPolicy(new Policies("testapi","im4", "nonblanklc", "UNUSED", "", null));	
		try {Thread.sleep(2);} catch (Exception e){}; 
		dhApiClient.addPolicy(new Policies("otherapp","io1", "", "UNUSED", null, null));
	}	
	
	
	private void assertsOnPolicy(Policies expectedPolicy, Policies actualPolicy) {
		assertEquals(expectedPolicy.getApplication(), actualPolicy.getApplication()); 
		assertEquals(expectedPolicy.getIdentifier(), actualPolicy.getIdentifier()); 
		assertEquals(expectedPolicy.getLifecycle(), actualPolicy.getLifecycle()); 
		assertEquals(expectedPolicy.getOtherdata(), actualPolicy.getOtherdata());
		if (expectedPolicy.getEpochtime() != null) {
			assertEquals(expectedPolicy.getOtherdata(), actualPolicy.getOtherdata()); 
		}
	}

	
	public static void main(String[] args) {
		
		DataHunterRestApiClient dhApiClient = new DataHunterRestApiClient("http://localhost:8081/dataHunter"  );
		DataHunterRestApiClientSampleUsage sample = new DataHunterRestApiClientSampleUsage();
		sample.basicPolicyAddPrintDeleteChecks(dhApiClient);
		sample.workingWithMultiplePolicies(dhApiClient);
		sample.policyCountsAndBreakdowns(dhApiClient);
		sample.policyCountBreakdownsUsingStartWith(dhApiClient);  // this method clears the database !!
		sample.workingWithUseStateChanges(dhApiClient);
//		sample.workingWithAsyncMessagesX(dhApiClient);

		sample.asyncLifeCycleTestWithUseabilityUpdate(dhApiClient);	
	}

}

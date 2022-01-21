package com.mark59.dataHunterRestApiDSL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
// import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// import com.fasterxml.jackson.databind.ObjectMapper;


public class DataHunterRestApiInvoker {
	
	private static final Logger LOG = LogManager.getLogger(DataHunterRestApiInvoker.class);		
	String dataHunterUrl;
	
	public DataHunterRestApiInvoker(String dataHunterUrl) {
		this.dataHunterUrl = dataHunterUrl;
	}
	

	public DataHunterRestApiResponsePojo printPolicy(String application, String identifier, String lifecycle){
		
		String webServiceUrl = dataHunterUrl + "/api/printPolicy?application=" + application + "&identifier=" + identifier;
		if (StringUtils.isNotBlank(lifecycle)) {
			webServiceUrl = webServiceUrl + "&lifecycle=" + lifecycle;
		}
		
		DataHunterRestApiResponsePojo response = invokeDataHunterRestApi(webServiceUrl);

//		List<Policies> policiesList = response.getPolicies();
		
//		if (policiesList.size() == 1 ){
//			modelMap.addAttribute("sqlResult", "PASS");			
//			modelMap.addAttribute("sqlResultText", "sql execution OK");
//			modelMap.addAttribute("policies", policiesList.get(0));
//			return new ModelAndView("/print_policy_action", "modelMap", modelMap);
//			
//		} else if (policiesList.size() == 0){
//			modelMap.addAttribute("sqlResult", "FAIL");			
//			modelMap.addAttribute("sqlResultText", "No rows matching the selection.");
//			return new ModelAndView("/print_policy_action_error", "modelMap", modelMap);
//			
//		} else {
//			modelMap.addAttribute("sqlResult", "FAIL");		
//			modelMap.addAttribute("sqlResultText", "sql execution : Error.  1 row should of been affected, but sql result indicates " + policiesList.size() + " rows affected?" );
//			return new ModelAndView("/print_policy_action_error", "modelMap", modelMap);			
//		}

		
		return response;
	}


	private DataHunterRestApiResponsePojo invokeDataHunterRestApi(String webServiceUrl)  {

		BufferedReader in = null;
		DataHunterRestApiResponsePojo response = new DataHunterRestApiResponsePojo();
		Integer repsonseCode = null;
		try {
			URL url = new URL(webServiceUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			repsonseCode = con.getResponseCode();

			in = new BufferedReader( new InputStreamReader(con.getInputStream()));
			String respLine;
			StringBuilder jsonResponseStr = new StringBuilder();
			while ((respLine = in.readLine()) != null) {
				jsonResponseStr.append(respLine);
			}
			in.close();
//			response = new ObjectMapper().readValue(jsonResponseStr.toString(), DataHunterRestApiResponsePojo.class);
			response = null;
			
		} catch (Exception | AssertionError e) {
			StringWriter stackTrace = new StringWriter();
			e.printStackTrace(new PrintWriter(stackTrace));
			String errorMsg = "Error: Failure calling the DataHunter Rest API at " + webServiceUrl + " message : \n"+e.getMessage()+"\n"+stackTrace.toString();
			LOG.error(errorMsg);
			System.out.println(errorMsg);
			LOG.debug("        last response-code from DataHunter Rest API was " + repsonseCode);			
			LOG.debug("        last response from DataHunter Rest API was \n" + response );
			if (in != null){try {in.close();} catch (IOException ignored) {}}
		}
		
		
		return response;
	}
	
	
	

}

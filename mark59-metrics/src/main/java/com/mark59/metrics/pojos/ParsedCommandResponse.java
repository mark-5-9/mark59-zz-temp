package com.mark59.metrics.pojos;

import java.util.List;

public class ParsedCommandResponse {
	
	private String commandName;
	private String parserName;
	private String commandResponse;
	private List<ParsedMetric> parsedMetrics;

	public ParsedCommandResponse() {
	}
	
	public String getCommandName() {
		return commandName;
	}
	
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	public String getParserName() {
		return parserName;
	}
	
	public void setParserName(String parserName) {
		this.parserName = parserName;
	}
	
	public String getCommandResponse() {
		return commandResponse;
	}

	public void setCommandResponse(String commandResponse) {
		this.commandResponse = commandResponse;
	}

	public List<ParsedMetric> getParsedMetrics() {
		return parsedMetrics;
	}

	public void setParsedMetrics(List<ParsedMetric> parsedMetrics) {
		this.parsedMetrics = parsedMetrics;
	}

	@Override
    public String toString() {
        return   "[commandName" + commandName
         	   + ", parserName="+ parserName   
         	   + ", commandResponse="+ commandResponse
         	   + ", parsedMetrics="+ parsedMetrics  
        	   + "]";
	}

}

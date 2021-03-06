-- in pgadmin run from 'postgres' db 
-- >  comment/uncomment as required
 
CREATE USER admin SUPERUSER PASSWORD 'admin';

-- DROP DATABASE mark59metricsdb;
CREATE DATABASE mark59metricsdb WITH ENCODING='UTF8' OWNER=admin TEMPLATE=template0 LC_COLLATE='C' LC_CTYPE='C';
 
-- in pgadmin run from 'mark59metricsdb' db query  panel 

DROP TABLE IF EXISTS  SERVERPROFILES;
DROP TABLE IF EXISTS  COMMANDS;
DROP TABLE IF EXISTS  SERVERCOMMANDLINKS;
DROP TABLE IF EXISTS  COMMANDRESPONSEPARSERS;
DROP TABLE IF EXISTS  COMMANDPARSERLINKS;

-- <
--   The utf8/C ecoding/collation is more in line with other mark59 database options (and how Java/JS sorts work). 
--   if you use the pgAdmin tool to load data, remember to hit the 'commit' icon to save the changes! 


CREATE TABLE IF NOT EXISTS SERVERPROFILES  (
   SERVER_PROFILE_NAME  varchar(64)   NOT NULL,
   EXECUTOR             varchar(32)   NOT NULL,
   SERVER               varchar(64)   DEFAULT '',
   ALTERNATE_SERVER_ID  varchar(64)   DEFAULT '',
   USERNAME             varchar(64)   DEFAULT '',
   PASSWORD             varchar(64)   DEFAULT '',
   PASSWORD_CIPHER      varchar(64)   DEFAULT '',
   CONNECTION_PORT      varchar(8)    DEFAULT '',
   CONNECTION_TIMEOUT   varchar(8)    DEFAULT '',
   COMMENT              varchar(128)  DEFAULT NULL,
   PARAMETERS           varchar(2000) DEFAULT NULL,
  PRIMARY KEY ( SERVER_PROFILE_NAME )
); 


CREATE TABLE IF NOT EXISTS COMMANDS  (
   COMMAND_NAME   varchar(64)   NOT NULL,
   EXECUTOR       varchar(32)   NOT NULL,
   COMMAND        varchar(8192) NOT NULL,
   IGNORE_STDERR  varchar(1)    DEFAULT NULL,
   COMMENT        varchar(128)  DEFAULT NULL,
   PARAM_NAMES    varchar(1000) DEFAULT NULL,   
  PRIMARY KEY ( COMMAND_NAME )
); 


CREATE TABLE IF NOT EXISTS SERVERCOMMANDLINKS  (
   SERVER_PROFILE_NAME  varchar(64) NOT NULL,
   COMMAND_NAME  varchar(64) NOT NULL,
  PRIMARY KEY ( SERVER_PROFILE_NAME , COMMAND_NAME )
);


CREATE TABLE IF NOT EXISTS COMMANDRESPONSEPARSERS  (
   PARSER_NAME  varchar(64) NOT NULL,
   METRIC_TXN_TYPE  varchar(64) NOT NULL,
   METRIC_NAME_SUFFIX  varchar(64) NOT NULL,
   SCRIPT  varchar(4096) NOT NULL,
   COMMENT  varchar(1024) NOT NULL,
   SAMPLE_COMMAND_RESPONSE  varchar(1024) NOT NULL,
  PRIMARY KEY ( PARSER_NAME )
); 


CREATE TABLE IF NOT EXISTS COMMANDPARSERLINKS  (
   COMMAND_NAME  varchar(64) NOT NULL,
   PARSER_NAME  varchar(64) NOT NULL,
  PRIMARY KEY ( COMMAND_NAME , PARSER_NAME )
); 


INSERT INTO SERVERPROFILES VALUES ('DemoLINUX-DataHunterSeleniumDeployAndExecute','SSH_LINIX_UNIX','localhost','','','','','22','60000','','');
INSERT INTO SERVERPROFILES VALUES ('DemoLINUX-DataHunterSeleniumGenJmeterReport','SSH_LINIX_UNIX','localhost','','','','','22','60000','Reports generated at   ~/Mark59_Runs/Jmeter_Reports/DataHunter/   <br>(open each index.html)   ','');
INSERT INTO SERVERPROFILES VALUES ('DemoLINUX-DataHunterSeleniumTrendsLoad','SSH_LINIX_UNIX','localhost','','','','','22','60000','Loads Trend Analysis (PG database).  See:<br>http://localhost:8083/mark59-trends/trending?reqApp=DataHunter','');
INSERT INTO SERVERPROFILES VALUES ('DemoWIN-DataHunterSeleniumDeployAndExecute','WMIC_WINDOWS','localhost','','','','','','','','');
INSERT INTO SERVERPROFILES VALUES ('DemoWIN-DataHunterSeleniumGenJmeterReport','WMIC_WINDOWS','localhost','','','','','','','Hint - in browser open this URL and go to each index.html:  file:///C:/Mark59_Runs/Jmeter_Reports/DataHunter/','');
INSERT INTO SERVERPROFILES VALUES ('DemoWIN-DataHunterSeleniumTrendsLoad','WMIC_WINDOWS','localhost','','','','','','','Loads Trend Analysis (PG database).  See:<br>http://localhost:8083/mark59-trends/trending?reqApp=DataHunter','');
INSERT INTO SERVERPROFILES VALUES ('localhost_LINUX','SSH_LINIX_UNIX','localhost','','','','','22','60000','','');
INSERT INTO SERVERPROFILES VALUES ('localhost_WINDOWS','WMIC_WINDOWS','localhost','','','','','','','','');
INSERT INTO SERVERPROFILES VALUES ('localhost_WINDOWS_HOSTID','WMIC_WINDOWS','localhost','HOSTID','','','','','','HOSTID will be subed <br> with computername  ','');
INSERT INTO SERVERPROFILES VALUES ('remoteLinuxServer','SSH_LINIX_UNIX','LinuxServerName','','userid','encryptMe','','22','60000','','');
INSERT INTO SERVERPROFILES VALUES ('remoteUnixVM','SSH_LINIX_UNIX','UnixVMName','','userid','encryptMe','','22','60000','','');
INSERT INTO SERVERPROFILES VALUES ('remoteWinServer','WMIC_WINDOWS','WinServerName','','userid','encryptMe','','','','','');
INSERT INTO SERVERPROFILES VALUES ('SimpleScriptSampleRunner', 'GROOVY_SCRIPT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'runs a supplied, basic groovy script sample', '{"parm1":"11","parm2":"55.7","parm3":"333"}');
INSERT INTO SERVERPROFILES VALUES ('NewRelicTestProfile', 'GROOVY_SCRIPT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'supplied sample New Relic API groovy script', '{"proxyPort":"","newRelicXapiKey":"","proxyServer":"","newRelicApiAppId":""}');    


INSERT INTO COMMANDS VALUES ('DataHunterSeleniumDeployAndExecute','WMIC_WINDOWS','process call create ''cmd.exe /c 
 echo Running Directly From Server Metrics Web (cmd DataHunterSeleniumDeployAndExecute) & 
 echo  METRICS_BASE_DIR: %METRICS_BASE_DIR% & 
 cd /D %METRICS_BASE_DIR% &  
 cd ..\mark59-datahunter-samples & 
 DEL C:\apache-jmeter\bin\mark59.properties & COPY .\mark59.properties C:\apache-jmeter\bin &
 DEL C:\apache-jmeter\bin\chromedriver.exe  & COPY .\chromedriver.exe  C:\apache-jmeter\bin &
 DEL C:\apache-jmeter\lib\ext\mark59-metrics-api.jar &
 COPY ..\mark59-metrics-api\target\mark59-metrics-api.jar  C:\apache-jmeter\lib\ext & 
 DEL C:\apache-jmeter\lib\ext\mark59-datahunter-samples.jar & 
 COPY .\target\mark59-datahunter-samples.jar  C:\apache-jmeter\lib\ext &
 RMDIR /S /Q C:\apache-jmeter\lib\ext\mark59-datahunter-samples-dependencies &
 MKDIR C:\apache-jmeter\lib\ext\mark59-datahunter-samples-dependencies &
 COPY .\target\mark59-datahunter-samples-dependencies  C:\apache-jmeter\lib\ext\mark59-datahunter-samples-dependencies &

 mkdir C:\Mark59_Runs &
 mkdir C:\Mark59_Runs\Jmeter_Results &
 mkdir C:\Mark59_Runs\Jmeter_Results\DataHunter &

 set path=%path%;C:\Windows\System32;C:\windows\system32\wbem & 
 cd /D C:\apache-jmeter\bin &

 echo Starting JMeter DataHunter test ... &  

 jmeter -n -X -f -t %METRICS_BASE_DIR%\..\mark59-datahunter-samples\test-plans\DataHunterSeleniumTestPlan.jmx -l C:\Mark59_Runs\Jmeter_Results\DataHunter\DataHunterTestResults.csv -JForceTxnFailPercent=0 -JDataHunterUrl=http://localhost:8081/mark59-datahunter -JStartCdpListeners=false &
 PAUSE
''
','N','refer DeployDataHunterTestArtifactsToJmeter.bat and DataHunterExecuteJmeterTest.bat in mark59-datahunter-samples ','');
INSERT INTO COMMANDS VALUES ('DataHunterSeleniumDeployAndExecute_LINUX','SSH_LINIX_UNIX','echo This script runs the JMeter deploy in the background, then opens a terminal for JMeter execution.
echo starting from $PWD;

{   # try  

    cd ../mark59-datahunter-samples && 
    DH_TEST_SAMPLES_DIR=$(pwd) && 
    echo mark59-datahunter-samples base dir is $DH_TEST_SAMPLES_DIR &&

    cp ./mark59.properties ~/apache-jmeter/bin/mark59.properties &&
    cp ./chromedriver ~/apache-jmeter/bin/chromedriver && 
    cp ../mark59-metrics-api/target/mark59-metrics-api.jar  ~/apache-jmeter/lib/ext/mark59-metrics-api.jar && 
    cp ./target/mark59-datahunter-samples.jar  ~/apache-jmeter/lib/ext/mark59-datahunter-samples.jar && 
    mkdir -p ~/Mark59_Runs/Jmeter_Results/DataHunter &&
    rm -rf ~/apache-jmeter/lib/ext/mark59-datahunter-samples-dependencies &&
    cp -r ./target/mark59-datahunter-samples-dependencies ~/apache-jmeter/lib/ext/mark59-datahunter-samples-dependencies &&
 
    gnome-terminal -- sh -c "~/apache-jmeter/bin/jmeter -n -X -f -t $DH_TEST_SAMPLES_DIR/test-plans/DataHunterSeleniumTestPlan.jmx -l ~/Mark59_Runs/Jmeter_Results/DataHunter/DataHunterTestResults.csv -JForceTxnFailPercent=0 -JStartCdpListeners=false; exec bash"

} || { # catch 
    echo Deploy was unsuccessful! 
}','Y','refer bin/TestRunLINUX-DataHunter-Selenium-DeployAndExecute.sh','');
INSERT INTO COMMANDS VALUES ('DataHunterSeleniumGenJmeterReport','WMIC_WINDOWS','process call create ''cmd.exe /c 
 cd /D %METRICS_BASE_DIR% & 
 cd../mark59-results-splitter & 
 CreateDataHunterJmeterReports.bat''
','N','','');
INSERT INTO COMMANDS VALUES ('DataHunterSeleniumGenJmeterReport_LINUX','SSH_LINIX_UNIX','echo This script creates a set of JMeter reports from a DataHunter test run.
echo starting from $PWD;

{   # try  

    cd ../mark59-results-splitter
    gnome-terminal -- sh -c "./CreateDataHunterJmeterReports.sh; exec bash"

} || { # catch 
    echo attempt to generate JMeter Reports has failed! 
}
','Y','refer bin/TestRunLINUX-DataHunter-Selenium-GenJmeterReport.sh','');
INSERT INTO COMMANDS VALUES ('DataHunterSeleniumTrendsLoad','WMIC_WINDOWS','process call create ''cmd.exe /c 
 echo Load DataHunter Test Results into Mark59 Trends Analysis PG database. & 
 cd /D  %METRICS_BASE_DIR% & 
 cd ../mark59-trends-load &  
 
 java -jar ./target/mark59-trends-load.jar -a DataHunter -i C:\Mark59_Runs\Jmeter_Results\DataHunter -d pg &
 PAUSE
''
','N','','');
INSERT INTO COMMANDS VALUES ('DataHunterSeleniumTrendsLoad_LINUX','SSH_LINIX_UNIX','echo This script runs mark59-trends-load,to load results from a DataHunter test run into the Metrics Trend Analysis Graph.
echo starting from $PWD;

{   # try  

    cd ../mark59-trends-load/target &&
    gnome-terminal -- sh -c "java -jar mark59-trends-load.jar -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter -d pg; exec bash"

} || { # catch 
    echo attempt to execute mark59-trends-load has failed! 
}
','Y','refer bin/TestRunLINUX-DataHunter-Selenium-metricsTrendsLoad.sh','');
INSERT INTO COMMANDS VALUES ('FreePhysicalMemory','WMIC_WINDOWS','OS get FreePhysicalMemory','N','','');
INSERT INTO COMMANDS VALUES ('FreeVirtualMemory','WMIC_WINDOWS','OS get FreeVirtualMemory','N','','');
INSERT INTO COMMANDS VALUES ('LINUX_free_m_1_1','SSH_LINIX_UNIX','free -m 1 1','N','linux memory','');
INSERT INTO COMMANDS VALUES ('LINUX_mpstat_1_1','SSH_LINIX_UNIX','mpstat 1 1','N','','');
INSERT INTO COMMANDS VALUES ('UNIX_lparstat_5_1','SSH_LINIX_UNIX','lparstat 5 1','N','','');
INSERT INTO COMMANDS VALUES ('UNIX_Memory_Script','SSH_LINIX_UNIX','vmstat=$(vmstat -v); 
let total_pages=$(print "$vmstat" | grep ''memory pages'' | awk ''{print $1}''); 
let pinned_pages=$(print "$vmstat" | grep ''pinned pages'' | awk ''{print $1}''); 
let pinned_percent=$(( $(print "scale=4; $pinned_pages / $total_pages " | bc) * 100 )); 
let numperm_pages=$(print "$vmstat" | grep ''file pages'' | awk ''{print $1}''); 
let numperm_percent=$(print "$vmstat" | grep ''numperm percentage'' | awk ''{print $1}''); 
pgsp_utils=$(lsps -a | tail +2 | awk ''{print $5}''); 
let pgsp_num=$(print "$pgsp_utils" | wc -l | tr -d '' ''); 
let pgsp_util_sum=0; 
for pgsp_util in $pgsp_utils; do let pgsp_util_sum=$(( $pgsp_util_sum + $pgsp_util )); done; 
pgsp_aggregate_util=$(( $pgsp_util_sum / $pgsp_num )); 
print "${pinned_percent},${numperm_percent},${pgsp_aggregate_util}"','N','','');

INSERT INTO COMMANDS VALUES ('UNIX_VM_Memory','SSH_LINIX_UNIX','vmstat=$(vmstat -v); 
let total_pages=$(print "$vmstat" | grep ''memory pages'' | awk ''{print $1}''); 
let pinned_pages=$(print "$vmstat" | grep ''pinned pages'' | awk ''{print $1}''); 
let pinned_percent=$(( $(print "scale=4; $pinned_pages / $total_pages " | bc) * 100 )); 
let numperm_pages=$(print "$vmstat" | grep ''file pages'' | awk ''{print $1}''); 
let numperm_percent=$(print "$vmstat" | grep ''numperm percentage'' | awk ''{print $1}''); 
pgsp_utils=$(lsps -a | tail +2 | awk ''{print $5}''); 
let pgsp_num=$(print "$pgsp_utils" | wc -l | tr -d '' ''); 
let pgsp_util_sum=0; 
for pgsp_util in $pgsp_utils; do let pgsp_util_sum=$(( $pgsp_util_sum + $pgsp_util )); done; 
pgsp_aggregate_util=$(( $pgsp_util_sum / $pgsp_num )); 
print "${pinned_percent},${numperm_percent},${pgsp_aggregate_util}"','N','','');

INSERT INTO COMMANDS VALUES ('WinCpuCmd','WMIC_WINDOWS','cpu get loadpercentage','N','','');

INSERT INTO COMMANDS VALUES ('SimpleScriptSampleCmd', 'GROOVY_SCRIPT', 
'import java.util.ArrayList;
import java.util.List;
import com.mark59.metrics.data.beans.ServerProfile;
import com.mark59.metrics.pojos.ParsedMetric;
import com.mark59.metrics.pojos.ScriptResponse;

ScriptResponse scriptResponse = new ScriptResponse();
List<ParsedMetric> parsedMetrics = new ArrayList<ParsedMetric>();

String commandLogDebug = "running script " + serverProfile.getServerProfileName() + "<br>" +  serverProfile.getComment();
commandLogDebug += "<br>passed parms : parm1=" + parm1 + ", parm2=" + parm2 + ", parm3=" + parm3

Number aNumber = 123;
parsedMetrics.add(new ParsedMetric("a_memory_txn", aNumber, "MEMORY"));
parsedMetrics.add(new ParsedMetric("a_cpu_util_txn", 33.3,  "CPU_UTIL"));
parsedMetrics.add(new ParsedMetric("some_datapoint", 66.6,  "DATAPOINT"));

scriptResponse.setCommandLog(commandLogDebug);
scriptResponse.setParsedMetrics(parsedMetrics);
return scriptResponse;', 'N', 'supplied basic groovy script sample', '["parm1","parm2","parm3"]');

INSERT INTO COMMANDS VALUES ('NewRelicSampleCmd', 'GROOVY_SCRIPT',
'import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mark59.metrics.pojos.ParsedMetric;
import com.mark59.metrics.pojos.ScriptResponse;

ScriptResponse scriptResponse = new ScriptResponse(); 
List<ParsedMetric> parsedMetrics = new ArrayList<ParsedMetric>();

String newRelicApiUrl = "https://api.newrelic.com/v2/applications/";

String url = newRelicApiUrl + newRelicApiAppId + "/instances.json"; 
String debugJsonResponses =  "running profile " + serverProfile.serverProfileName + ", init req : " + url ;
JSONObject jsonResponse = null;
Proxy proxy = StringUtils.isNotBlank(proxyServer + proxyPort) ? new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyServer , new Integer(proxyPort))) : null;

try {
	HttpURLConnection conn = proxy != null ? (HttpURLConnection)new URL(url).openConnection(proxy) : (HttpURLConnection)new URL(url).openConnection();
	conn.setRequestMethod("GET");
	conn.addRequestProperty("X-Api-Key", newRelicXapiKey);
	conn.setRequestProperty("Content-Type", "application/json");
	conn.setRequestProperty("Accept", "application/json");
	if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new Exception("rc:" + conn.getResponseCode() + "from " + url);
	}
	String isStr = null;
	Scanner s = new Scanner(conn.getInputStream());
	isStr = s.hasNext() ? s.next() : "";
	jsonResponse = new JSONObject(isStr);
	debugJsonResponses =  debugJsonResponses + "<br>init res.: " + jsonResponse.toString();

	ZonedDateTime utcTimeNow = ZonedDateTime.now(ZoneOffset.UTC);
	String toHour 	= String.format("%02d", utcTimeNow.getHour());
	String toMinute	= String.format("%02d", utcTimeNow.getMinute());
	ZonedDateTime utcMinus1Min = utcTimeNow.minusMinutes(1);
	String fromHour	= String.format("%02d", utcMinus1Min.getHour());
	String fromMinute = String.format("%02d", utcMinus1Min.getMinute());
	String fromDate = utcMinus1Min.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	String toDate 	= utcTimeNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));		
	String urlDateRangeParmStr = "&from=" + fromDate + "T" + fromHour + "%3A" + fromMinute + "%3A00%2B00%3A00" + "&to=" + toDate + "T" + toHour + "%3A" + toMinute + "%3A00%2B00%3A00";

	JSONArray application_instances = jsonResponse.getJSONArray("application_instances");

	for (int i = 0; i < application_instances.length(); i++) {
		JSONObject application_instance = (JSONObject) application_instances.get(i);
		Integer instanceId = (Integer) application_instance.get("id");
		String instanceName = ((String)application_instance.get("application_name")).replace(":","_");
		url = newRelicApiUrl + newRelicApiAppId  + "/instances/" + instanceId + "/metrics/data.json?names%5B%5D=Memory%2FHeap%2FFree&names%5B%5D=CPU%2FUser%2FUtilization" + urlDateRangeParmStr;
		debugJsonResponses =  debugJsonResponses + "<br><br>req." + i + ": " + url ; 
		
		conn = proxy != null ? (HttpURLConnection)new URL(url).openConnection(proxy) : (HttpURLConnection)new URL(url).openConnection();
		conn.setRequestMethod("GET");
		conn. addRequestProperty("X-Api-Key", newRelicXapiKey);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("rc:" + conn.getResponseCode() + "from " + url);
		}
		isStr = null;
		s = new Scanner(conn.getInputStream());
		isStr = s.hasNext() ? s.next() : "";
		jsonResponse = new JSONObject(isStr);
		debugJsonResponses =  debugJsonResponses + "<br>res." + i + ": " + jsonResponse.toString(); 
	
		Number totalUusedMbMemory = -1.0;
		totalUusedMbMemory =  (Number)((JSONObject)((JSONObject)jsonResponse.getJSONObject("metric_data").getJSONArray("metrics").get(0)).getJSONArray("timeslices").get(0)).getJSONObject("values").get("total_used_mb") ;
		parsedMetrics.add(new ParsedMetric("MEMORY_" + newRelicApiAppId + "_" + instanceId + "_" + instanceName, totalUusedMbMemory,  "MEMORY"));
		
		Number percentCpuUserUtilization = -1.0;
		percentCpuUserUtilization = (Number)((JSONObject)((JSONObject)jsonResponse.getJSONObject("metric_data").getJSONArray("metrics").get(1)).getJSONArray("timeslices").get(0)).getJSONObject("values").get("percent");
		parsedMetrics.add(new ParsedMetric("CPU_" + newRelicApiAppId + "_" + instanceId + "_" + instanceName, percentCpuUserUtilization, "CPU_UTIL"));

	}
} catch (Exception e) {
	debugJsonResponses =  debugJsonResponses + "<br>\n ERROR :  Exception last url: " + url + ", response of  : " + jsonResponse + ", message: "+ e.getMessage(); 
}
scriptResponse.setCommandLog(debugJsonResponses);
scriptResponse.setParsedMetrics(parsedMetrics);

return scriptResponse;', 'N', 'NewRelic Supplied Sample', '["newRelicApiAppId","newRelicXapiKey","proxyServer","proxyPort"]'); 



INSERT INTO COMMANDRESPONSEPARSERS VALUES ('LINUX_Memory_freeG','MEMORY','freeG','import org.apache.commons.lang3.StringUtils;
// ---
String targetColumnName= "free";              
String targetRowName= "Mem:";  
// ---
String extractedMetric = "-3";

if (StringUtils.isNotBlank(commandResponse)) {
    String wordsOnThisResultLine = commandResponse.replace("\\n", " ").replace("\\r", " ");
    ArrayList<String> cmdResultLine = new ArrayList<>(
         Arrays.asList(wordsOnThisResultLine.trim().split("\\s+")));

    if (cmdResultLine.contains(targetColumnName)) {
	extractedMetric = cmdResultLine
		.get(cmdResultLine.indexOf(targetRowName) + cmdResultLine.indexOf(targetColumnName) + 1);
    }
}
return Math.round(Double.parseDouble(extractedMetric) / 1000 );
','','              total        used        free      shared  buff/cache   available
Mem:          28798       14043         561        1412       14392       12953
Swap:             0           0           0
');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('LINUX_Memory_totalG','MEMORY','totalG','import org.apache.commons.lang3.StringUtils;
// ---
String targetColumnName= "total";              
String targetRowName= "Mem:";  
// ---
String extractedMetric = "-3";

if (StringUtils.isNotBlank(commandResponse)) {
    String wordsOnThisResultLine = commandResponse.replace("\\n", " ").replace("\\r", " ");
    ArrayList<String> cmdResultLine = new ArrayList<>(
         Arrays.asList(wordsOnThisResultLine.trim().split("\\s+")));

    if (cmdResultLine.contains(targetColumnName)) {
	extractedMetric = cmdResultLine
		.get(cmdResultLine.indexOf(targetRowName) + cmdResultLine.indexOf(targetColumnName) + 1);
    }
}
return Math.round(Double.parseDouble(extractedMetric) / 1000 );
','','              total        used        free      shared  buff/cache   available
Mem:          28798       14043         361        1412       14392       12953
Swap:             0           0           0
');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('LINUX_Memory_usedG','MEMORY','usedG','import org.apache.commons.lang3.StringUtils;
// ---
String targetColumnName= "used";              
String targetRowName= "Mem:";  
// ---
String extractedMetric = "-3";

if (StringUtils.isNotBlank(commandResponse)) {
    String wordsOnThisResultLine = commandResponse.replace("\\n", " ").replace("\\r", " ");
    ArrayList<String> cmdResultLine = new ArrayList<>(
         Arrays.asList(wordsOnThisResultLine.trim().split("\\s+")));

    if (cmdResultLine.contains(targetColumnName)) {
	extractedMetric = cmdResultLine
		.get(cmdResultLine.indexOf(targetRowName) + cmdResultLine.indexOf(targetColumnName) + 1);
    }
}
return Math.round(Double.parseDouble(extractedMetric) / 1000 );
','','              total        used        free      shared  buff/cache   available
Mem:          28798       14043         361        1412       14392       12953
Swap:             0           0           0
');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('Memory_FreePhysicalG','MEMORY','FreePhysicalG','Math.round(Double.parseDouble(commandResponse.replaceAll("[^\\d.]", "")) / 1000000 )','','FreePhysicalG
22510400');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('Memory_FreeVirtualG','MEMORY','FreeVirtualG','Math.round(Double.parseDouble(commandResponse.replaceAll("[^\\d.]", "")) / 1000000 )','','FreeVirtualMemory
22510400');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('Nix_CPU_Idle','CPU_UTIL','IDLE','import org.apache.commons.lang3.ArrayUtils;
// ---
String targetColumnName = "%idle"              
String targetmetricFormat = "\\d*\\.?\\d+"   // a decimal format  
// ---
String extractedMetric = "-1";
int colNumberOfTargetColumnName = -1;
String[] commandResultLine = commandResponse.trim().split("\\r\\n|\\n|\\r");

for (int i = 0; i < commandResultLine.length && "-1".equals(extractedMetric); i++) {

    String[] wordsOnThiscommandResultsLine = commandResultLine[i].trim().split("\\s+");

    if (colNumberOfTargetColumnName > -1
  	&& wordsOnThiscommandResultsLine[colNumberOfTargetColumnName].matches(targetmetricFormat)) {
	extractedMetric = wordsOnThiscommandResultsLine[colNumberOfTargetColumnName];
    }
    if (colNumberOfTargetColumnName == -1) { // column name not yet found, so see if it is on this line ...
	colNumberOfTargetColumnName = ArrayUtils.indexOf(wordsOnThiscommandResultsLine, targetColumnName);
    }
}
return extractedMetric;
','This works on data in a simple column format (eg unix lparstat and linux mpstat cpu). It will return the first matching value it finds in the column requested.','System configuration: type=Shared mode=Uncapped smt=4 lcpu=4 mem=47104MB psize=60 ent=0.50 

%user  %sys  %wait  %idle physc %entc  lbusy   app  vcsw phint  %nsp  %utcyc
----- ----- ------ ------ ----- ----- ------   --- ----- ----- -----  ------
 11.3  15.0    0.0   73.7  0.22  44.5    6.1 45.26   919     0   101   1.39 ');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('Nix_CPU_UTIL','CPU_UTIL','','import org.apache.commons.lang3.ArrayUtils;
import java.math.RoundingMode;
// 
String targetColumnName = "%idle";
String targetmetricFormat = "\\d*\\.?\\d+"; // a decimal format
// 
String notFound = "-1";
String extractedMetric = notFound;
int colNumberOfTargetColumnName = -1;
String[] commandResultLine = commandResponse.trim().split("\\r\\n|\\n|\\r");

for (int i = 0; i < commandResultLine.length && notFound.equals(extractedMetric); i++) {

	String[] wordsOnThiscommandResultsLine = commandResultLine[i].trim().split("\\s+");

	if (colNumberOfTargetColumnName > -1
			&& wordsOnThiscommandResultsLine[colNumberOfTargetColumnName].matches(targetmetricFormat)) {
		extractedMetric = wordsOnThiscommandResultsLine[colNumberOfTargetColumnName];
	}
	if (colNumberOfTargetColumnName == -1) { // column name not yet found, so see if it is on this line ...
		colNumberOfTargetColumnName = ArrayUtils.indexOf(wordsOnThiscommandResultsLine, targetColumnName);
	}
}

if (notFound.equals(extractedMetric))return notFound;
String cpuUtil = notFound;
try {  cpuUtil = new BigDecimal(100).subtract(new BigDecimal(extractedMetric)).setScale(1, RoundingMode.HALF_UP).toPlainString();} catch (Exception e) {}
return cpuUtil;','This works on data in a simple column format (eg unix lparstat and linux mpstat cpu). It will return the first matching value it finds in the column requested.','System configuration: type=Shared mode=Uncapped smt=4 lcpu=4 mem=47104MB psize=60 ent=0.50 

%user  %sys  %wait  %idle physc %entc  lbusy   app  vcsw phint  %nsp  %utcyc
----- ----- ------ ------ ----- ----- ------   --- ----- ----- -----  ------
 11.3  15.0    0.0   73.7  0.22  44.5    6.1 45.26   919     0   101   1.39 ');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('Return1','DATAPOINT','','return 1','','any rand junk');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('UNIX_Memory_numperm_percent','MEMORY','numperm_percent','commandResponse.split(",")[1].trim()','','1,35,4');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('UNIX_Memory_pgsp_aggregate_util','MEMORY','pgsp_aggregate_util','commandResponse.split(",")[2].trim()','','1,35,4');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('UNIX_Memory_pinned_percent','MEMORY','pinned_percent','commandResponse.split(",")[0].trim()','','1,35,4');
INSERT INTO COMMANDRESPONSEPARSERS VALUES ('WicnCpu','CPU_UTIL','','java.util.regex.Matcher m = java.util.regex.Pattern.compile("-?[0-9]+").matcher(commandResponse);
Integer sum = 0; 
int count = 0; 
while (m.find()){ 
    sum += Integer.parseInt(m.group()); 
    count++;
}; 
if (count==0) 
    return 0 ; 
else 
    return sum/count;','comment','LoadPercentage
21');


INSERT INTO SERVERCOMMANDLINKS VALUES ('DemoLINUX-DataHunterSeleniumDeployAndExecute','DataHunterSeleniumDeployAndExecute_LINUX');
INSERT INTO SERVERCOMMANDLINKS VALUES ('DemoLINUX-DataHunterSeleniumGenJmeterReport','DataHunterSeleniumGenJmeterReport_LINUX');
INSERT INTO SERVERCOMMANDLINKS VALUES ('DemoLINUX-DataHunterSeleniumTrendsLoad','DataHunterSeleniumTrendsLoad_LINUX');
INSERT INTO SERVERCOMMANDLINKS VALUES ('DemoWIN-DataHunterSeleniumDeployAndExecute','DataHunterSeleniumDeployAndExecute');
INSERT INTO SERVERCOMMANDLINKS VALUES ('DemoWIN-DataHunterSeleniumGenJmeterReport','DataHunterSeleniumGenJmeterReport');
INSERT INTO SERVERCOMMANDLINKS VALUES ('DemoWIN-DataHunterSeleniumTrendsLoad','DataHunterSeleniumTrendsLoad');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_LINUX','LINUX_free_m_1_1');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_LINUX','LINUX_mpstat_1_1');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_WINDOWS','FreePhysicalMemory');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_WINDOWS','FreeVirtualMemory');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_WINDOWS','WinCpuCmd');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_WINDOWS_HOSTID','FreePhysicalMemory');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_WINDOWS_HOSTID','FreeVirtualMemory');
INSERT INTO SERVERCOMMANDLINKS VALUES ('localhost_WINDOWS_HOSTID','WinCpuCmd');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteLinuxServer','LINUX_free_m_1_1');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteLinuxServer','LINUX_mpstat_1_1');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteUnixVM','UNIX_lparstat_5_1');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteUnixVM','UNIX_Memory_Script');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteWinServer','FreePhysicalMemory');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteWinServer','FreeVirtualMemory');
INSERT INTO SERVERCOMMANDLINKS VALUES ('remoteWinServer','WinCpuCmd');
INSERT INTO SERVERCOMMANDLINKS VALUES ('SimpleScriptSampleRunner', 'SimpleScriptSampleCmd');
INSERT INTO SERVERCOMMANDLINKS VALUES ('NewRelicTestProfile', 'NewRelicSampleCmd'); 


INSERT INTO COMMANDPARSERLINKS VALUES ('DataHunterSeleniumDeployAndExecute','Return1');
INSERT INTO COMMANDPARSERLINKS VALUES ('DataHunterSeleniumDeployAndExecute_LINUX','Return1');
INSERT INTO COMMANDPARSERLINKS VALUES ('DataHunterSeleniumGenJmeterReport','Return1');
INSERT INTO COMMANDPARSERLINKS VALUES ('DataHunterSeleniumGenJmeterReport_LINUX','Return1');
INSERT INTO COMMANDPARSERLINKS VALUES ('DataHunterSeleniumTrendsLoad','Return1');
INSERT INTO COMMANDPARSERLINKS VALUES ('DataHunterSeleniumTrendsLoad_LINUX','Return1');
INSERT INTO COMMANDPARSERLINKS VALUES ('FreePhysicalMemory','Memory_FreePhysicalG');
INSERT INTO COMMANDPARSERLINKS VALUES ('FreeVirtualMemory','Memory_FreeVirtualG');
INSERT INTO COMMANDPARSERLINKS VALUES ('LINUX_free_m_1_1','LINUX_Memory_freeG');
INSERT INTO COMMANDPARSERLINKS VALUES ('LINUX_free_m_1_1','LINUX_Memory_totalG');
INSERT INTO COMMANDPARSERLINKS VALUES ('LINUX_free_m_1_1','LINUX_Memory_usedG');
INSERT INTO COMMANDPARSERLINKS VALUES ('LINUX_mpstat_1_1','Nix_CPU_UTIL');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_lparstat_5_1','Nix_CPU_UTIL');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_Memory_Script','UNIX_Memory_numperm_percent');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_Memory_Script','UNIX_Memory_pgsp_aggregate_util');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_Memory_Script','UNIX_Memory_pinned_percent');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_VM_Memory','UNIX_Memory_numperm_percent');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_VM_Memory','UNIX_Memory_pgsp_aggregate_util');
INSERT INTO COMMANDPARSERLINKS VALUES ('UNIX_VM_Memory','UNIX_Memory_pinned_percent');
INSERT INTO COMMANDPARSERLINKS VALUES ('WinCpuCmd','WicnCpu');

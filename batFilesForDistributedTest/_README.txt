Typical Parameters for a Distributed Jenkins Jobs
---------------------------------------------------


CHECK JOB 
-----------
Application:									DataHunterDistributed
Duration:										20
Users:											1
JmeterHome:										C:\Jmeter\apache-jmeter5-DataHunter
JmeterTestPlan									C:\Jmeter\apache-jmeter5-DataHunter\bin\test-plans\DataHunterSeleniumTestPlanUsingExcel.jmx
OverrideTestResultsFile:						C:\Jmeter\Temp\DataHunterResults.csv
ExtraParam1:									-JForceTxnFailPercent=0
ExtraParam2:									-JDataHunterUrl=http://{SERVER-RUNNING-DATAHUNTER}:8081/dataHunter	   (OR WHATEVER PORT ITS USING)
ExtraParam3:									-JMark59ServerMetricsWebUrl="http://{SERVER-RUNNING-WEB-SERVER-METRICS}:8085/mark59-server-metrics-web"  (OR WHATEVER PORT ITS USING)
SetPath:										"C:\Program Files\Java\jdk-11\bin";C:\Windows\System32;C:\windows\system32\wbem

Restrict where this project can be run:			{your jmeter master server} 

Build command:									C:\work\Admin\JenkinsConfig\jmeterApplicationCheck-v1.bat



START JMETER DISTRIBUTED SERVERS
--------------------------------
RunCI: (boolean param)							ticked
ScriptVerificationPassed: (boolean param)		ticked
JmeterHome:										C:\Jmeter\apache-jmeter5-DataHunter
DistributedServersList
												 Comma delimited list of remote server names followed by their ip address on which the Test Plan will execute on. 
												 Format is "serverName:serverIP,.." Do not include the master server. 
												 Sample Usage:  slavesrvr1:10.130.01.001,slavesrvr2:10.130.01.002,slavesrvr03;10.130.01.003
												 
MasterIP:										ip of your jmeter master server.  Sample Usage:  10.111.222.150

SetPath:										"C:\Program Files\Java\jdk-11\bin";C:\Windows\System32;C:\windows\system32\wbem


Restrict where this project can be run:			{your jmeter master server} 

Build command:									CALL C:\work\admin\JenkinsConfig\jmeterApplicationDistributedJmeterServicesBypassRMI-noKill.bat





EXECUTE DISRIBUTED JMETER TEST 
-----------------------------	
RunCI: (boolean param)							ticked
ScriptVerificationPassed: (boolean param)		ticked	 
Application:									DataHunterDistributed
JmeterHome:										C:\Jmeter\apache-jmeter5-DataHunter
JmeterTestPlan:									C:\Jmeter\apache-jmeter5-DataHunter\bin\test-plans\DataHunterSeleniumTestPlanUsingExcel.jmx
TestResultsFileName:							DataHunterDistributedTestResults.csv

DistributedIPsList		
												For Distributed Tests Only. The list of IPs to run the Test Plan (with master ip first in list). Include the " -R". 
												Sample Usage:  -R 10.111.222.150,10.130.01.001,10.130.01.002,10.130.01.003

Duration:										(blank)	
ExtraParam1:									-GForceTxnFailPercent=0
ExtraParam2										-GMark59ServerMetricsWebUrl="http://{SERVER-RUNNING-WEB-SERVER-METRICS}:8085/mark59-server-metrics-web"  (OR WHATEVER PORT ITS USING)
ExtraParam3										-GDataHunterUrl=http://{SERVER-RUNNING-DATAHUNTER}:8081/dataHunter	   (OR WHATEVER PORT ITS USING)
ExtraParam4:									-GStartCdpListeners=true

ExtraParam5										-GMetricsWebRestrictToIP={ip of server running the Web Server Metrics application}

CopyResults (boolean parameter)					unticked
CopyResutsDirectory:							C:\Temp
SetPath:										"C:\Program Files\Java\jdk-11\bin";C:\Windows\System32;C:\windows\system32\wbem

Restrict where this project can be run:			{your jmeter master server} 

Build command:									call C:\work\admin\JenkinsConfig\jmeterApplicationExecuteDistributedTest.bat



RESULTS JOB 
-----------
parmExecuteScriptVerificationPassed: (booean)	unticked
parmExecuteResult:								Execution_OK
Application:									DataHunterDistributed
Granularity:									15000
JmeterHomeReportGeneration:						C:\Jmeter\apache-jmeter5-reporting      (actually, this  can be any valid jmeter instance) 
MetricsReportSplit: (choice param)				SplitByDataType
ErrorTransactionNaming: (choice param)			Rename
eXcludeResultsWithSub: (choice param)			True
eXcludestart:									(blank)
captureperiod:									(blank)

SetPath:										"C:\Program Files\Java\jdk-11\bin";C:\Windows\System32;C:\windows\system32\wbem

Restrict where this project can be run:			{your jmeter master server} 

Build command:									CALL C:\work\Admin\JenkinsConfig\jmeterApplicationResult-v3.bat
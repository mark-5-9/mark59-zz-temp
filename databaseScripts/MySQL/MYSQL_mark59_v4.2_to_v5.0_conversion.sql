
-- *************************************************************************************
-- **
-- **   from 4.2 to 5.0.xx   
-- **
-- **   RENAME OF DATABASES
-- **   ------------------
-- **   Rename the databases as below (we suggest also doing a backup): 
-- **
-- **       datahunterdb               to mark59datahunterdb 
-- **       mark59servermetricswebdb   to mark59metricsdb
-- **       metricsdb                  to mark59trendsdb 
-- **
-- *************************************************************************************

--  Updates for the mark59metricsdb (ex mark59servermetricswebdb) tables
--  --------------------------------------------------------------------
--  Due to code and name changes the following changes may be needed depending on your requirements.
--  The changes assume you want/have the sample Profiles as originally provided in 4.2, and have followed similar
--   patterns  when creating additional Profiles.  Please review before execution.        


SET SQL_SAFE_UPDATES = 0;

-- as the RunCheck program is now called TrendsLoad, and directory /metrics is renamed to /mark59-trends-load. 

DELETE FROM mark59metricsdb.SERVERPROFILES WHERE SERVER_PROFILE_NAME = 'DemoLINUX-DataHunterSeleniumRunCheck';
DELETE FROM mark59metricsdb.SERVERPROFILES WHERE SERVER_PROFILE_NAME = 'DemoWIN-DataHunterSeleniumRunCheck';
INSERT INTO mark59metricsdb.SERVERPROFILES VALUES ('DemoLINUX-DataHunterSeleniumTrendsLoad','SSH_LINIX_UNIX','localhost','','','','','22','60000','Loads Trend Analysis (MYSQL database).  See:<br>http://localhost:8083/mark59-trends/trending?reqApp=DataHunter',NULL);
INSERT INTO mark59metricsdb.SERVERPROFILES VALUES ('DemoWIN-DataHunterSeleniumTrendsLoad','WMIC_WINDOWS','localhost','','','','','','','Loads Trend Analysis (MYSQL database).  See:<br>http://localhost:8083/mark59-trends/trending?reqApp=DataHunter',NULL);

DELETE FROM mark59metricsdb.COMMANDS WHERE COMMAND_NAME = 'DataHunterSeleniumRunCheck';
DELETE FROM mark59metricsdb.COMMANDS WHERE COMMAND_NAME = 'DataHunterSeleniumRunCheck_LINUX';
INSERT INTO mark59metricsdb.COMMANDS VALUES ('DataHunterSeleniumTrendsLoad','WMIC_WINDOWS','process call create \'cmd.exe /c \r\n echo Load DataHunter Test Results into Mark59 Trends Graph database. & \r\n cd /D  %SERVER_METRICS_WEB_BASE_DIR% & \r\n cd ../mark59-trends-load &  \r\n \r\n java -jar ./target/mark59-trends-load.jar -a DataHunter -i C:\\Mark59_Runs\\Jmeter_Results\\DataHunter -d mysql &\r\n PAUSE\r\n\'\r\n','N','',NULL);
INSERT INTO mark59metricsdb.COMMANDS VALUES ('DataHunterSeleniumTrendsLoad_LINUX','SSH_LINIX_UNIX','echo This script runs mark59-trends-load,to load results from a DataHunter test run into the Metrics TrendS Graph.\r\necho starting from $PWD;\r\n\r\n{   # try  \r\n\r\n    cd ../mark59-trends-load/target &&\r\n    gnome-terminal -- sh -c \"java -jar mark59-trends-load.jar -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter -d mysql; exec bash\"\r\n\r\n} || { # catch \r\n    echo attempt to execute mark59-trends-load has failed! \r\n}\r\n','Y','refer bin/TestRunLINUX-DataHunter-Selenium-metricsTrendsLoad.sh',NULL);

DELETE FROM mark59metricsdb.SERVERCOMMANDLINKS WHERE SERVER_PROFILE_NAME = 'DemoLINUX-DataHunterSeleniumRunCheck';
DELETE FROM mark59metricsdb.SERVERCOMMANDLINKS WHERE SERVER_PROFILE_NAME = 'DemoWIN-DataHunterSeleniumRunCheck';
INSERT INTO mark59metricsdb.SERVERCOMMANDLINKS VALUES ('DemoLINUX-DataHunterSeleniumTrendsLoad','DataHunterSeleniumTrendsLoad_LINUX');
INSERT INTO mark59metricsdb.SERVERCOMMANDLINKS VALUES ('DemoWIN-DataHunterSeleniumTrendsLoad','DataHunterSeleniumTrendsLoad');

DELETE FROM mark59metricsdb.COMMANDPARSERLINKS WHERE COMMAND_NAME = 'DataHunterSeleniumRunCheck';
DELETE FROM mark59metricsdb.COMMANDPARSERLINKS WHERE COMMAND_NAME = 'DataHunterSeleniumRunCheck_LINUX';
INSERT INTO mark59metricsdb.COMMANDPARSERLINKS VALUES ('DataHunterSeleniumTrendsLoad','Return1');
INSERT INTO mark59metricsdb.COMMANDPARSERLINKS VALUES ('DataHunterSeleniumTrendsLoad_LINUX','Return1');

-- as directory structures/jars names of mark59 have changed ( dataHunterPerformanceTestSamples to mark59-datahunter-samples, mark59-server-metrics to mark59-metrics-api, resultFilesConverter to mark59-results-splitter )   

DELETE FROM mark59metricsdb.COMMANDS WHERE COMMAND_NAME = 'DataHunterSeleniumDeployAndExecute';
DELETE FROM mark59metricsdb.COMMANDS WHERE COMMAND_NAME = 'DataHunterSeleniumDeployAndExecute_LINUX';
DELETE FROM mark59metricsdb.COMMANDS WHERE COMMAND_NAME = 'DataHunterSeleniumGenJmeterReport';
DELETE FROM mark59metricsdb.COMMANDS WHERE COMMAND_NAME = 'DataHunterSeleniumGenJmeterReport_LINUX';

INSERT INTO mark59metricsdb.COMMANDS VALUES ('DataHunterSeleniumDeployAndExecute','WMIC_WINDOWS','process call create \'cmd.exe /c \r\n echo Running Directly From Server Metrics Web (cmd DataHunterSeleniumDeployAndExecute) & \r\n echo  SERVER_METRICS_WEB_BASE_DIR: %SERVER_METRICS_WEB_BASE_DIR% & \r\n cd /D %SERVER_METRICS_WEB_BASE_DIR% &\r\n cd ..\\mark59-datahunter-samples & \r\n DEL C:\\apache-jmeter\\bin\\mark59.properties & COPY .\\mark59.properties C:\\apache-jmeter\\bin &\r\n DEL C:\\apache-jmeter\\bin\\chromedriver.exe  & COPY .\\chromedriver.exe  C:\\apache-jmeter\\bin &\r\n DEL C:\\apache-jmeter\\lib\\ext\\mark59-metrics-api.jar &\r\n COPY ..\\mark59-metrics-api\\target\\mark59-metrics-api.jar  C:\\apache-jmeter\\lib\\ext & \r\n DEL C:\\apache-jmeter\\lib\\ext\\mark59-datahunter-samples.jar & \r\n COPY .\\target\\mark59-datahunter-samples.jar  C:\\apache-jmeter\\lib\\ext & \r\n RMDIR /S /Q C:\\apache-jmeter\\lib\\ext\\mark59-datahunter-samples-dependencies &\r\n MKDIR C:\\apache-jmeter\\lib\\ext\\mark59-datahunter-samples-dependencies &\r\n COPY .\\target\\mark59-datahunter-samples-dependencies  C:\\apache-jmeter\\lib\\ext\\mark59-datahunter-samples-dependencies &\r\n\r\n mkdir C:\\Mark59_Runs &\r\n mkdir C:\\Mark59_Runs\\Jmeter_Results &\r\n mkdir C:\\Mark59_Runs\\Jmeter_Results\\DataHunter &\r\n\r\n set path=%path%;C:\\Windows\\System32;C:\\windows\\system32\\wbem & \r\n cd /D C:\\apache-jmeter\\bin &\r\n\r\n jmeter -n -X -f -t %SERVER_METRICS_WEB_BASE_DIR%\\..\\mark59-datahunter-samples\\test-plans\\DataHunterSeleniumTestPlan.jmx -l C:\\Mark59_Runs\\Jmeter_Results\\DataHunter\\DataHunterTestResults.csv -JForceTxnFailPercent=0 -JDataHunterUrl=http://localhost:8081/mark59-datahunter -JStartCdpListeners=false &\r\n\r\n PAUSE\r\n\'\r\n','N','refer DeployDataHunterTestArtifactsToJmeter.bat and DataHunterExecuteJmeterTest.bat in mark59-datahunter-samples ','[]');
INSERT INTO mark59metricsdb.COMMANDS VALUES ('DataHunterSeleniumDeployAndExecute_LINUX','SSH_LINIX_UNIX','echo This script runs the JMeter deploy in the background, then opens a terminal for JMeter execution.\r\necho starting from $PWD;\r\n\r\n{   # try  \r\n\r\n    cd ../mark59-datahunter-samples && \r\n    DH_TEST_SAMPLES_DIR=$(pwd) && \r\n    echo mark59-datahunter-samples base dir is $DH_TEST_SAMPLES_DIR &&\r\n\r\n    cp ./mark59.properties ~/apache-jmeter/bin/mark59.properties &&\r\n    cp ./chromedriver ~/apache-jmeter/bin/chromedriver && \r\n    cp ../mark59-metrics-api/target/mark59-metrics-api.jar  ~/apache-jmeter/lib/ext/mark59-metrics-api.jar && \r\n    cp ./target/mark59-datahunter-samples.jar  ~/apache-jmeter/lib/ext/mark59-datahunter-samples.jar && \r\n    mkdir -p ~/Mark59_Runs/Jmeter_Results/DataHunter &&\r\n    rm -rf ~/apache-jmeter/lib/ext/mark59-datahunter-samples-dependencies &&\r\n    cp -r ./target/mark59-datahunter-samples-dependencies ~/apache-jmeter/lib/ext/mark59-datahunter-samples-dependencies &&\r\n \r\n    gnome-terminal -- sh -c \"~/apache-jmeter/bin/jmeter -n -X -f  -t $DH_TEST_SAMPLES_DIR/test-plans/DataHunterSeleniumTestPlan.jmx -l ~/Mark59_Runs/Jmeter_Results/DataHunter/DataHunterTestResults.csv -JForceTxnFailPercent=0 -JStartCdpListeners=false; exec bash\"\r\n\r\n} || { # catch \r\n    echo Deploy was unsuccessful! \r\n}','Y','refer bin/TestRunLINUX-DataHunter-Selenium-DeployAndExecute.sh','[]');
INSERT INTO mark59metricsdb.COMMANDS VALUES ('DataHunterSeleniumGenJmeterReport','WMIC_WINDOWS','process call create \'cmd.exe /c \r\n cd /D %SERVER_METRICS_WEB_BASE_DIR% & \r\n cd../mark59-results-splitter & \r\n CreateDataHunterJmeterReports.bat\'\r\n','N','',NULL);
INSERT INTO mark59metricsdb.COMMANDS VALUES ('DataHunterSeleniumGenJmeterReport_LINUX','SSH_LINIX_UNIX','echo This script creates a set of JMeter reports from a DataHunter test run.\r\necho starting from $PWD;\r\n\r\n{   # try  \r\n\r\n    cd ../mark59-results-splitter\r\n    gnome-terminal -- sh -c \"./CreateDataHunterJmeterReports.sh; exec bash\"\r\n\r\n} || { # catch \r\n    echo attempt to generate JMeter Reports has failed! \r\n}\r\n','Y','refer bin/TestRunLINUX-DataHunter-Selenium-GenJmeterReport.sh',NULL);

-- as mark59 package names have changed  (com.mark59.servermetricsweb.XX to com.mark59.metrics.XX, which can affect Grovy scripts) 

UPDATE mark59metricsdb.COMMANDS SET COMMAND = REPLACE(COMMAND,'com.mark59.servermetricsweb', 'com.mark59.metrics');

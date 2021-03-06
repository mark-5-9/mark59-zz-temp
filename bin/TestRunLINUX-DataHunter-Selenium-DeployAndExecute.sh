#     -------------------------------------------------------------------------------------------------------------------------------------------------
#     | Deploy Artifacts and Run JMeter DataHunter Selenium Test.
#     |
#     | NOTE - you may need to ensure the chromedriver.exe file at root of mark59-datahunter-samples project is compatible with your Chrome version
#     |        (see Mark59 user guide for details).  
#     |      - mark59serverprofiles.xlsx is not copied. Before you run the '..usingExcel' testplan, copy it manually to the JMeter bin directory
#     |        (not necessary before running this bat file as it runs the DataHunterSeleniumTestPlan which doesn't use the spreadsheet).
#     |
#     |  An instance of JMeter is expected at C:\apache-jmeter
#     |
#     |  Alternative to running this .sh 
#     |		 - login  to the server-metrics-web application  "http://localhost:8085/mark59-metrics" 
#     |		 - run the DemoLINUX-DataHunter-Selenium-DeployAndExecute profile. 
#     |
#     |  The only database considerations are that when the test is running server metrics are obtained invoking the 'localhost_LINUX' profile from the running
#     |      server-metrics-web application (ie via whatever DB it is connected to), and also the datahunter application will be writing to its database.
#     |
#     |  logging at  ~/apache-jmeter/bin/jmeter.log
#     |  JMeter result file output to ~/Mark59_Runs/Jmeter_Results/DataHunter
#     |
#     -------------------------------------------------------------------------------------------------------------------------------------------------

echo This script runs the JMeter deploy in the background, then opens a terminal for JMeter execution.
echo starting from $PWD;

# use SET "StartCdpListeners=true" to allow the cdp listeners in the test script to execute 
StartCdpListeners=false
# StartCdpListeners=true
ehco StartCdpListeners is StartCdpListeners 

{   # try  

    cd ../mark59-datahunter-samples && 
    DH_TEST_SAMPLES_DIR=$(pwd) && 
    echo mark59-datahunter-samples base dir is $DH_TEST_SAMPLES_DIR &&

    cp ./mark59.properties ~/apache-jmeter/bin/mark59.properties &&
    cp ./chromedriver ~/apache-jmeter/bin/chromedriver && 
    cp ../mark59-metrics-api/target/mark59-metrics-api.jar  ~/apache-jmeter/lib/ext/mark59-metrics-api.jar && 
    cp ./target/mark59-datahunter-samples.jar  ~/apache-jmeter/lib/ext/mark59-datahunter-samples.jar &&
    rm -rf ~/apache-jmeter/lib/ext/mark59-datahunter-samples-dependencies &&
    cp -r ./target/mark59-datahunter-samples-dependencies ~/apache-jmeter/lib/ext/mark59-datahunter-samples-dependencies &&
    mkdir -p ~/Mark59_Runs/Jmeter_Results/DataHunter && 
 
    gnome-terminal -- sh -c "~/apache-jmeter/bin/jmeter -n -X -f  -t $DH_TEST_SAMPLES_DIR/test-plans/DataHunterSeleniumTestPlan.jmx -l ~/Mark59_Runs/Jmeter_Results/DataHunter/DataHunterTestResults.csv -JDataHunterUrl=http://localhost:8081/mark59-datahunter -JForceTxnFailPercent=0 -JStartCdpListeners=$StartCdpListeners; exec bash"

} || { # catch 
    echo Deploy was unsuccessful! 
}

#   -------------------------------------------------------------------------------------------------------------------------------------------------
#   | Generate the JMeter reports for a DataHunter Selenium Test.
#   |
#   |  Alternative to running this .bat 
#   |		 - login  to the server-metrics-web application  "http://localhost:8085/mark59-metrics" 
#   |		 - run the DemoWIN-DataHunter-Selenium-GenJmeterReport profile. 
#   |
#   |  There are are no database considerations when running JMeter report generation.
#   |
#   |  JMeter input results file expected at ~/Mark59_Runs/Jmeter_Results/DataHunter/
#   |
#   |  Output generated to ~/Mark59_Runs/Jmeter_Reports/DataHunter/ 
#   |
#   -------------------------------------------------------------------------------------------------------------------------------------------------
{   # try  

    cd ../mark59-results-splitter
    gnome-terminal -- sh -c "./CreateDataHunterJmeterReports.sh; exec bash"

} || { # catch 
    echo attempt to generate JMeter Reports has failed! 
}
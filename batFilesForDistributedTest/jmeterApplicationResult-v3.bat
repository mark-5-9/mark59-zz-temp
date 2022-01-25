C:
echo parmExecuteScriptVerificationPassed=%parmExecuteScriptVerificationPassed%
echo parmExecuteResult=%parmExecuteResult%

set PATH=%SetPath%

echo PATH       is %PATH%

java -version

if "%parmExecuteResult%" equ "Execution_OK" (
   

    echo "load results into graphical comparative analysis tooling" 

    cd C:\Jmeter\_runcheck
    java -jar metricsRuncheck.jar -a %Application% -i C:\Jmeter\Jmeter_Results\%Application% -h pv-reporting.auiag.corp  -p 3309 -t JMETER %eXcludestart% %captureperiod% -r "<a href='%BUILD_URL%HTML_Report'>run %BUILD_NUMBER%</a>"

    echo "convert xml file to csv" 

    if exist C:\Jmeter\Jmeter_Results\%Application%\MERGED\ RD /S /Q C:\Jmeter\Jmeter_Results\%Application%\MERGED	
    mkdir C:\Jmeter\Jmeter_Results\%Application%\MERGED\ 

    java -jar resultFilesConverter.jar -iC:\Jmeter\Jmeter_Results\%Application% -f%Application%TestResults_converted.csv -m%MetricsReportSplit% -e%ErrorTransactionNaming% -x%eXcludeResultsWithSub%

    echo "run jmeter report(s) generation"     

    rmdir /s /q C:\Jmeter\htmlreport\%Application%
    md C:\Jmeter\htmlreport\%Application%
    
    rmdir /s /q C:\Jmeter\htmlreport\%Application%_CPU_UTIL
    md C:\Jmeter\htmlreport\%Application%_CPU_UTIL
    
    rmdir /s /q C:\Jmeter\htmlreport\%Application%_DATAPOINT
    md C:\Jmeter\htmlreport\%Application%_DATAPOINT
    
    rmdir /s /q C:\Jmeter\htmlreport\%Application%_MEMORY
    md C:\Jmeter\htmlreport\%Application%_MEMORY

    rmdir /s /q C:\Jmeter\htmlreport\%Application%_METRICS
    md C:\Jmeter\htmlreport\%Application%_METRICS


    cd %JmeterHomeReportGeneration%\bin

    jmeter -Dlog4j2.formatMsgNoLookups=true -Jjmeter.reportgenerator.overall_granularity=%Granularity% -g  C:\Jmeter\Jmeter_Results\%Application%\MERGED\%Application%TestResults_converted.csv -o C:\Jmeter\htmlreport\%Application%

    jmeter -Dlog4j2.formatMsgNoLookups=true -Jjmeter.reportgenerator.overall_granularity=%Granularity% -g  C:\Jmeter\Jmeter_Results\%Application%\MERGED\%Application%TestResults_converted_CPU_UTIL.csv -o C:\Jmeter\htmlreport\%Application%_CPU_UTIL	

    jmeter -Dlog4j2.formatMsgNoLookups=true -Jjmeter.reportgenerator.overall_granularity=%Granularity% -g  C:\Jmeter\Jmeter_Results\%Application%\MERGED\%Application%TestResults_converted_DATAPOINT.csv -o C:\Jmeter\htmlreport\%Application%_DATAPOINT	

    jmeter -Dlog4j2.formatMsgNoLookups=true -Jjmeter.reportgenerator.overall_granularity=%Granularity% -g  C:\Jmeter\Jmeter_Results\%Application%\MERGED\%Application%TestResults_converted_MEMORY.csv -o C:\Jmeter\htmlreport\%Application%_MEMORY	

    jmeter -Dlog4j2.formatMsgNoLookups=true -Jjmeter.reportgenerator.overall_granularity=%Granularity% -g  C:\Jmeter\Jmeter_Results\%Application%\MERGED\%Application%TestResults_converted_METRICS.csv -o C:\Jmeter\htmlreport\%Application%_METRICS

    TIME /T
)


SET mailmessage=nothing

if "%parmExecuteResult%" equ "Execution_OK" (
   SET mailmessage=Scenario_Run_ok
)
if "%parmExecuteResult%" equ "Execution_failure" (
   SET mailmessage=Scenario_Run_Fail
)
if %parmExecuteScriptVerificationPassed% equ false (
   SET mailmessage=Script_verify_failure
) 


echo BEGINMAIL%mailmessage%ENDMAIL


if "%parmExecuteResult%" equ "Execution_OK" (
   exit 0
)
if "%parmExecuteResult%" equ "Execution_failure" (
   exit 1
)
if %parmExecuteScriptVerificationPassed% equ false (
   exit 1
) 
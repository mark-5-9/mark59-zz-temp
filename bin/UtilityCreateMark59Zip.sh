#!/bin/sh
#   --------------------------------------------------------------------------------------------------------------
#   |  An adminstration tool to create the file structure for inclusion in the download Mark59 zip file. 
#   | 
#   |  Files are derived from source git repo plus the built executables in target folders, and any other artefacts
#   |  required for execution (eg bat and shell files).
#   | 
#   |  Source is also included for mark59-datahunter-samples and mark59-dsl-samples projects. 
#   |   
#   |  When a new version of mark59-core and mark59-selenium-implementation is being used that is yet to be uploaded to     
#   |  Maven central those maven projects must also be included.   
#   --------------------------------------------------------------------------------------------------------------
# SOURCE_DIR=~/gitrepo/mark-5-9/mark59-wip/
# DEST_DIR=~/mark59-3.x_PRE_RELEASE"

SOURCE_DIR=~/gitrepo/mark59-wip/
DEST_DIR=~/mark59-4.2/ 

rm -rf ${DEST_DIR}
mkdir -p ${DEST_DIR}
cd ~/gitrepo/mark59-wip/

rsync -av -m --exclude '.*' --exclude '*/src' --exclude '*/webapp' --exclude '*/test' --exclude '*/WEB-INF' --exclude '*/classes' --exclude '*/test-classes' --exclude 'TESTDATA' --exclude '*/maven*' --exclude 'archive-tmp' --exclude '*/m2e-wtp*' --exclude '*/surefire*' --exclude '*.log' --exclude '*.png' --exclude '*.gif' --exclude '*.original' --exclude '*.java' --exclude 'UtilityCreateMark59Zip.*' --exclude 'pom.xml' --exclude 'mark59-metrics.jar' --exclude 'mark59-metrics-api.jar' --exclude 'mark59-datahunter-samples' --exclude 'mark59-dsl-samples' --exclude 'mark59-core' --exclude 'mark59-selenium-implementation' "${SOURCE_DIR}" "${DEST_DIR}"

# In the command below, for a _PRE_RELEASE version: do NOT include the directives "--exclude 'mark59-core' --exclude 'mark59-selenium-implementation' " 
#     (that way core and selenium-implementation project jars are avaiable before they have been added in Maven Central)
# When building the full _RELEASE zip file, add the directives in. That is, the end of the command should look like :
#      ... --exclude 'mark59-results-splitter' --exclude 'mark59-core' --exclude 'mark59-selenium-implementation' "${SOURCE_DIR}" "${DEST_DIR}"  

rsync -a -m --exclude '.*' --exclude '*/classes' --exclude '*/test-classes' --exclude '*/maven*' --exclude 'archive-tmp' --exclude '*/m2e-wtp*' --exclude '*/surefire*' --exclude '*.log' --exclude '*.original' --exclude '/pom.xml' --exclude 'bin' --exclude 'databaseScripts' --exclude 'dataHunter' --exclude 'mark59-datahunter-api' --exclude 'mark59-metrics' --exclude 'mark59-metrics-api' --exclude 'mark59-trends' --exclude 'mark59-trends-load' --exclude 'mark59-results-splitter' --exclude 'mark59-core' --exclude 'mark59-selenium-implementation' "${SOURCE_DIR}" "${DEST_DIR}"

$SHELL

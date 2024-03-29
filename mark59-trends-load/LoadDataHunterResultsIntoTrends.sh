#   -------------------------------------------------------------------------------------------------------------------------------------------------
#   |  Load DataHunter Test Results to Mark59 Trends Analysis database.
#   | 
#   |  This bat assumes - the mark59-trends-load.jar file exists in the ./target directory (relative to this file) 
#   |                   - when using a MySQL or Postgres database, the mark59trendsdb database exists locally (using defaults)
#   |
#   |  Notes : the use of double quotes in a few places, required to cater for the & (ampersand) char, or to enter a space (equates to a blank here). 
#   |          the current time is used as the reference..
#   |
#   |  To directly execute :
#   |  ---------------------
#   |  You need to un-# whichever DATABASE value you wish to use.  
#   |  (re-# all the set DATABASE lines afterwards, but leaving DATABASE=$1 uncommented, if you intend to run this .sh via the mark59 /bin directory shell)
#   |
#   -------------------------------------------------------------------------------------------------------------------------------------------------
# DATABASE=H2
# DATABASE=MYSQL
# DATABASE=POSTGRES

# -- special purpose values
# DATABASE=H2MEM
# DATABASE=H2TCPCLIENT

DATABASE=$1

CURRENTDATE=`date +"%Y-%m-%d:%T"`
echo Current Date and Time: ${CURRENTDATE}
echo The database has been set to $DATABASE

if [ "$DATABASE" = "" ]; then
	# Using H2  Starting mark59-trends-load batch for a dataHunter load (defaults taken on parameters) 
	echo No database set, so assuming H2
	java -jar ./target/mark59-trends-load.jar  -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter/ -d h2   	
fi

if [ "$DATABASE" = "H2" ]; then
	# Using H2  Starting mark59-trends-load batch for a dataHunter load (defaults taken on parameters) 
	java -jar ./target/mark59-trends-load.jar  -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter/ -d h2   	
fi

if [ "$DATABASE" = "H2TCPCLIENT" ]; then
	# Using H2 using a tcp connection to start  mark59-trends-load batch for a dataHunter load (defaults taken on parameters. (Note for docker use -h metrics) 
	java -jar ./target/mark59-trends-load.jar  -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter/ -d h2tcpclient -h localhost  -p 9092
fi

if [ "$DATABASE" = "H2MEM" ]; then
	# Using H2 in memory   Starting mark59-trends-load batch for a dataHunter load (defaults taken on parameters) 
	java -jar ./target/mark59-trends-load.jar  -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter/ -d h2mem   	
fi

if [ "$DATABASE" = "MYSQL" ]; then
	# using MySQL:  Starting mark59-trends-load batch with some parameters provided (defaults taken on other parameters. ) 
	java -jar ./target/mark59-trends-load.jar -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter/ -d mysql -h localhost  -p 3306 -s mark59trendsdb -q "?allowPublicKeyRetrieval=true&useSSL=false" -t JMETER  -r ${CURRENTDATE}
fi

if [ "$DATABASE" = "POSTGRES" ]; then
	# using Postgress:  Starting mark59-trends-load batch with some parameters provided (defaults taken on other parameters. ) 
	java -jar ./target/mark59-trends-load.jar -a DataHunter -i ~/Mark59_Runs/Jmeter_Results/DataHunter/ -d pg -h localhost  -p 5432 -s mark59trendsdb -q "?sslmode=disable" -t JMETER  -r ${CURRENTDATE}
fi


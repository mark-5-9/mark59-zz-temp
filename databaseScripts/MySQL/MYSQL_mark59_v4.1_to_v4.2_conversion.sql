
-- *************************************************************************************
-- **
-- **   from 4.1 to 4.2   
-- **   Only change is in the DataHunter MYSQL policies table, precision of DATATIME fields    
-- **   have changed from secs to nanosecs (bring in line with Postgress precision, and 
-- **   near H2 (msecs).  Was done to faciltate DataHunter REST API test cases. 
-- **
-- *************************************************************************************


SET SQL_SAFE_UPDATES = 0;

ALTER TABLE datahunterdb.POLICIES  MODIFY CREATED TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE datahunterdb.POLICIES  MODIFY UPDATED TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6);
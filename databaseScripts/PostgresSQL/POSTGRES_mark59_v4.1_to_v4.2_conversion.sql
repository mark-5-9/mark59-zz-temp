
-- *************************************************************************************
-- **
-- **   from 4.1 to 4.2   
-- **   Align TIMESTAMP defaults with the H2 and MYSQL databases 
-- **
-- **   NOTE: its still not exactly the same as H2 and MYSQL as PG does not have 
-- **         a table 'ON UPDATE' option 
-- **
-- *************************************************************************************

-- connect to the metricsdb database and run:

 ALTER TABLE POLICIES ALTER CREATED SET DEFAULT CURRENT_TIMESTAMP;

 ALTER TABLE POLICIES ALTER UPDATED SET DEFAULT CURRENT_TIMESTAMP;
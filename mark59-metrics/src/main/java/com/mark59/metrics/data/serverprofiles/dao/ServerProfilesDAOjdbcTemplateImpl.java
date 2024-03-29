/*
 *  Copyright 2019 Mark59.com
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mark59.metrics.data.serverprofiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.mark59.metrics.data.beans.ServerProfile;

/**
 * @author Philip Webb
 * Written: Australian Summer 2020  
 */
public class ServerProfilesDAOjdbcTemplateImpl implements ServerProfilesDAO 
{
	
	@Autowired  
	private DataSource dataSource;
		

	@Override
	public ServerProfile  findServerProfile(String serverProfileName){
		
		String selectServerSQL = "select EXECUTOR, SERVER, ALTERNATE_SERVER_ID, USERNAME, "
				+ "PASSWORD, PASSWORD_CIPHER, CONNECTION_PORT, CONNECTION_TIMEOUT, COMMENT, PARAMETERS "
				+ "from SERVERPROFILES where SERVER_PROFILE_NAME = :serverProfileName "
				+ " order by SERVER_PROFILE_NAME ";

		MapSqlParameterSource sqlparameters = new MapSqlParameterSource()
				.addValue("serverProfileName", serverProfileName);

//		System.out.println(" findServerProfile : " + selectServerSQL + " : " + serverProfileName);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(selectServerSQL, sqlparameters);
		
		if (rows.size() == 0 ){
			return null;
		}
		Map<String, Object>  row = rows.get(0);
		ServerProfile server = new ServerProfile();
		server.setServerProfileName(serverProfileName); 
		server.setExecutor((String)row.get("EXECUTOR")); 
		server.setServer((String)row.get("SERVER")); 
		server.setAlternativeServerId((String)row.get("ALTERNATE_SERVER_ID")); 		
		server.setUsername((String)row.get("USERNAME")); 		
		server.setPassword((String)row.get("PASSWORD")); 		
		server.setPasswordCipher((String)row.get("PASSWORD_CIPHER")); 		
		server.setConnectionPort((String)row.get("CONNECTION_PORT")); 		
		server.setConnectionTimeout((String)row.get("CONNECTION_TIMEOUT")); 		
		server.setComment((String)row.get("COMMENT")); 		
		server.setParameters(deserializeJsonToMap((String)row.get("PARAMETERS"))); 		
//		System.out.println("ServerProfilesDAOjdbcTemplateImpl.findServerProfile  : " + serverProfileName.toString()  ) ;		
		return  server;
	}

	
	@Override
	public List<ServerProfile> findServerProfiles(){
		return  findServerProfiles("","");
	}

	@Override
	public List<ServerProfile> findServerProfiles(String selectionCol, String selectionValue){

		String sql = "select SERVER_PROFILE_NAME, EXECUTOR, SERVER, ALTERNATE_SERVER_ID, USERNAME, "
				+ "PASSWORD, PASSWORD_CIPHER, CONNECTION_PORT, CONNECTION_TIMEOUT, COMMENT, PARAMETERS from SERVERPROFILES ";
		
		if (!selectionValue.isEmpty()  ) {			
			sql += "  where " + selectionCol + " like :selectionValue ";
		} 
		sql += " order by SERVER_PROFILE_NAME ";		
		
		MapSqlParameterSource sqlparameters = new MapSqlParameterSource()
				.addValue("selectionValue", selectionValue);

//		System.out.println(" findServerProfiles : " + sql + Mark59Utils.prettyPrintMap(sqlparameters.getValues()));
		List<ServerProfile> serversList = new ArrayList<>();
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, sqlparameters);
		
		for (Map<String, Object> row : rows) {
			ServerProfile server = new ServerProfile();
			server.setServerProfileName((String)row.get("SERVER_PROFILE_NAME")); 
			server.setExecutor((String)row.get("EXECUTOR")); 			
			server.setServer((String)row.get("SERVER")); 
			server.setAlternativeServerId((String)row.get("ALTERNATE_SERVER_ID")); 		
			server.setUsername((String)row.get("USERNAME")); 		
			server.setPassword((String)row.get("PASSWORD")); 		
			server.setPasswordCipher((String)row.get("PASSWORD_CIPHER")); 		
			server.setConnectionPort((String)row.get("CONNECTION_PORT")); 		
			server.setConnectionTimeout((String)row.get("CONNECTION_TIMEOUT")); 
			server.setComment((String)row.get("COMMENT")); 		
			server.setParameters(deserializeJsonToMap((String)row.get("PARAMETERS"))); 					
			serversList.add(server);
		}	
		return  serversList;
	}
	
	
	@Override
	public void insertServerProfile(ServerProfile serverProfile) {
		
		String sql = "INSERT INTO SERVERPROFILES (SERVER_PROFILE_NAME, EXECUTOR, SERVER, ALTERNATE_SERVER_ID, USERNAME, "
				+ "PASSWORD, PASSWORD_CIPHER, CONNECTION_PORT, CONNECTION_TIMEOUT, COMMENT, PARAMETERS ) " 
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.update(sql,
				serverProfile.getServerProfileName(),
				serverProfile.getExecutor(),
				serverProfile.getServer(),
				serverProfile.getAlternativeServerId(),
				serverProfile.getUsername(),
				serverProfile.getPassword(),
				serverProfile.getPasswordCipher(),
				serverProfile.getConnectionPort(),
				serverProfile.getConnectionTimeout(),
				serverProfile.getComment(),
				serializeMapToJson(serverProfile.getParameters()));
	}
	
	
	@Override
	public void updateServerProfile(ServerProfile serverProfile){

		String sql = "UPDATE SERVERPROFILES set SERVER = ?, EXECUTOR = ?, ALTERNATE_SERVER_ID = ?, USERNAME = ?, "
				+ "PASSWORD = ?, PASSWORD_CIPHER = ?, CONNECTION_PORT = ?, CONNECTION_TIMEOUT = ?, COMMENT = ?, PARAMETERS = ? "
				+ "where SERVER_PROFILE_NAME = ? ";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		jdbcTemplate.update(sql,
				serverProfile.getServer(),
				serverProfile.getExecutor(),
				serverProfile.getAlternativeServerId(),
				serverProfile.getUsername(),
				serverProfile.getPassword(),
				serverProfile.getPasswordCipher(),
				serverProfile.getConnectionPort(),
				serverProfile.getConnectionTimeout(),
				serverProfile.getComment(),
				serializeMapToJson(serverProfile.getParameters()),
				serverProfile.getServerProfileName());
	}	
	
	
	@Override
	public void deleteServerProfile(String serverProfileName) {
		
		String sql = "delete from SERVERCOMMANDLINKS where SERVER_PROFILE_NAME = :serverProfileName ";
		
		MapSqlParameterSource sqlparameters = new MapSqlParameterSource()
				.addValue("serverProfileName", serverProfileName);		

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		jdbcTemplate.update(sql, sqlparameters);
		
		sql = "delete from SERVERPROFILES where SERVER_PROFILE_NAME = :serverProfileName ";
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		jdbcTemplate.update(sql, sqlparameters);
	}	

}

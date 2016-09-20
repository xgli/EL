/**
 * 
 */
package edu.li.other;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Aug 8, 2016 11:55:52 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Aug 8, 2016 11:55:52 AM
 */
public class mysql {
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "spa" + File.separator + "df" + File.separator;
	
	public static void GetConnection() throws IOException, SQLException{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("load driver ok");
		}catch(ClassNotFoundException e){
			System.out.println("not found driver");
			e.printStackTrace();
		}
		String url = "jdbc:mysql://10.110.6.43:3306/kbp2016";
		Connection connection;
			String doc_id="";
			String doc_text="";
			connection = (Connection) DriverManager.getConnection(url,"cikuu","cikuutest!");
			Statement statement = (Statement) connection.createStatement();
			System.out.println("connect ok");
			File dir = new File(DFFILEINPUTDIR);
			File[] files = dir.listFiles();
			for(File file : files){
				String fileName = file.getName();
				System.out.println(fileName);
				doc_id = fileName.replace(".xml", "");
				doc_text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName).replace("'", " ");
				String lan = "spa";
				String type = "df";
				String sql = "replace into raw_spa(doc_id,text,lan,type) values('" + doc_id + "','"  + doc_text +  "','" + lan + "','" + type +"')";
				try {
					statement.execute(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}			
			

			statement.close();
			connection.close();
	}
	
	public static void main(String[] args) throws SQLException{
		try {
			GetConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	

}

/**
 * 
 */
package edu.li.nerServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.stanford.nlp.io.EncodingPrintWriter;

/**
 *date:Jun 12, 2016 9:18:46 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 12, 2016 9:18:46 AM
 */
public class nerClient {

		    public static void communicateWithNERServer(String host, int port, 
		                                                String charset, 
		                                                BufferedReader input,
		                                                BufferedWriter output,
		                                                boolean closeOnBlank)
		      throws IOException 
		    {
		      if (host == null) {
		        host = "localhost";
		      }
	          String re = "";
		      for (String userInput; (userInput = input.readLine()) != null; ) {
		        if (userInput.matches("\\n?")) {
		          if (closeOnBlank) {
		            break;
		          } else {
		            continue;
		          }
		        }
		        try {
		          // TODO: why not keep the same socket for multiple lines?
		          Socket socket = new Socket(host, port);
		          PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), charset), true);
		          BufferedReader in = new BufferedReader(new InputStreamReader(
		                  socket.getInputStream(), charset));
		          // send material to NER to socket
		          out.println(userInput);
		          // Print the results of NER
		          String result;

		          while ((result = in.readLine()) != null) {
		            if (output == null) {
		              EncodingPrintWriter.out.println(result, charset);
		            } else {
		              output.write(result);
		              output.newLine();
		            }
		          }		        
		          in.close();
		          socket.close();
//		          return re;
		        } catch (UnknownHostException e) {
		          System.err.print("Cannot find host: ");
		          System.err.println(host);	
//		          return "";
		        } catch (IOException e) {
		          System.err.print("I/O error in the connection to: ");
		          System.err.println(host);	
		        }
		      }
		    
		  } // end static class NERClient
}

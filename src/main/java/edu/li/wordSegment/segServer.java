package edu.li.wordSegment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.util.StringUtils;

public class segServer {
	
	private boolean DEBUG = false;
	private final String charset;
	private final ServerSocket listener;
	
	public segServer(int port) throws IOException{
		listener = new ServerSocket(port);
		this.charset = "utf-8";
	}
	
	public void run(){
	    Socket client = null;
	    while (true) {
	      try {
	        client = listener.accept();
	        if (DEBUG) {
	          System.err.print("Accepted request from ");
	          System.err.println(client.getInetAddress().getHostName());
	        }
	        new Session(client);
	      } catch (Exception e1) {
	        System.err.println("NERServer: couldn't accept");
	        e1.printStackTrace(System.err);
	        try {
	          client.close();
	        } catch (Exception e2) {
	          System.err.println("NERServer: couldn't close client");
	          e2.printStackTrace(System.err);
	        }
	      }
	    }
	}
	
	private class Session extends Thread{
		private final Socket client;
		private final BufferedReader in;
		private PrintWriter out;
		
		private Session(Socket socket) throws UnsupportedEncodingException, IOException {
			client = socket;
			in = new BufferedReader(new InputStreamReader(client.getInputStream(),charset));
			out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(),charset));
			start();			
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void run() {
			
		      if (DEBUG) {System.err.println("Created new session");}
		      String input = null;
		      try {
		        // TODO: why not allow for multiple lines of input?
		        input = in.readLine();
		        if (DEBUG) {
		          EncodingPrintWriter.err.println("Receiving: \"" + input + '\"', charset);
		        }
		      } catch (IOException e) {
		        System.err.println("segServer:Session: couldn't read input");
		        e.printStackTrace(System.err);
		      } catch (NullPointerException npe) {
		        System.err.println("SegServer:Session: connection closed by peer");
		        npe.printStackTrace(System.err);
		      }
		      try {
		        if (! (input == null)) {
		    	 List<Term> terms = null;
		          terms = NlpAnalysis.parse(input);
		          String output = terms.toString();		            
		          
		          if (DEBUG) {
		            EncodingPrintWriter.err.println("Sending: \"" + output + '\"', charset);
		          }
		          out.print(output);
		          out.flush();
		        }
		      } catch (RuntimeException e) {
		        // ah well, guess they won't be hearing back from us after all
		      }
		      close();
		    }
			
		private void close(){
	      try {
	          in.close();
	          out.close();
	          client.close();
	        } catch (Exception e) {
	          System.err.println("segServer:Session: can't close session");
	          e.printStackTrace(System.err);
	        }
		}

	}
	
	  public static class segClient {

		    private segClient() {}

		    public static void communicateWithsegServer(String host, int port,
		                                                String charset) 
		      throws IOException
		    {
		      System.out.println("Input some text and press RETURN to segment.");

		      BufferedReader stdIn = 
		        new BufferedReader(new InputStreamReader(System.in, charset));
		      communicateWithsegServer(host, port, charset, stdIn, null, true);
		      stdIn.close();
		    }

		    public static void communicateWithsegServer(String host, int port, 
		                                                String charset, 
		                                                BufferedReader input,
		                                                BufferedWriter output,
		                                                boolean closeOnBlank)
		      throws IOException 
		    {
		      if (host == null) {
		        host = "localhost";
		      }

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
		        } catch (UnknownHostException e) {
		          System.err.print("Cannot find host: ");
		          System.err.println(host);
		          return;
		        } catch (IOException e) {
		          System.err.print("I/O error in the connection to: ");
		          System.err.println(host);
		          return;
		        }
		      }
		    }
		  } // end static class NERClient
	
	
	
	public static void main(String[] args) throws IOException {
		 Properties props = StringUtils.argsToProperties(args);
		 String client = props.getProperty("client");
		 int port = Integer.parseInt(props.getProperty("port", "4465"));
		// TODO Auto-generated method stub
		NlpAnalysis.parse("nihao");
	    if (client != null && ! client.equals("")) {
	        // run a test client for illustration/testing
	        String host = props.getProperty("host");
	        segClient.communicateWithsegServer(host, port,"utf-8");
	      } else {
	        new segServer(port).run();
	      }
////		segClient.communicateWithsegServer("10.103.29.154", 4465,"utf-8");
//		 String str = "北京是一个城市。";
//	  	 StringReader stringReader = new StringReader(str);
//		 BufferedReader reader = new BufferedReader(stringReader);
////		 BufferedReader reader = new BufferedReader(new FileReader("text.txt"));    	 
//		 BufferedWriter writer = new BufferedWriter(new FileWriter("NerResult.txt"));
//		 segClient.communicateWithsegServer("10.103.29.154", 4465 ,"utf-8",reader,writer,true); 
//		 reader.close();
//		 writer.close();  	
//	   	
	}

}

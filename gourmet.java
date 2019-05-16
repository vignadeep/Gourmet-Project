package gourmet_pkg;

import java.io.IOException;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Servlet to handle the Get request from Index.html
public class gourmet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();					//response to the Index.html is created. everything that is printed in out is passed to client.
		  out.println("<HTML>");								//HTML start
		  out.println("<HEAD>");
		  out.println("<TITLE>Gourmet Search</TITLE>");
		  out.println("</HEAD>");
		  out.println("<BODY>");
		  
		  String[] paramValues = request.getParameterValues("search_box");			//get parameters from the search box
		  String keywords=String.join(" ",paramValues[0].split(" "));				//join the string array to string
		  keywords= new LinkedHashSet<String>( Arrays.asList(keywords.split("\\s+")) ).toString().replaceAll("[\\[\\],]", ""); // remove any duplicate keywords
		  ProcessBuilder processBuilder = new ProcessBuilder("sh","/usr/share/tomcat8/webapps/gourmet_search.sh",keywords);			
	        processBuilder.redirectErrorStream(true);
	        Process process = processBuilder.start();				//Calling the shell script gourmet_search.sh to search the file with keywords
	        try (BufferedReader processOutputReader = new BufferedReader(
	                new InputStreamReader(process.getInputStream()));)					
	        {
	            String readLine;
	            out.println("<table border=\"1\" cellspacing=\"1\" cellpadding=\"5\" bgcolor=\"lightblue\">");						//table start
	            out.println("<tr><th>PRODUCT-ID</th><th>USER-ID</th><th>NAME</th><th>HELPFULLNESS</th><th>SCORE</th><th>TIME</th><th>SUMMARY</th><th>COMMENTS</th></tr>");
	            while ((readLine = processOutputReader.readLine()) != null)																// read the output from shell script and create rows for each review. Each line is a review.
	            {
	            	readLine=readLine.replaceFirst("product/productId:","<tr><td>");
	            	readLine=readLine.replaceFirst("review/userId:","</td><td>");
	            	readLine=readLine.replaceFirst("review/profileName:","</td><td>");
	            	readLine=readLine.replaceFirst("review/helpfulness:","</td><td>");
	            	readLine=readLine.replaceFirst("review/score:","</td><td>");
	            	readLine=readLine.replaceFirst("review/time:","</td><td>");
	            	readLine=readLine.replaceFirst("review/summary:","</td><td>");
	            	readLine=readLine.replaceFirst("review/text:","</td><td>");												//conversion of data to HTML compatible form
	            	readLine=readLine.replaceAll("<br />"," ");
	            	out.println(readLine + System.lineSeparator());															//printing the selected reviews in HTML
	            	out.println("</tr>");
	            }
	            process.waitFor();
	            out.println("</table>");																						//table end
	        }
	        catch(InterruptedException e)
	        {
	        	out.println("Error in the Script:<br>" + e);
	        }


		  out.println("<br><br><form><button formmethod=\"get\" formaction=\"http://ec2-13-235-8-22.ap-south-1.compute.amazonaws.com:8080/Gourmet/\" name=\"Back_button\" type=\"submit\" value=\"Search Again\" style=\"background-color: lightblue; color:blue; width: 80px; height: 40px;\" > Search Again </button></form><br>");
		  //Back button gets the home page
		  
		  out.println("</BODY>");
		  out.println("</HTML>");				//HTML end
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

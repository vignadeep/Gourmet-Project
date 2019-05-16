# Gourmet-Project
Gourmet.war:
  WAR file contains source code and index page with necessary pakages. Shell script is not included.
  Each file and its purpose is described below. This file is hosted in a AWS EC2 private server.
 
 index.html:
  Starting webpage. input is given here.
  
 gourmet.java:
  Java servlet that handles GET request from the index page and calls bash script. responsible for parsing the next HTML page.
  
  gourmet_search.sh:
    Searches the file foods.txt for keywords and selects what to display. passes the results to gourmet.java.
    
    How to run:
    go to website: http://ec2-13-235-8-22.ap-south-1.compute.amazonaws.com:8080/Gourmet/
    give input and click search.
    Wait for few minutes to get the result.
  

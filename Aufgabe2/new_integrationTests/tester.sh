#! /bin/sh

echo "--- REQUESTING ALL SONGS IN JSON ------------"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsServlet/?all"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING ALL SONGS IN XML ------------"
curl -X GET \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/songsServlet/?all"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING SONG NUMBER 7 IN XML --------"
curl -X GET \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/songsServlet/?songID=7"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- POSTING A SONG IN JSON ------------------"
curl -X POST \
     -H "Content-Type: application/json" \
     -d "@einSong.json" \
     -v "http://localhost:8080/songsServlet/"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING THE NEW SONG NUMBER IN JSON --"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsServlet/?songID=11"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- POSTING ANOTHER SONG IN XML ------------"
curl -X POST \
     -H "Content-Type: application/xml" \
     -d "@einZweiterSong.xml" \
     -v "http://localhost:8080/songsServlet/"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- BAD REQUESTS ---------------"

echo "--- GET WITH NO-ACCEPT ---------------"
curl -X GET \
     -v "http://localhost:8080/songsServlet/?songID=7"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- GET WITH BAD ACCEPT ---------------"
curl -X GET \
     -H "Accept: test/stuff" \
     -v "http://localhost:8080/songsServlet/?songID=7"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- GET WITH NON-EXISTING ID ---------------"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsServlet/?songID=22"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- GET WITH BAD ID ---------------"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsServlet/?songID=notANumber"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- GET WITH NO PARAM ---------------"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsServlet/?NO"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- POSTING A NON-XML LOVE SONG ----"
curl -X POST \
     -H "Content-Type: application/xml" \
     -d "@emptyFile.txt" \
     -v "http://localhost:8080/songsServlet/"
echo = " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- SHUTTING DOWN TOMCAT -----------"
$CATALINA_HOME/bin/shutdown.sh

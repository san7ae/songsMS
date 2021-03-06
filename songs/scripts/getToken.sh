#! /bin/sh

if [ "$#" -ne 1 ]; then
    echo "Illegal number of parameters"
    echo "Usage: ./getToken.sh http://host:port/songsWS-TEAMNAME"
    echo "Example: ./getToken.sh http://localhost:8080/songsWS-teames" 
    exit 1
fi

echo "--- REQUESTING A TOKEN ------------------------"
curl -X POST \
	 -H "Content-Type: application/json" \
     -d '{"userid":"mmuster","password":"pass1234"}' \
     -v "$1/rest/auth"
echo " "

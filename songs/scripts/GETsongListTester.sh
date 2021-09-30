#! /bin/sh
#


if [ "$#" -ne 3 ]; then
    echo "Illegal number of parameters"
    echo "Usage: ./GETsongListTester.sh http://host:port/songsWS-TEAMNAME token songListId"
    echo "Example: ./GETsongListTester.sh http://host:port/songsWS-teames 4c87dubofnfrheesom6t0833qa 2"
    exit 1
fi

echo "--- REQUESTING JSON-SONGLIST $3 WITH TOKEN: ----------"
curl -X GET \
     -H "Authorization: $2" \
     -H "Accept: application/json" \
     -v "$1/rest/songLists/$3"
echo "-------------------------------------------------------"

echo "--- REQUESTING XML-SONGLIST $3 WITH TOKEN: ------------"
curl -X GET \
     -H "Authorization: $2" \
     -H "Accept: application/xml" \
     -v "$1/rest/songLists/$3"
echo "-------------------------------------------------------"

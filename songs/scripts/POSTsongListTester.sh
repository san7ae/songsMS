#! /bin/sh
#

if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters"
    echo "Usage: ./POSTsongListTester.sh http://host:port/songsWS-TEAMNAME token"
    echo "Example: ./POSTsongListTester.sh http://host:port/songsWS-teames 4c87dubofnfrheesom6t0833qa"
    exit 1
fi

echo "--- POSTING A JSON SONGLIST   ---------------------"
curl -X POST \
     -H "Authorization: $2" \
     -H "Content-Type: application/json" \
     -d "@aSongList.json" \
     -v "$1/rest/songLists"
echo "---------------------------------------------------"

echo "--- POSTING A JSON SONGLIST WITH NON-EXISTING SONG -"
curl -X POST \
     -H "Authorization: $2" \
     -H "Content-Type: application/json" \
     -d "@aSongListBad.json" \
     -v "$1/rest/songLists"
echo " "



#! /bin/sh
#


if [ "$#" -ne 3 ]; then
    echo "$#"
    echo "Illegal number of parameters"
    echo "Usage: ./DELETEsongListTester.sh http://host:port/songsWS-TEAMNAME token songListId"
    echo "Example: ./DELETEsongListTester.sh http://host:port/songsWS-teames 4c87dubofnfrheesom6t0833qa 22"
    exit 1
fi

echo "--- DELETING SONGLIST WITHOUT TOKEN SHOULD RETURN 401"
curl -X DELETE \
     -v "$1/rest/songLists/$2"
echo "-----------------------------------------------------"

echo "--- DELETING SONGLIST WITH TOKEN--------"
curl -X DELETE \
     -H "Authorization: $2" \
     -v "$1/rest/songLists/$3"
echo "-----------------------------------------------------"

echo "--- REQUESTING DELETED SONGLIST, SHOULD RETURN 404 --"
curl -X GET \
     -H "Authorization: $2" \
     -H "Accept: application/json" \
     -v "$1/rest/songLists/$3"
echo " "



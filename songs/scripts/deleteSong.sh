#! /bin/sh


if [ "$#" -ne 3 ]; then
    echo "Illegal number of parameters"
    echo "Usage: ./deleteSong.sh http://host:port/songsWS-TEAMNAME token songIdToBeDeleted"
    echo "Example: ./deleteSong.sh http://host:port/songsWS-teames 4c87dubofnfrheesom6t0833qa 17"
    exit 1
fi

echo "--- DELETING SONG $3 WITH TOKEN--------"
curl -X DELETE \
     -H "Authorization: $2" \
     -v "$1/rest/songs/$3"
echo " "
echo "-------------------------------------------------------------------------------------------------"


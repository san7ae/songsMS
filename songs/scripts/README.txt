Belegabgabe 4: 
1) Team teilt Bildschirm und zeigt in der DB die SongListen für mmuster und eschuler, mit id und private-flag 

2) http://localhost:8080/songsWS-TEAMNAME/rest/version

3) Scripts:
./getToken.sh http://localhost:8080/songsWS-TEAMNAME  
	* Token für mmuster holen, Token, der zurueckgeschickt wird, kopieren

./GETALLsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token mmuster 
	* TESTING songLists-endpoint WITHOUT TOKEN SHOULD RETURN 401
	* get all songsList for mmuster in XML: all
	* get all songsList for mmuster in JSON: all

./GETALLsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token eschuler
	* get all songsList for eschuler in XML: only public 
	* get all songsList for eschuler in JSON: only public

./GETALLsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token hamwanich
	* get all songsList for hamwanich in XML: 404
	* get all songsList for hamwanich in JSON: 404

./GETsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token mmusterPrivatId
./GETsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token mmusterPublicId
./GETsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token eschulerPrivatId
./GETsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token eschulerPublicId
	* get a songList in XML
	* get a songList in JSON

./POSTsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token
	* post a JSON songList: aSongList.json
	* post a songList in JSON with a non-existing Song: aSongListBad.json

./DELETEsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token newSongListID
./DELETEsongListTester.sh http://localhost:8080/songsWS-TEAMNAME token eschulerPublicId 
	

Pick a song from mmustersPrivatId-Songlist
./deleteSong.sh http://localhost:8080/songsWS-TEAMNAME token songIdToBeDeleted"



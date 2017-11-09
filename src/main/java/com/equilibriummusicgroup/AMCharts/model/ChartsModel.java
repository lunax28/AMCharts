package com.equilibriummusicgroup.AMCharts.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ChartsModel {

    private JsonQueryUtils jsonQueryUtils = new JsonQueryUtils();
    private String nextLink;

    public ChartsModel() {
    }


    public StringBuilder getAlbumCharts(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {

        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        //helper variable to store the numerical position of each album in the list
        int position = 1;

        //this variable limits the search for additional pages if the "next" field
        //is absent. Otherwise it is set to 6.
        int loopLimit = 1;

        //for loop to retrieve 6 pages of charts. Look at the AM API doc
        for(int x = 0; x < loopLimit; x++) {

            JsonObject jsonResponse = jsonQueryUtils.getJson(link);

            System.out.println("JSON RESPONSE: " + jsonResponse.toString());

            JsonObject results = jsonResponse.get("results").getAsJsonObject();

            JsonArray albums = results.get("albums").getAsJsonArray();

            JsonObject data = albums.get(0).getAsJsonObject();

            if (this.checkNode(data, "next")) {
                loopLimit = 6;
                this.nextLink = data.get("next").getAsString();
            }

            System.out.println("this.nextLink: " + this.nextLink);

            JsonArray dataArray = data.get("data").getAsJsonArray();

            int sizeDataArray = dataArray.size();
            System.out.println("size data array: " + sizeDataArray);

            //after reaching the correct field we loop over each album to retrieve the info
            for (int i = 0; i < sizeDataArray; i++) {

                JsonObject firstResult = dataArray.get(i).getAsJsonObject();

                JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

                JsonObject artwork = attributes.get("artwork").getAsJsonObject();

                String artworkUrl = "";

                //checking whether the json field exists. If not, an error string is inserted
                if (this.checkNode(artwork, "url")) {
                    artworkUrl = artwork.get("url").getAsString();
                } else {
                    artworkUrl = "NOT FOUND";
                }

                //replacing {w} and {h} with the respective width and height
                Integer width = artwork.get("width").getAsInt();
                String coverUrl = artworkUrl.replaceAll("\\{w\\}", width.toString()).replaceAll("\\{h\\}", width.toString());
                System.out.println("COVER URL: " + coverUrl);

                String albumName = "";
                if (this.checkNode(attributes, "name")) {
                    albumName = attributes.get("name").getAsString();
                } else {
                    albumName = "NOT FOUND";
                }

                String artistName = "";
                if (this.checkNode(attributes, "artistName")) {
                    artistName = attributes.get("artistName").getAsString();
                } else {
                    artistName = "NOT FOUND";
                }

                String url = "";
                if (this.checkNode(attributes, "url")) {
                    url = attributes.get("url").getAsString();
                } else {
                    url = "NOT FOUND";
                }

                int trackCount = 0;
                if (this.checkNode(attributes, "trackCount")) {
                    trackCount = attributes.get("trackCount").getAsInt();
                }

                String releaseDate = "";
                if (this.checkNode(attributes, "releaseDate")) {
                    releaseDate = attributes.get("releaseDate").getAsString();
                } else {
                    releaseDate = "NOT FOUND";
                }

                String recordLabel = "";
                if (this.checkNode(attributes, "recordLabel")) {
                    recordLabel = attributes.get("recordLabel").getAsString();
                } else {
                    recordLabel = "NOT FOUND";
                }

                String copyright = "";
                if (this.checkNode(attributes, "copyright")) {
                    copyright = attributes.get("copyright").getAsString();
                } else {
                    copyright = "NOT FOUND";
                }

                //we append all the info retrieved and thus build the chart
                result.append(position + "; ")
                        .append(albumName)
                        .append("; ")
                        .append(artistName)
                        .append("; ")
                        .append(url)
                        .append("; ")
                        .append(trackCount)
                        .append("; ")
                        .append(releaseDate)
                        .append("; ")
                        .append(recordLabel)
                        .append("; ")
                        .append(copyright)
                        .append("; ")
                        .append(coverUrl)
                        .append("\n");

                //incrementing the album position
                position++;
            }
            //after going through all the first page array elements we pass
            //another link containing the second page results. This up to the 6th page.
            StringBuilder next = new StringBuilder("https://api.music.apple.com");
            next.append(this.nextLink);

            link = next.toString();

        }

        return result;
    }

    public StringBuilder getSongsCharts(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {

        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        JsonObject jsonResponse = jsonQueryUtils.getJson(link);

        System.out.println("JSON RESPONSE: " + jsonResponse.toString());

        JsonObject results = jsonResponse.get("results").getAsJsonObject();

        JsonArray songs = results.get("songs").getAsJsonArray();

        JsonObject data = songs.get(0).getAsJsonObject();

        JsonArray dataArray = data.get("data").getAsJsonArray();

        int sizeDataArray = dataArray.size();
        System.out.println("size data array: " + sizeDataArray);

        //after reaching the correct field we loop over each album to retrieve the info
        for (int i = 0; i < sizeDataArray; i++) {

            JsonObject firstResult = dataArray.get(i).getAsJsonObject();

            JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

            String songArtist = attributes.get("name").getAsString();

            result.append(songArtist)
                    .append("\n");





        }




        return result;
    }


    /**
     * Helper method to check whether the json retrieved has the field passed as a parameter the <code>jsonResponse</code> from the <code>com.equilibriummusicgroup.AMCharts.model.JsonQueryUtils</code>class.
     * @param gson the json object retrieved
     * @param key the key against which the check is made
     * @return Boolean
     */
    private Boolean checkNode(JsonObject gson, String key){
        return gson.has(key);
    }


}

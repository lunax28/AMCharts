package com.equilibriummusicgroup.AMCharts.model;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ChartsModel {

    private JsonQueryUtils jsonQueryUtils = new JsonQueryUtils();
    private String nextLink;

    public ChartsModel() {
    }


    public StringBuilder getAlbumCharts(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {

        //Making sure to set nextLink to null for a new query!!
        this.nextLink= null;

        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        //helper variable to store the numerical position of each album in the list
        int position = 1;

        //this variable limits the search for additional pages if the "next" field
        //is absent. Otherwise it is set to 6.
        int loopLimit = 1;

        //for loop to retrieve 6 pages of charts. Look at the AM API doc
        for (int x = 0; x < loopLimit; x++) {

            JsonObject jsonResponse = jsonQueryUtils.getJson(link);

            System.out.println("Json Response in ChartsModel: " + jsonResponse.toString());

            JsonObject results = jsonResponse.get("results").getAsJsonObject();

            JsonArray albums = results.get("albums").getAsJsonArray();

            JsonObject data = albums.get(0).getAsJsonObject();

            if (this.checkNode(data, "next")) {
                System.out.println("LINE 42 data has next!");
                loopLimit = 3;
                this.nextLink = data.get("next").getAsString();
            } else {
                System.out.println("LINE 46 data has NOT next!");
                this.nextLink= null;
            }

            System.out.println("LINE 49 this.nextLink: " + this.nextLink);

            JsonArray dataArray = data.get("data").getAsJsonArray();

            int sizeDataArray = dataArray.size();
            System.out.println("size data array: " + sizeDataArray);

            //after reaching the correct field we loop over each album to retrieve the info
            for (int i = 0; i < sizeDataArray; i++) {

                JsonObject firstResult = dataArray.get(i).getAsJsonObject();

                JsonObject attributes;

                //checking whether the json field exists. If not, an error string is inserted
                if (this.checkNode(firstResult, "attributes")) {
                    attributes = firstResult.get("attributes").getAsJsonObject();
                } else {
                    continue;
                }

                //JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

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
                //System.out.println("COVER URL: " + coverUrl);

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


            if (this.nextLink == null){
                System.out.println("LINE 157 There's NO nextLink! Break Loop!");
                break;
            }
            //after going through all the first page array elements we pass
            //another link containing the second page results. This up to the 6th page.
            StringBuilder next = new StringBuilder("https://api.music.apple.com");
            next.append(this.nextLink);

            link = next.toString();
            System.out.println("NEXT LINK LINE 155: " + link);
            System.out.println("LINE 170 END OF PAGE\n");

        }

        System.out.println("\n###END OF LOOP###\n");

        return result;
    }

    public StringBuilder getSongsCharts(String link, int limit, String countryValue, String genreValue) throws InvalidKeySpecException, NoSuchAlgorithmException {

        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        int offset = 50;

        String nextEndpoint = "";

        JsonObject jsonResponse;
        JsonObject beforeData;

        //for loop to retrieve 6 pages of charts. Look at the AM API doc
        for (int x = 0; x < limit; x++) {

            //System.out.println("LINE 172 link: " + nextLink);
            if (nextEndpoint.isEmpty()) {
                jsonResponse = jsonQueryUtils.getJson(link);

            } else {
                jsonResponse = jsonQueryUtils.getJson(nextLink);
            }


            System.out.println("ChartsModel JSON RESPONSE: " + jsonResponse.toString());

            JsonObject results = jsonResponse.get("results").getAsJsonObject();

            JsonArray songs = results.get("songs").getAsJsonArray();

            beforeData = songs.get(0).getAsJsonObject();

            if (!this.checkNode(beforeData, "data")) {
                System.out.println("NO DATA IN beforeData!!!!!!!!");
                break;
            }

            JsonArray dataArray = beforeData.get("data").getAsJsonArray();


            int sizeDataArray = dataArray.size();
            System.out.println("ChartsModel size data array: " + sizeDataArray);

            //after reaching the correct field we loop over each album to retrieve the info
            for (int i = 0; i < sizeDataArray; i++) {

                JsonObject firstResult = dataArray.get(i).getAsJsonObject();

                String id = "";
                if (this.checkNode(firstResult, "id")) {
                    id = firstResult.get("id").getAsString();
                } else {
                    id = "NOT FOUND";
                }

                if (!this.checkNode(firstResult, "attributes")) {
                    System.out.println("ChartsModel NO ATTRIBUTES IN firstResult!!!!!!!!");
                    continue;
                }

                JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

                String isrc = "";
                if (this.checkNode(attributes, "isrc")) {
                    isrc = attributes.get("isrc").getAsString();
                } else {
                    isrc = "NOT FOUND";
                }

/*                if (isrc.substring(0, 3).equals("ITO")) {
                    int isrcInt = 0;
                    try {
                        isrcInt = Integer.parseInt(isrc.substring(3, isrc.length()));
                        System.out.println("LINE 238 isrcInt: " + isrcInt);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    if (isrcInt > 101100000 && isrcInt < 101900000) {
                        continue;
                    }
                }*/

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

                String relDate = "";
                if (this.checkNode(attributes, "releaseDate")) {
                    relDate = attributes.get("releaseDate").getAsString();
                } else {
                    relDate = "NOT FOUND";
                }

                String songName = "";
                if (this.checkNode(attributes, "name")) {
                    songName = attributes.get("name").getAsString();
                } else {
                    songName = "NOT FOUND";
                }

                result.append(id)
                        .append("; ")
                        .append(songName)
                        .append("; ")
                        .append(relDate)
                        .append("; ")
                        .append(isrc)
                        .append("; ")
                        .append(artistName)
                        .append("; ")
                        .append(url)
                        .append("\n");

            }

            if (this.checkNode(beforeData, "next")) {
                if (beforeData.get("next").getAsString().isEmpty()) {
                    System.out.println("ChartsModel ATTENTION! NEXT FIELD IS EMPTY");
                    break;
                }

                nextEndpoint = beforeData.get("next").getAsString();
                System.out.println("####ChartsModel nextEndpoint: " + nextEndpoint);
                //after going through all the first page array elements we pass
                //another link containing the second page results. This up to the 6th page.
                StringBuilder next = new StringBuilder("https://api.music.apple.com");
                next.append(nextEndpoint);

                nextLink = next.toString();
            } else {
                /*offset = offset + 20;
                nextEndpoint = "/v1/catalog/" + countryValue + "/charts?chart=most-played&genre=" + genreValue + "&offset=" + offset + "&types=songs";
                StringBuilder next = new StringBuilder("https://api.music.apple.com");
                next.append(nextEndpoint);
                nextLink = next.toString();*/
                System.out.println("ChartsModel ATTENTION!!! NO NEXT FIELD!");
                break;

            }


        }

        return result;
    }


    public StringBuilder getAllSongsCharts(String link, int limit, String countryValue, String genre) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        //this variable limits the search for additional pages if the "next" field
        //is absent. Otherwise it is set to 6.
        int loopLimit = 500;

        int offset = 490;

        String nextEndpoint = "";

        JsonObject jsonResponse;
        JsonObject beforeData;


        for (int x = 0; x < limit; x++) {

            //System.out.println("LINE 172 link: " + nextLink);
            if (nextEndpoint.isEmpty()) {
                jsonResponse = jsonQueryUtils.getJson(link);

            } else {
                jsonResponse = jsonQueryUtils.getJson(nextLink);
            }


            System.out.println("JSON RESPONSE: " + jsonResponse.toString());

            JsonObject results = jsonResponse.get("results").getAsJsonObject();

            JsonArray songs = results.get("songs").getAsJsonArray();

            beforeData = songs.get(0).getAsJsonObject();

            if (!this.checkNode(beforeData, "data")) {
                System.out.println("NO DATA IN beforeData!!!!!!!!");
                break;
            }

            JsonArray dataArray = beforeData.get("data").getAsJsonArray();


            int sizeDataArray = dataArray.size();
            System.out.println("size data array: " + sizeDataArray);

            //after reaching the correct field we loop over each album to retrieve the info
            for (int i = 0; i < sizeDataArray; i++) {

                JsonObject firstResult = dataArray.get(i).getAsJsonObject();

                String id = "";
                if (this.checkNode(firstResult, "id")) {
                    id = firstResult.get("id").getAsString();
                } else {
                    id = "NOT FOUND";
                }

                if (!this.checkNode(firstResult, "attributes")) {
                    System.out.println("NO ATTRIBUTES IN firstResult!!!!!!!!");
                    continue;
                }

                JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

                String isrc = "";
                if (this.checkNode(attributes, "isrc")) {
                    isrc = attributes.get("isrc").getAsString();
                } else {
                    isrc = "NOT FOUND";
                }

                if (isrc.substring(0, 3).equals("ITO")) {
                    int isrcInt = 0;
                    try {
                        isrcInt = Integer.parseInt(isrc.substring(3, isrc.length()));
                        System.out.println("LINE 238 isrcInt: " + isrcInt);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    if (isrcInt > 101100000 && isrcInt < 101900000) {
                        continue;
                    }
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

                String relDate = "";
                if (this.checkNode(attributes, "releaseDate")) {
                    relDate = attributes.get("releaseDate").getAsString();
                } else {
                    relDate = "NOT FOUND";
                }

                String songName = "";
                if (this.checkNode(attributes, "name")) {
                    songName = attributes.get("name").getAsString();
                } else {
                    songName = "NOT FOUND";
                }

                result.append(genre)
                        .append("; ")
                        .append(id)
                        .append("; ")
                        .append(songName)
                        .append("; ")
                        .append(relDate)
                        .append("; ")
                        .append(isrc)
                        .append("; ")
                        .append(artistName)
                        .append("; ")
                        .append(url)
                        .append("\n");

            }

            if (this.checkNode(beforeData, "next")) {
                if (beforeData.get("next").getAsString().isEmpty()) {
                    System.out.println("ATTENTION! NEXT FIELD IS EMPTY");
                    break;
                }
                nextEndpoint = beforeData.get("next").getAsString();
                System.out.println("nextEndpoint line 454: " + nextEndpoint);
                //after going through all the first page array elements we pass
                //another link containing the second page results. This up to the 6th page.
                StringBuilder next = new StringBuilder("https://api.music.apple.com");
                next.append(nextEndpoint);

                nextLink = next.toString();
            } else {
                offset = offset + 20;
                nextEndpoint = "/v1/catalog/" + countryValue + "/charts?chart=most-played&genre=" + genre + "&offset=" + offset + "&types=songs";
                StringBuilder next = new StringBuilder("https://api.music.apple.com");
                next.append(nextEndpoint);
                nextLink = next.toString();
                System.out.println("ATTENTION!!! NO NEXT FIELD!");

            }


        }

        return result;


    }

    /**
     * Helper method to check whether the json retrieved has the field passed as a parameter the <code>jsonResponse</code> from the <code>com.equilibriummusicgroup.AMCharts.model.JsonQueryUtils</code>class.
     *
     * @param gson the json object retrieved
     * @param key  the key against which the check is made
     * @return Boolean
     */
    private Boolean checkNode(JsonObject gson, String key) {
        return gson.has(key);
    }


    public StringBuilder getKeywordsHints(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {

        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        JsonObject jsonResponse = jsonQueryUtils.getJson(link);

        System.out.println("Json Response in ChartsModel: " + jsonResponse.toString());

        JsonObject results = jsonResponse.get("results").getAsJsonObject();

        JsonArray termsArray = results.get("terms").getAsJsonArray();

        System.out.println("terms: " + termsArray.toString());

        int termsArraySize = termsArray.size();

        for (int i = 0; i < termsArraySize; i++) {

            result.append(termsArray.get(i).getAsString());
            result.append("\n");

        }

        return result;


    }
}

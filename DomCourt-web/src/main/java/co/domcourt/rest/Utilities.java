/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package co.domcourt.rest;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**

 @author 1010199039
 */
public class Utilities
{
    public static JsonObject getJsonObject( String jsonString )
    {
        JsonObject jsonObject = null;
        try( JsonReader jsonReader = Json.createReader( new StringReader( jsonString ) ) )
        {
            jsonObject = jsonReader.readObject();
        }

        return jsonObject;
    }
}

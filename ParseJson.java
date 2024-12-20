package com.example.testcarte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParseJson {

    public static List<Carte> parseJson(String s)
    {
        try
        {
            JSONObject root = new JSONObject(s);
            JSONObject details = root.getJSONObject("details");
            JSONArray datasets = details.getJSONArray("datasets");
            List<Carte> carti = new ArrayList<>();

            for(int i =0; i<datasets.length(); i++)
            {
                JSONObject obj = datasets.getJSONObject(i);
                JSONObject carte = obj.getJSONObject("carte");

                String name = carte.getString("name");
                int nbPages = carte.getInt("nbPages");
                Date date = DateConverter.toDate(carte.getString("publishDate"));
                Carte cartes = new Carte(name, nbPages, date);
                carti.add(cartes);
            }

            return carti;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

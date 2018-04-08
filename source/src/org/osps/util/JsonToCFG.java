package org.osps.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonToCFG {
	
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
        //try(Reader reader = new InputStreamReader(JsonToCFG.class.getResourceAsStream("Output.json"), "UTF-8")){
		try {
			BufferedReader reader = new BufferedReader(  
                    new FileReader("Output.json"));
            Gson gson = new GsonBuilder().create();

            ArrayList<Revenant> revList = gson.fromJson(reader, new TypeToken<ArrayList<Revenant>>(){}.getType());
            for (Revenant rev : revList) {
            	System.out.print("spawn\t=\t");
            	System.out.print(rev.getMobId() + "\t");
            	System.out.print(rev.getPosition().getX() + "\t");
            	System.out.print(rev.getPosition().getY() + "\t");
            	System.out.print(rev.getPosition().getZ() + "\t");
            	if (rev.getRoamTile() != 0) {
            		System.out.print("1\t");
            	} else {
            		System.out.print("0\t");
            	}
            	System.out.print("0\t0\t0\t");
            	System.out.println(rev.getMobName());
            	
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

}

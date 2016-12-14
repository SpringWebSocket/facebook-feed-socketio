/*package com.kshrd.saveobjectandlisttopreference;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Put Object to Preference
        Person p = new Person(1, "John", 18);
        String personJson = new Gson().toJson(p);
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("person", personJson);
        editor.apply();

        // Retrieve Object from Preference
        String result = getPreferences(MODE_PRIVATE).getString("person", "");
        Gson gson = new Gson();
        Person p1 = gson.fromJson(result, Person.class);
        Log.e("ooooo", p1.getName());


        *//***************************************************************************//*

        // Put List to Preference
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person(1, "Dara", 20));
        people.add(new Person(2, "Coco", 35));
        String personList = new Gson().toJson(people);
        editor.putString("people", personList);
        editor.apply();

        // Retrieve List from Preference
        String res = getPreferences(MODE_PRIVATE).getString("people", "");
        // use this line when working with List of Object
        Type type = new TypeToken<List<Person>>(){}.getType();
        List<Person> p2 = new Gson().fromJson(res, type);
        for (Person temp: p2){
            Log.e("ooooo", temp.getName());
        }
    }
}*/
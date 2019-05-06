//package com.test.faceid;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
////import android.support.design.widget.FloatingActionButton;
////import android.support.design.widget.NavigationView;
////import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//
//
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.view.MenuItem;
//import android.widget.ListView;
//import android.support.v7.widget.Toolbar;
//
//import java.util.ArrayList;
//
//public class Section extends AppCompatActivity implements View.OnClickListener{
//    private TextView title2 = null;
//    private Button btn_classA, btn_classB;
//
//    String professor = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_section);
//
//        title2 = (TextView) findViewById(R.id.title2) ;
//        btn_classA = (Button) findViewById(R.id.btn_classA) ;
//        btn_classB = (Button) findViewById(R.id.btn_classB) ;
//        btn_classA.setOnClickListener(this);
//        btn_classB.setOnClickListener(this);
//
//        Bundle bundle = getIntent().getExtras();
//        professor = bundle.getString("professor");
//        title2.setText("Welcome " + professor);
//
//    }
//
//    public void onClick(View v){
//        Intent it = new Intent(Section.this, select_mode.class);
//        Bundle bd = new Bundle();
//        switch (v.getId()) {
//            case R.id.btn_classA:
//
//                bd.putString("professor", professor);
//                bd.putString("className", "classA");
//                it.putExtras(bd);
//                startActivity(it);
//
//                break;
//
//            case R.id.btn_classB:
//
//                bd.putString("professor", professor);
//                bd.putString("className", "classB");
//                it.putExtras(bd);
//                startActivity(it);
//
//                break;
//
//
//        }
//    }
//}


package com.test.faceid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;


public class Section extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    Button b1;
//    Button b2;
//    Button b3;
//    Button b4;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;
    private NavigationView navigationView;
    //    private Fragment fragment;
//    private FragmentTransaction fragmentTransaction;
    private ArrayList<String> arrayList;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);


        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=(ListView)findViewById(R.id.listview);

        arrayList=new ArrayList<>();

        arrayList.add("Section 1");
        arrayList.add("Section 2");
        arrayList.add("Section 3");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Section.this, select_mode.class);
                intent.putExtra("professor", "");
                intent.putExtra("className", "");
                startActivity(intent);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                arrayList.add("Section " + (arrayList.size() + 1));
                ArrayAdapter arrayAdapter=new ArrayAdapter(Section.this,android.R.layout.simple_list_item_1, arrayList);

                listView.setAdapter(arrayAdapter);

            }
        });

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        Log.i("MenuItem", "InNavigationdrawer");
        int id = item.getItemId();
        if (id == R.id.nav_login) {
            startActivity(new Intent(this, login.class));
            this.finish();

//            Intent navLogin = new Intent(Section.this, login.class);
//            Section.this.startActivity(navLogin);
//            return true;
        }

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, Camera.class));

//            Intent navCamera = new Intent(Section.this, Camera.class);
//            Section.this.startActivity(navCamera);
//            return true;
        }
        return  false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}




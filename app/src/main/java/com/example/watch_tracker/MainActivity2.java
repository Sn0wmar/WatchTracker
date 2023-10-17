package com.example.watch_tracker;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView mask = findViewById(R.id.masquer);
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(it2);
            }
        });
    }
}
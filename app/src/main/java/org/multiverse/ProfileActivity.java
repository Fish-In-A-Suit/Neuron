package org.multiverse;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.multiverse.multiversetools.Test;

public class ProfileActivity extends AppCompatActivity {
    Button test;
    EditText text;
    TextView result;
    FirebaseDatabase fd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        test = findViewById(R.id.test);
        text = findViewById(R.id.editText);
        result = findViewById(R.id.result);

        fd = FirebaseDatabase.getInstance();
        final DatabaseReference messages = fd.getReference("messages");

        messages.addValueEventListener(new ValueEventListener() {
            //this method gets called every time data is changed
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                result.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Failed to read value!");

            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.setValue(text.getText().toString());
            }
        });
    }
}

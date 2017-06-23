package lu.gab.com.alertapet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import lu.gab.com.alertapet.LoginActivity;
import lu.gab.com.alertapet.MainActivity;
import lu.gab.com.alertapet.R;

public class ProfileActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private DatabaseReference mUserDatabase;
    private FirebaseUser currentUser;

    private CircleImageView userImage;
    private TextView userName;
    private TextView userEmail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        userImage = (CircleImageView) findViewById(R.id.user_profile_settings);

        //user from firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //get the uuid from user
        final String current_uid = currentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        String email = currentUser.getEmail();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //get the values from firebase
                String name =  dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("imageProfile").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //@Override
  //  public void onBackPressed() {
   //     super.onBackPressed();
   ////     Intent startIntent = new Intent(ProfileActivity.this, MainActivity.class);
    //    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
   //     startActivity(startIntent);
   //     finish();
   // }
}
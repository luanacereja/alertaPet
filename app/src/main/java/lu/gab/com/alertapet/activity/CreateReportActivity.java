package lu.gab.com.alertapet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

import lu.gab.com.alertapet.MainActivity;
import lu.gab.com.alertapet.Model.Report;
import lu.gab.com.alertapet.R;
import lu.gab.com.alertapet.SignupActivity;
import lu.gab.com.alertapet.other.CircleTransform;

/**
 * Created by Luana on 27/06/2017.
 */
@SuppressWarnings("VisibleForTests")
public class CreateReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageReport;
    private EditText textPhone, inputDescriprtion;
    private CheckBox checkBoxRua, checkBoxPerdido, checkBoxDoacao;
    private DatabaseReference mUserDatabase, mReportReference;
    private FirebaseUser currentUser;
    private Button saveReportButton ;
    private ProgressDialog progressDialog;
    private String userName,place, phone;
    private ImageButton findPlaceButtom;
    private TextView textPlace;

    //firebase storage
    private StorageReference imageStorage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Crie um report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //user from firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        imageStorage = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        mReportReference = FirebaseDatabase.getInstance().getReference().child("Reports");


        textPhone = (EditText) findViewById(R.id.input_phone);
        inputDescriprtion = (EditText) findViewById(R.id.input_description);

        textPlace = (TextView) findViewById(R.id.text_location);

        checkBoxRua = (CheckBox) findViewById(R.id.checkBoxRua);
        checkBoxPerdido = (CheckBox) findViewById(R.id.checkBoxPerdido);
        checkBoxDoacao = (CheckBox) findViewById(R.id.checkBoxDoacao);

        saveReportButton = (Button) findViewById(R.id.create_report_button);
        findPlaceButtom = (ImageButton) findViewById(R.id.button_place);



        // IMAGE REPORT
        imageReport = (ImageView) findViewById(R.id.image_create_report);
        if(getIntent().hasExtra("imageReport")) {

            Uri imageUri = (Uri) getIntent().getExtras().get("imageReport");

           // byte[] byteArray = getIntent().getByteArrayExtra("imageReport");
          //  Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            Glide.with(CreateReportActivity.this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageReport);
            //imageReport.setImageBitmap(bmp);
        }


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName =  dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // BUTTOM SAVE REPORT
        saveReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validar valores
                final String descritpion = inputDescriprtion.getText().toString();
                String categoria1 = "";
                Uri imageUri = (Uri) getIntent().getExtras().get("imageReport");
                place = textPlace.getText().toString();
                phone = textPhone.getText().toString();

                if ((!checkBoxDoacao.isChecked()) &&(!checkBoxPerdido.isChecked()) && (!checkBoxRua.isChecked()) ){
                    Toast.makeText(getApplicationContext(), "Categoria obrigatória!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (place.equals("Informe o local")){
                    Toast.makeText(getApplicationContext(), "Local obrigatório!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(descritpion)) {
                    Toast.makeText(getApplicationContext(), "Descrição obrigatória!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //dialog sending report
                progressDialog = new ProgressDialog(CreateReportActivity.this);
                progressDialog.setTitle("Criando report ");
                progressDialog.setMessage("Aguarde enquanto o report é enviado...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (checkBoxRua.isChecked()){
                    categoria1 = "De rua";
                }else if (checkBoxPerdido.isChecked()){
                    categoria1 = "Perdido";
                }else if (checkBoxDoacao.isChecked()){
                    categoria1 = "Doacao";
                }

                final String categoria = categoria1;

                //Salvando foto do report
                StorageReference filePath = imageStorage.child("report_images").child( random() + ".jpg");
                filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){

                            final String donwload_url = task.getResult().getDownloadUrl().toString();
                            String place = textPlace.getText().toString();

                                // GET the values
                                Report report = new Report();
                                report.setReportText(descritpion);
                                report.setCategory(categoria);
                                report.setReportDate(System.currentTimeMillis());
                                report.setReportImage(donwload_url);
                                report.setUserName(userName);
                                report.setUserUid(currentUser.getUid());
                                report.setAddress(place);
                                report.setPhone(phone);


                            mReportReference.push().setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                                    progressDialog.dismiss();
                                            Intent mainIntent = new Intent(CreateReportActivity.this, MainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();

                                        }

                                    }
                                });

                        }else{
                            progressDialog.dismiss();
                        }
                    }
                });
                // FIM SALVANDO FOTO
            }
        });

        findPlaceButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace(v);
            }
        });


    }

    // CHECK BOX
    public void onCheckboxClicked(View view) {

        switch(view.getId()) {

            case R.id.checkBoxRua:

                checkBoxPerdido.setChecked(false);
                checkBoxDoacao.setChecked(false);

                break;

            case R.id.checkBoxPerdido:

                checkBoxRua.setChecked(false);
                checkBoxDoacao.setChecked(false);

                break;

            case R.id.checkBoxDoacao:

                checkBoxRua.setChecked(false);
                checkBoxPerdido.setChecked(false);

                break;
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                ((TextView) findViewById(R.id.text_location))
                        .setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar =  (char) (generator.nextInt(96) + 32 );
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

}

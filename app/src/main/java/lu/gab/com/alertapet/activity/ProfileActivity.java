package lu.gab.com.alertapet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import lu.gab.com.alertapet.LoginActivity;
import lu.gab.com.alertapet.MainActivity;
import lu.gab.com.alertapet.R;
import lu.gab.com.alertapet.other.BordderTransformation;
import lu.gab.com.alertapet.other.CircleTransform;

public class ProfileActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private DatabaseReference mUserDatabase;
    private FirebaseUser currentUser;

    private ImageView userImage;
    private TextView userName;
    private TextView userEmail;
    private Button changePhotoButtom, changeNameButtom;
    private ProgressDialog progressDialog;
    private EditText userInputName;

    private AlertDialog alertDialog;




    private static final int GALLERY_PICK = 1;

    //firebase storage
    private StorageReference imageStorage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //user profile photo
        userImage = (ImageView) findViewById(R.id.profile_settings_image);

        //Text view name, email
        userName = (TextView) findViewById(R.id.profile_settings_name);
        userEmail = (TextView) findViewById(R.id.profile_settings_email);

        //butoon
        changePhotoButtom = (Button) findViewById(R.id.profile_settings_buttom);
        changeNameButtom = (Button) findViewById(R.id.button_change_name);

        //user from firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        imageStorage = FirebaseStorage.getInstance().getReference();

        //get the uuid from user
        final String current_uid = currentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        String email = currentUser.getEmail();

        //set the email on emailTextView
        userEmail.setText(email);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //get the values from firebase
                String name =  dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("imageProfile").getValue().toString();
               // String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                //set the name from firebase
                userName.setText(name);

                //set the image from firebase
                if (!image.equals("default")) {

                    Glide.with(ProfileActivity.this)
                            .load(image)
                            .placeholder(R.drawable.ic_user_circle)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(ProfileActivity.this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(userImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Buttom for change IMAGE
        changePhotoButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pic some image from gallery
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Selecione uma foto"),GALLERY_PICK);

                // start picker to get image for cropping and then use the image in cropping activity
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);
                */

            }
        });

        changeNameButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ProfileActivity.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_name_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ProfileActivity.this);
                alertDialogBuilderUserInput.setView(mView);


                userInputName = (EditText) mView.findViewById(R.id.userInputDialog);
                userInputName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);



                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {



                                String newName = userInputName.getText().toString();

                                if (TextUtils.isEmpty(newName)) {

                                    // progressDialog.setMessage("Erro ao alterar nome");
                                    //  Toast.makeText(getApplicationContext(),"Erro ao alterar nome", Toast.LENGTH_LONG).show();
                                    //progressDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), "Campo não pode ficar vazio!", Toast.LENGTH_LONG).show();
                                    return;
                                }


                                alertDialog.dismiss();

                                progressDialog = new ProgressDialog(ProfileActivity.this);
                                progressDialog.setTitle("Salvando mudanças ");
                                progressDialog.setMessage("Aguarde enquanto o nome é alterado...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();



                                mUserDatabase.child("name").setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            //
                                            Runnable progressRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                }
                                            };

                                            Handler pdCanceller = new Handler();
                                            pdCanceller.postDelayed(progressRunnable, 2000);

                                        }else{
                                            progressDialog.setMessage("Erro ao alterar nome");
                                          //  Toast.makeText(getApplicationContext(),"Erro ao alterar nome", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }

                                    }
                                });


                            }
                        })

                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                alertDialog = alertDialogBuilderUserInput.create();
                alertDialog.show();

            }
        });



    }

    @SuppressWarnings("VisibleForTests")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1 )
                    .setMinCropWindowSize(500,500)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setTitle("Enviando foto... ");
                progressDialog.setMessage("Aguarde enquanto a imagem é processada.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Uri resultUri = result.getUri();
                // File thumbnail_filePath = new File(resultUri.getPath());

                String current_user_uid = currentUser.getUid();
              /*  Bitmap thumbn_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumbnail_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbn_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();
*/
                StorageReference filePath = imageStorage.child("profile_images").child(current_user_uid + ".jpg");
                //final StorageReference thumb_file_path = imageStorage.child("profile_images").child("thumb")
                //       .child( current_user_uid + ".jpg"   );


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override

                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            final String donwload_url = task.getResult().getDownloadUrl().toString();

                            mUserDatabase.child("imageProfile").setValue(donwload_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                            //UploadTask uploadTask = thumb_file_path.putBytes(thumb_byte);
                            //  uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            //     @Override
                            //     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                            //       String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                            //    if (thumb_task.isSuccessful()){

                            //        Map updateHashmap = new HashMap();
                            //        updateHashmap.put("imageProfile",donwload_url);
                            //       updateHashmap.put("thumb_image",thumb_downloadUrl);

                            //   mUserDatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //      @Override
                            //      public void onComplete(@NonNull Task<Void> task) {
                            //           if (task.isSuccessful()){
                            //              progressDialog.dismiss();
                            //         }
                            //     }
                            // });
                        } else {
                            Toast.makeText(ProfileActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_LONG);
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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


}

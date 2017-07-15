package lu.gab.com.alertapet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import lu.gab.com.alertapet.R;

/**
 * Created by Lu on 06/07/2017.
 */

public class ReportDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageReport;
    private TextView textPlace, textUser, textTime, textDescription, textPhone;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_details_activity);

        //initializate
        imageReport = (ImageView) findViewById(R.id.report_image_details);
        textUser = (TextView) findViewById(R.id.user_detail_report);
        textTime = (TextView) findViewById(R.id.time_detail_report);
        textPlace = (TextView) findViewById(R.id.locationReport);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textPhone = (TextView) findViewById(R.id.textViewPhone);

        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes Report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (intent.hasExtra("reportImage")){
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("reportImage");
            imageReport.setImageBitmap(bitmap);
        }

        if (intent.hasExtra("reportTime")){
           String time = (String) intent.getExtras().get("reportTime");
            textTime.setText(time);
        }

        if (intent.hasExtra("reportUserName")){
            String userName = (String) intent.getExtras().get("reportUserName");
            textUser.setText(userName);
        }

        if (intent.hasExtra("reportAddress")){
            String reportAddress = (String) intent.getExtras().get("reportAddress");
            textPlace.setText(reportAddress);
        }

        if (intent.hasExtra("reportDescription")){
            String reportDescription = (String) intent.getExtras().get("reportDescription");
            textDescription.setText(reportDescription);
        }

        if (intent.hasExtra("reportPhone")){
            String reportPhone = (String) intent.getExtras().get("reportPhone");
            textPhone.setText(reportPhone);
        }



    }
}

package lu.gab.com.alertapet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lu.gab.com.alertapet.Fragment.LostFragment;
import lu.gab.com.alertapet.Model.Report;
import lu.gab.com.alertapet.R;

/**
 * Created by Lu on 09/07/2017.
 */

public class MyReportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference reportsReference;
    private FirebaseUser currentUser;
    private RecyclerView recyclerListMyReports;
    private Query myReports;
    private LinearLayoutManager mLayoutManager;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Meus Reports");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView
        recyclerListMyReports = (RecyclerView) findViewById(R.id.recycleListMyReoprts);
        recyclerListMyReports.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);


        //user from firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //get the uuid from user
        final String current_uid = currentUser.getUid();

        reportsReference = FirebaseDatabase.getInstance().getReference().child("Reports");
        myReports = reportsReference.orderByChild("userUid").equalTo(current_uid);

    }

    @Override
    public void onStart(){
        super.onStart();
        recyclerListMyReports.setLayoutManager(mLayoutManager);

        FirebaseRecyclerAdapter<Report, ReportsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Report, ReportsViewHolder>(

                Report.class,
                R.layout.custom_row_report,
                ReportsViewHolder.class,
                myReports
        ) {
            @Override
            protected void populateViewHolder(final ReportsViewHolder reportsViewHolder, Report report, int position) {

                DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
                Date date = null;
                date = new Date(report.getReportDate());
                String crdate1 = dateFormatNeeded.format(date);

                reportsViewHolder.reportAddress.setText(report.getAddress());
                reportsViewHolder.userNameView.setText(report.getUserName());
                reportsViewHolder.reportDescription.setText(report.getReportText());
                reportsViewHolder.reportTime.setText(crdate1);
                reportsViewHolder.reporterPhone.setText(report.getPhone());

                Glide.with(MyReportsActivity.this)
                        .load(report.getReportImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(reportsViewHolder.reportImage);

                reportsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent reportDetails = new Intent(MyReportsActivity.this, ReportDetailsActivity.class);
                        reportDetails.putExtra("reportAddress",  reportsViewHolder.reportAddress.getText());
                        reportDetails.putExtra("reportUserName",  reportsViewHolder.userNameView.getText());
                        reportDetails.putExtra("reportDescription",  reportsViewHolder.reportDescription.getText());
                        reportDetails.putExtra("reportTime",  reportsViewHolder.reportTime.getText());
                        reportDetails.putExtra("reportCategory",  reportsViewHolder.reportTime.getText());
                        reportDetails.putExtra("reportPhone",  reportsViewHolder.reporterPhone.getText());


                        reportsViewHolder.reportImage.buildDrawingCache();
                        Bitmap image= reportsViewHolder.reportImage.getDrawingCache();

                        reportDetails.putExtra("reportImage",  image);
                        startActivity(reportDetails);

                    }
                });

            }
        };
        recyclerListMyReports.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder{

        TextView userNameView,reportTime,reportAddress,reportDescription, reporterPhone;
        ImageView reportImage;
        View mView;

        public ReportsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            userNameView = (TextView) itemView.findViewById(R.id.reportUserName);
            reportTime = (TextView) itemView.findViewById(R.id.reportDate);
            reportAddress = (TextView) itemView.findViewById(R.id.reportLocation);
            reportDescription = (TextView) itemView.findViewById(R.id.reportMessage);
            reportImage = (ImageView) itemView.findViewById(R.id.imageReport);
            reporterPhone = (TextView) itemView.findViewById(R.id.textViewPhoneRow);

        }
    }
}

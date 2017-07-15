package lu.gab.com.alertapet.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lu.gab.com.alertapet.Model.Report;
import lu.gab.com.alertapet.R;
import lu.gab.com.alertapet.activity.ReportDetailsActivity;

/**
 * Created by Lu on 31/05/2017.
 */

public class StreetFragment extends Fragment{

    private static RecyclerView reportStreetList;
    private DatabaseReference reportReferece;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<Report, LostFragment.ReportsViewHolder> firebaseRecyclerAdapter;
    private View mRootVIew;
    private Query reportsStreets;


    public StreetFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //iniciate reports reference
        reportReferece = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportsStreets = reportReferece.orderByChild("category").equalTo("De rua");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mRootVIew = inflater.inflate(R.layout.street_fragment, container, false);

        reportStreetList = (RecyclerView) mRootVIew.findViewById(R.id.recycleListStreet);
        reportStreetList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());

        return mRootVIew;

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reportStreetList.setLayoutManager(mLayoutManager);

        FirebaseRecyclerAdapter<Report, LostFragment.ReportsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Report, LostFragment.ReportsViewHolder>(

                Report.class,
                R.layout.custom_row_report,
                LostFragment.ReportsViewHolder.class,
                reportsStreets
        ) {
            @Override
            protected void populateViewHolder(final LostFragment.ReportsViewHolder reportsViewHolder, Report report, int position) {

                DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
                Date date = null;
                date = new Date(report.getReportDate());
                String crdate1 = dateFormatNeeded.format(date);

                reportsViewHolder.reportAddress.setText(report.getAddress());
                reportsViewHolder.userNameView.setText(report.getUserName());
                reportsViewHolder.reportDescription.setText(report.getReportText());
                reportsViewHolder.reportTime.setText(crdate1);
                reportsViewHolder.reporterPhone.setText(report.getPhone());

                Glide.with(getActivity().getApplicationContext())
                        .load(report.getReportImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(reportsViewHolder.reportImage);

                reportsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent reportDetails = new Intent(getActivity(), ReportDetailsActivity.class);
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

        reportStreetList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {

        TextView userNameView, reportTime, reportAddress, reportDescription,reporterPhone;
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

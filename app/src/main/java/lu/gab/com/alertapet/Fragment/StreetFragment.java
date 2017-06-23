package lu.gab.com.alertapet.Fragment;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lu.gab.com.alertapet.R;

/**
 * Created by Lu on 31/05/2017.
 */

public class StreetFragment extends Fragment{

    public StreetFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.street_fragment,container,false);
    }
}

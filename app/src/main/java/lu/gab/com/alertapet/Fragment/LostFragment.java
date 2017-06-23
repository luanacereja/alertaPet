package lu.gab.com.alertapet.Fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lu.gab.com.alertapet.R;

/**
 * Created by Lu on 31/05/2017.
 */

public class LostFragment extends Fragment{

    public LostFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lost_fragment,container,false);
    }

}

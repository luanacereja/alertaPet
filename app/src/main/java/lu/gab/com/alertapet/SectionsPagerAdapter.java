package lu.gab.com.alertapet;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import lu.gab.com.alertapet.Fragment.DonationFragment;
import lu.gab.com.alertapet.Fragment.LostFragment;
import lu.gab.com.alertapet.Fragment.StreetFragment;

/**
 * Created by Lu on 19/06/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position){

            case 0:
                StreetFragment streetFragment = new StreetFragment();
                return streetFragment;
            case 1:
                LostFragment lostFragment = new LostFragment();
                return lostFragment;
            case 2:
                DonationFragment donationFragment = new DonationFragment();
                return donationFragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "De rua";

            case 1:
                return "Perdido";

            case 2:
                return "Doação";

            default:
                return null;
        }

    }

}

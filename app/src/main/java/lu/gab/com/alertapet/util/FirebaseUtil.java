package lu.gab.com.alertapet.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Lu on 27/06/2017.
 */

public class FirebaseUtil {

    public static DatabaseReference getUserRef(String email){
        return FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_KEYS)
                .child(email);
    }


    public static DatabaseReference getReportRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Constants.REPORT_KEY);
    }
}

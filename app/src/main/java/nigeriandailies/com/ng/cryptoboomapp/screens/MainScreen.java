package nigeriandailies.com.ng.cryptoboomapp.screens;


import java.util.List;

import nigeriandailies.com.ng.cryptoboomapp.models.CoinModel;

/**
 * Created by omrierez on 23.11.17.
 */

public interface MainScreen {

    void updateData(List<CoinModel> data);
    void setError(String msg);
}

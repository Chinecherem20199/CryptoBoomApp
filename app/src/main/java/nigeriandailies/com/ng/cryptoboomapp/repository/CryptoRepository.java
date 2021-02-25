package nigeriandailies.com.ng.cryptoboomapp.repository;


import androidx.lifecycle.LiveData;

import java.util.List;

import nigeriandailies.com.ng.cryptoboomapp.models.CoinModel;

/**
 * Created by omrierez on 28.12.17.
 */

public interface CryptoRepository {

    LiveData<List<CoinModel>> getCryptoCoinsData();
    LiveData<String> getErrorStream();
    LiveData<Double> getTotalMarketCapStream();
    void fetchData();
}

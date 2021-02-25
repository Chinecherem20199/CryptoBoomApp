package nigeriandailies.com.ng.cryptoboomapp.repository.datasource;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import nigeriandailies.com.ng.cryptoboomapp.db.RoomDb;
import nigeriandailies.com.ng.cryptoboomapp.entities.CryptoCoinEntity;


/**
 * Created by omrierez on 28.12.17.
 */

public class LocalDataSource implements DataSource<List<CryptoCoinEntity>>{
    private final RoomDb mDb;
    private final MutableLiveData<String> mError=new MutableLiveData<>();
    public LocalDataSource(Context mAppContext) {
        mDb= RoomDb.getDatabase(mAppContext);
    }
    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mDb.coinDao().getAllCoinsLive();
    }
    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void writeData(List<CryptoCoinEntity> coins) {
        try {
            mDb.coinDao().insertCoins(coins);
        }catch(Exception e)
        {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public List<CryptoCoinEntity> getALlCoins() {
        return mDb.coinDao().getAllCoins();
    }
}

package nigeriandailies.com.ng.cryptoboomapp.repository;


import android.content.Context;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nigeriandailies.com.ng.cryptoboomapp.entities.CryptoCoinEntity;
import nigeriandailies.com.ng.cryptoboomapp.mappers.CryptoMapper;
import nigeriandailies.com.ng.cryptoboomapp.models.CoinModel;
import nigeriandailies.com.ng.cryptoboomapp.repository.datasource.LocalDataSource;
import nigeriandailies.com.ng.cryptoboomapp.repository.datasource.RemoteDataSource;

/**
 * Created by omrierez on 28.12.17.
 */

public class CryptoRepositoryImpl implements CryptoRepository {

    private static final String TAG = CryptoRepositoryImpl.class.getSimpleName();
    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private final RemoteDataSource mRemoteDataSource;
    private final LocalDataSource mLocalDataSource;
    private final CryptoMapper mMapper;
    MediatorLiveData<List<CoinModel>> mDataMerger = new MediatorLiveData<>();
    MediatorLiveData<String> mErrorMerger = new MediatorLiveData<>();

    private CryptoRepositoryImpl(RemoteDataSource mRemoteDataSource, LocalDataSource mLocalDataSource, CryptoMapper mapper) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
        mMapper = mapper;
        mDataMerger.addSource(this.mRemoteDataSource.getDataStream(), entities ->
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mDataMerger\tmRemoteDataSource onChange invoked");
                        mLocalDataSource.writeData(entities);
                        List<CoinModel> list = mMapper.mapEntityToModel(entities);
                        mDataMerger.postValue(list);

                    }
                })
        );
      /*  mDataMerger.addSource(this.mLocalDataSource.getDataStream(), entities ->
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mDataMerger\tmLocalDataSource onChange invoked");
                        List<CoinModel> models = mMapper.mapEntityToModel(entities);
                        mDataMerger.postValue(models);
                    }
                })

        );*/
        mErrorMerger.addSource(mRemoteDataSource.getErrorStream(), errorStr -> {
                    mErrorMerger.setValue(errorStr);
                    Log.d(TAG, "Network error -> fetching from LocalDataSource");
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<CryptoCoinEntity> entities = (mLocalDataSource.getALlCoins());
                            mDataMerger.postValue(mMapper.mapEntityToModel(entities));
                        }
                    });

                }
        );
        mErrorMerger.addSource(mLocalDataSource.getErrorStream(), errorStr -> mErrorMerger.setValue(errorStr));
    }

    public static CryptoRepository create(Context mAppContext) {
        final CryptoMapper mapper = new CryptoMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(mAppContext);
        return new CryptoRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @Override
    public void fetchData() {
        mRemoteDataSource.fetch();
    }

    @Override
    public LiveData<List<CoinModel>> getCryptoCoinsData() {
        return mDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mErrorMerger;
    }

    @Override
    public LiveData<Double> getTotalMarketCapStream() {
        return Transformations.map(mDataMerger, input -> {
            double totalMarketCap = 0;
            for (int i = 0; i < input.size(); i++) {
                totalMarketCap += input.get(i).marketCap;
            }
            return totalMarketCap;
        });
    }
}

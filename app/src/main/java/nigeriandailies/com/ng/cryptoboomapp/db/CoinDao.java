package nigeriandailies.com.ng.cryptoboomapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

;import java.util.List;

import nigeriandailies.com.ng.cryptoboomapp.entities.CryptoCoinEntity;

/**
 * Created by omrierez on 30.12.17.
 */

@Dao
public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoins(List<CryptoCoinEntity> coins);

    @Query("SELECT * FROM coins")
    LiveData<List<CryptoCoinEntity>> getAllCoinsLive();

    @Query("SELECT * FROM coins")
    List<CryptoCoinEntity> getAllCoins();

    @Query("SELECT * FROM coins LIMIT :limit")
    LiveData<List<CryptoCoinEntity>>getCoins(int limit);

    @Query("SELECT * FROM coins WHERE symbol=:symbol")
    LiveData<CryptoCoinEntity>getCoin(String symbol);

}


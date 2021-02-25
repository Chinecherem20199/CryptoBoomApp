package nigeriandailies.com.ng.cryptoboomapp.repository.datasource;


import androidx.lifecycle.LiveData;

/**
 * Created by omrierez on 28.12.17.
 */

public interface DataSource<T> {

    LiveData<T> getDataStream();
    LiveData<String> getErrorStream();
}

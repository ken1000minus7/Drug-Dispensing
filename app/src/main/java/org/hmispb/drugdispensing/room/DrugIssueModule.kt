package org.hmispb.drugdispensing.room

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hmispb.drugdispensing.Util.LOGIN_RESPONSE_PREF
import org.hmispb.drugdispensing.Util.password
import org.hmispb.drugdispensing.Util.username
import org.hmispb.drugdispensing.model.DailyDrugConsumption
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DrugIssueModule {
    @Provides
    @Singleton
    fun provideDrugIssueDatabase(app : Application) : DrugIssueDatabase {
        return Room.databaseBuilder(
            app,
            DrugIssueDatabase::class.java,
            "drugissue_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDrugIssueRepository(drugIssueDatabase: DrugIssueDatabase, drugIssueApi: DrugIssueApi) : DrugIssueRepository {
        return DrugIssueRepositoryImpl(drugIssueDatabase.drugIssueDao,drugIssueApi)
    }

    @Provides
    @Singleton
    fun provideDailyDrugConsumption(drugIssueDatabase: DrugIssueDatabase) : DailyDrugConsumptionRepository {
        return DailyDrugConsumptionRepositoryImpl(drugIssueDatabase.dailyDrugConsumptionDao)
    }

    @Provides
    @Singleton
    fun provideDrugIssueApi(app : Application, sharedPreferences: SharedPreferences) : DrugIssueApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val cacheSize = (10*1024*1024).toLong()
        val cache = Cache(app.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(logging)
            .addInterceptor(BasicAuthInterceptor(
                username,
                password
            ))
            .addInterceptor{
                var request = it.request()
                request = if (hasNetwork(app) ==true)
                    request.newBuilder().header("Cache-Control","public, max-age="+5).build()
                else
                    request.newBuilder().header("Cache-Control","public, only-if-cached, max-stale="+60*60*24*7).build()
                it.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl("https://hmispb.in/HISUtilities/services/restful/EMMSMasterDataWebService/DMLService/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(DrugIssueApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app : Application) : SharedPreferences {
        return app.getSharedPreferences(LOGIN_RESPONSE_PREF,Context.MODE_PRIVATE)
    }

    private fun hasNetwork(app : Application): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}
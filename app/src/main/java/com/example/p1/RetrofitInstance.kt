import com.example.p1.LoginInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.133.124:8083/"
    private const val BASE_URL_LOG = "http://192.168.133.124:3000/"
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
    private val retrofitForAuth: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_LOG)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun login(): LoginInterface {
        return retrofitForAuth.create(LoginInterface::class.java)
    }

}

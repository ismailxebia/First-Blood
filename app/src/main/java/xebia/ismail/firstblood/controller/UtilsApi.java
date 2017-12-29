package xebia.ismail.firstblood.controller;

/**
 * Created by Admin on 12/29/2017.
 */

public class UtilsApi {

    // Masih virtual.
    public static final String BASE_URL_API = "https://a632d00c.ngrok.io";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
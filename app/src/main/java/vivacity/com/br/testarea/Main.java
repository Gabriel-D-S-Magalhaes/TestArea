package vivacity.com.br.testarea;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main
{
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    private static final String subscriptionKey = "beb935869dcc4e18bb32d18381335e54";

    // Replace or verify the region.
    //
    // You must use the same region in your REST API call as you used to obtain your subscription keys.
    // For example, if you obtained your subscription keys from the westus region, replace
    // "westcentralus" in the URI below with "westus".
    //
    // NOTE: Free trial subscription keys are generated in the westcentralus region, so if you are using
    // a free trial subscription key, you should not need to change this region.

    private static final String detectURL = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect" +
            "?returnFaceId=true" +
            "&returnFaceLandmarks=false" +
            "&returnFaceAttributes=age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

    private static final String verifyURL = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/verify\n";

    public static void main(String[] args) {

    }

    private static void webClient() {

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        //builder.url(detectURL);
        builder.url(verifyURL);
        builder.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Media type of the body sent to the API.
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // content: JSON = { "url": "URL of input image" }
        //RequestBody body = RequestBody.create(mediaType, "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}");
        RequestBody body = RequestBody.create(mediaType, "{\"faceId1\":\"be5aace4-d6f4-4c35-bf32-af249b779afc\",\"faceId2\":\"c9c0992d-6174-400d-87a5-a549657db703\"}");

        builder.post(body);

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(response.body().string()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserialize() throws IOException {
        //Forecast15Bean forecast15Bean = new Gson().fromJson(new HTTPMethods().forecast15(3477), Forecast15Bean.class);
        //Forecast3Bean forecast3Bean = new Gson().fromJson(new HTTPMethods().forecast3(3477), Forecast3Bean.class);
        //LocaleBean localeBean = new Gson().fromJson(new HTTPMethods().localeID(3477), LocaleBean.class);
        //LocaleBean[] localeBeans = new Gson().fromJson(new HTTPMethods().localeNameState(HTTPMethods.SEARCH_CITY_STATE, null, "SP"), LocaleBean[].class);
        //WeatherBean weatherBean = new Gson().fromJson(new HTTPMethods().weather(3477), WeatherBean.class);
    }
}

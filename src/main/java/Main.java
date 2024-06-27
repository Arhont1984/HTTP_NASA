
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .setRedirectsEnabled(false)
                .build();
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setUserAgent("My Test Service")
                    .setDefaultRequestConfig(config)
                    .build();
            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=9ajwC49sedIccxQgJaAUANr8mI9TGkOpoUDwzzVc");
            CloseableHttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            NASAImage imageData = objectMapper.readValue(jsonResponse, NASAImage.class);

            if (imageData != null && imageData.getUrl() != null) {
                HttpGet imageRequest = new HttpGet(imageData.getUrl());
                CloseableHttpResponse imageResponse = httpClient.execute(imageRequest);
                byte[] imageBytes = EntityUtils.toByteArray(imageResponse.getEntity());
                String fileName = imageData.getUrl().substring(imageData.getUrl().lastIndexOf('/') + 1);
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    fos.write(imageBytes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


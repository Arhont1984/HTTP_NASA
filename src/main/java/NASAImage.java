import com.fasterxml.jackson.annotation.JsonProperty;


public class NASAImage {

    private final String url;

    public NASAImage(
            @JsonProperty("url") String url
    ) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
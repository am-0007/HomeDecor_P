package HomeDecor.product.image.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageResponse {
    private String url;
    public  ImageResponse(String url) {
        this.url = url;
    }
}

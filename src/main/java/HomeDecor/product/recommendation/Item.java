package HomeDecor.product.recommendation;

import lombok.Getter;

@Getter
public class Item {

    private long productId;
    private double rate;

    public Item(long productId, double rate) {
        this.productId = productId;
        this.rate = rate;
    }
}

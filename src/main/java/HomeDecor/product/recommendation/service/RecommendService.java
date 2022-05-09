package HomeDecor.product.recommendation.service;

import HomeDecor.product.Product;
import HomeDecor.product.repository.ProductRepository;
import HomeDecor.product.dto.ProductResponse;
import HomeDecor.product.rating.RatingProduct;
import HomeDecor.product.rating.ratingrepository.RatingRepository;
import HomeDecor.product.recommendation.Item;
import lombok.AllArgsConstructor;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RecommendService {

    @Autowired
    private final RatingRepository ratingRepository;

    @Autowired
    private final ProductRepository productRepository;
    private static String directory = "data/dataset.csv";
    //private static String fileCsv = "jrsc/src/main/resources/data/output.csv";
    private static Integer recommendedItemsCount = 100;


    public List<Product> recommendAllItemUserBased(long userId) {
        List<Product> recommendProduct = new ArrayList<>();
        List<RatingProduct> recommendItems = ratingRepository.findAll();
        intoFile(recommendItems);

        List<Item> items = new ArrayList<>();

        try {
            //ClassLoader classLoader = getClass().getClassLoader();
            //File file = new File(classLoader.getResource("table.csv").getFile());
            File file = new File(directory);
            DataModel model = new FileDataModel(new File(file.getAbsolutePath()));
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            List<RecommendedItem> recommendations = recommender.recommend(userId, recommendedItemsCount);
            for (RecommendedItem item : recommendations) {
                items.add(new Item(item.getItemID(), item.getValue()));
            }
            for (Item i : items) {
                System.out.println("Product id: "  + i.getProductId());
            }

            List<ProductResponse> productResponses =new ArrayList<>();
            for (Item i : items) {
                Optional<Product> recommendProductEmpty = productRepository.findById((int) i.getProductId());
                if (recommendProductEmpty.isPresent()) {
                    Product getRecommendProduct = recommendProductEmpty.get();
                    productResponses.add(new ProductResponse(
                            getRecommendProduct.getId(),
                            getRecommendProduct.getProductName(),
                            getRecommendProduct.getProductDescription(),
                            getRecommendProduct.getProductCategory(),
                            getRecommendProduct.getPrice(),
                            getRecommendProduct.getUser().getId(),
                            getRecommendProduct.getImage().getImagePath()
                    ));
                    /*getRecommendProduct.setImage(Base64.getDecoder().decode(getRecommendProduct.getImage()));
                    recommendProduct.add(getRecommendProduct);*/
                }
            }

            /*for (Item i : items) {
                Optional<Product> recommendProductEmpty = productRepository.findById((int)i.getProductId());
                if (recommendProductEmpty.isPresent()) {
                    Product getRecommendProduct = recommendProductEmpty.get();
                    getRecommendProduct.setImage(Base64.getDecoder().decode(getRecommendProduct.getImage()));
                    recommendProduct.add(getRecommendProduct);
                }
            }*/
            return recommendProduct;
        } catch (Exception e) {
            items.clear();
            System.out.println(e);
        }
        return recommendProduct;
    }


    public List<Product> recommendAllItemsItemBased(Integer userId) {

        List<Product> recommendProduct = new ArrayList<>();
        List<RatingProduct> recommendItems = ratingRepository.findAll();
        intoFile(recommendItems);

        List<Item> items = new ArrayList<>();
        try{
            File file = new File(directory);
            DataModel model = new FileDataModel(new File(file.getAbsolutePath()));
            ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(model);
            Recommender itemRecommender = new GenericItemBasedRecommender(model,itemSimilarity);
            List<RecommendedItem> itemRecommendations = itemRecommender.recommend(userId, recommendedItemsCount);
            for (RecommendedItem item : itemRecommendations) {
                items.add(new Item(item.getItemID(), item.getValue()));
            }
            for (RecommendedItem i : itemRecommendations) {
                System.out.println(i);
            }
            for (Item i : items) {
                System.out.println("Product id: "  + i.getProductId());
            }

            List<ProductResponse> productResponses =new ArrayList<>();
            for (Item i : items) {
                Optional<Product> recommendProductEmpty = productRepository.findById((int)i.getProductId());
                if (recommendProductEmpty.isPresent()) {
                    Product getRecommendProduct = recommendProductEmpty.get();
                    productResponses.add(new ProductResponse(
                            getRecommendProduct.getId(),
                            getRecommendProduct.getProductName(),
                            getRecommendProduct.getProductDescription(),
                            getRecommendProduct.getProductCategory(),
                            getRecommendProduct.getPrice(),
                            getRecommendProduct.getUser().getId(),
                            getRecommendProduct.getImage().getImagePath()
                    ));
                    /*getRecommendProduct.setImage(Base64.getDecoder().decode(getRecommendProduct.getImage()));
                    recommendProduct.add(getRecommendProduct);*/
                }
            }
            return recommendProduct;
        } catch (Exception e) {
            items.clear();
            System.out.println(e);
        }
        return recommendProduct;
    }

    public void intoFile(List<RatingProduct> DataFromDB) {
        //write into File
        FileWriter writer = null;
        try {
            writer = new FileWriter("data/dataset.csv");
            String data;
            String uData;
            double Rdata;
            for (RatingProduct i : DataFromDB) {
                data = i.getProduct().getId().toString();
                uData = i .getUserID().toString();
                Rdata = i.getRatingProduct();
                writer.write(uData +",");
                writer.write(data + ",");
                writer.write(String.valueOf(Rdata) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /*for (RatingProduct i : DataFromDB) {
             data = Arrays.asList(
                    String.valueOf(i.getProduct().getId()),
                    String.valueOf(i.getUserID()),
                    String.valueOf(i.getRatingProduct())
            );
            System.out.println(data);
        }
        System.out.println(data);*/

    }


     /*   @Override
    public List<RatingProduct> fillItemsUserBased(Integer userId) {

        return null;
    }*/

    /*
        public ByteArrayInputStream load() {
            */
/*List<RatingProduct> tutorials = ratingRepository.findAll();
        ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
        return in;*//*

    }
*/


}

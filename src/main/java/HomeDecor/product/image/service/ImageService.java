package HomeDecor.product.image.service;

import HomeDecor.product.image.Image;
import HomeDecor.product.image.ImageRepository;
import HomeDecor.user.User;
import HomeDecor.user.UserInterface;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserInterface userInterface;

    public Image saveImage(MultipartFile multipartFile, User user) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Image image = new Image(
                fileName,
                multipartFile.getContentType(),
                user
        );
        Image savedImage = imageRepository.save(image);
        String uploadDirectory = "image/" + image.getUser().getId() + "/product";
        image.saveFile(uploadDirectory, fileName, multipartFile);
        return savedImage;
    }

}

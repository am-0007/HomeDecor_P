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
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

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

    public Image updateImage(MultipartFile multipartFile, User user, Integer imageId, String formerImageName) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Optional<Image> imageExists = imageRepository.findById(imageId);
        if (imageExists.isEmpty()) {
            throw new IllegalStateException("Image failed to upload");
        }
        Image image = imageExists.get();
        image.setName(fileName);
        image.setType(multipartFile.getContentType());
        image.setUser(user);
        Image updateImage = imageRepository.save(image);
        String updateDirectory = "image/" + image.getUser().getId() + "/product";
        image.updateFile(updateDirectory, fileName, multipartFile, formerImageName);
        return updateImage;
    }

    public void deleteImage(Image image) {
        String updateDirectory = "image/" + image.getUser().getId() + "/product";
        System.out.println("Image id: " + image.getImgId());
        try {
            image.deleteImage(image, updateDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Image not deleted!");
        }
        imageRepository.delete(image);
    }

}

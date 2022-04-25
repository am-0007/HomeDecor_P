package HomeDecor.product.image;

import HomeDecor.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgId;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne
    private User user;

    public Image(String name,
                 String type,
                 User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }

    public void saveFile(String uploadDirectory,
                         String fileName,
                         MultipartFile multipartFile) throws IOException {
        Path updatedPath = Paths.get(uploadDirectory);
        if (!Files.exists(updatedPath)) {
            Files.createDirectories(updatedPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = updatedPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public String getImagePath() {
        if (name == null || user.getId() == null) {
            return null;
        }
        return "/image/" + user.getId() + "/" + "product/" + name;
    }

    public void updateFile(String updateDirectory,
                           String fileName,
                           MultipartFile multipartFile,
                           String formerImageName)throws IOException{

        Path updatedPath = Paths.get(updateDirectory);
        try {
            File deleteFile = new File(updatedPath + "/" + formerImageName);
            System.out.println(formerImageName);
            deleteFile.delete();
            if (deleteFile.exists()) {
                throw new IOException("Failed to delete Image");
            } else {
                saveFile(updateDirectory, fileName, multipartFile);
            }

        } catch (IOException e) {
            throw new IOException("Failed to update image");
        }
    }

    public void deleteImage(Image image, String updateDirectory) throws IOException {
        System.out.println("Nameeeeeeeeeeee: " + image.getName());

        File deleteFile = new File(updateDirectory + "/" + image.getName());
        deleteFile.delete();
        if (deleteFile.exists()) {
            throw new IOException("Failed to delete Image");
        }
    }
}

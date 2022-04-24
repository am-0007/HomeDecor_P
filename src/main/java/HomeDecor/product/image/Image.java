package HomeDecor.product.image;

import HomeDecor.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
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

    public void saveFile(String uploadDirectory, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
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
}

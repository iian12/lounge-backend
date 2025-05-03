package com.dju.lounge.domain.post.service;

import com.dju.lounge.domain.post.dto.FileUploadResult;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class FileService {

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png",
        "image/gif", "video/mp4", "video/quicktime", "video/x-matroska");
    private final Tika tika = new Tika();
    @Value("${file.upload-dir}")
    private String uploadDir;

    private static BufferedImage cropToSquare(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        int size = Math.min(width, height);
        int xOffset = (width - size) / 2;
        int yOffset = (height - size) / 2;

        return original.getSubimage(xOffset, yOffset, size, size);
    }

    private static BufferedImage resizeImage(BufferedImage original) {
        Image temp = original.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = resized.createGraphics();
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        return resized;
    }

    public FileUploadResult validateAndSaveFilesWithThumbnailIncluded(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return new FileUploadResult(null, List.of());
        }

        MultipartFile firstFile = files.getFirst();
        String mimeType;

        try {
            mimeType = tika.detect(firstFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to detect file type", e);
        }

        String today = LocalDate.now().toString().replace("-", "");
        Path folerPath = Paths.get(uploadDir, today);
        if (!Files.exists(folerPath)) {
            try {
                Files.createDirectories(folerPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory", e);
            }
        }

        String thumbnailUrl = saveThumbnail(firstFile, mimeType, folerPath);
        List<String> fileUrls = files.stream().skip(1).map(file -> {
            try {
                String type = tika.detect(file.getInputStream());
                if (!ALLOWED_MIME_TYPES.contains(type)) {
                    throw new IllegalArgumentException("Unsupported file type: " + type);
                }
                return saveFile(file, type, today, folerPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }
        }).toList();

        return new FileUploadResult(thumbnailUrl, fileUrls);
    }

    private String saveFile(MultipartFile file, String mimeType, String today, Path folderPath)
        throws IOException {

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        String extension = changeMimeTypeToExtension(mimeType);
        String savedFileName = UUID.randomUUID() + extension;

        Path savedPath = folderPath.resolve(savedFileName);
        file.transferTo(savedPath.toFile());

        return "/files/ " + today + "/" + savedFileName;
    }

    private String changeMimeTypeToExtension(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            case "video/mp4" -> ".mp4";
            case "video/quicktime" -> ".mov";
            case "video/webm" -> ".webm";
            case "video/x-matroska" -> ".mkv";
            default -> "";
        };
    }

    private String saveThumbnail(MultipartFile file, String mimeType, Path folderPath) {
        try {
            if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
                throw new IllegalArgumentException("Unsupported file type: " + mimeType);
            }

            String extension = changeMimeTypeToExtension(mimeType);
            String fileName = UUID.randomUUID() + extension;

            Path thumbnailFolder = folderPath.resolve("thumbnail");
            if (!Files.exists(thumbnailFolder)) {
                Files.createDirectories(thumbnailFolder);
            }
            Path thumbPath = thumbnailFolder.resolve(fileName);

            if (mimeType.startsWith("image")) {
                BufferedImage original = ImageIO.read(file.getInputStream());
                BufferedImage cropped = cropToSquare(original);
                BufferedImage resized = resizeImage(cropped);
                ImageIO.write(resized, "jpg", thumbPath.toFile());
            } else if (mimeType.startsWith("video")) {
                Path tempVideo = Files.createTempFile("upload_video_",
                    changeMimeTypeToExtension(mimeType));
                file.transferTo(tempVideo.toFile());

                Path extractedFrame = Files.createTempFile("frame_", ".jpg");
                ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", tempVideo.toString(), "-ss",
                    "00:00:01", "-vframes", "1", "-q:v", "2", extractedFrame.toString());
                pb.redirectErrorStream(true);
                Process process = pb.start();
                process.waitFor();

                BufferedImage frame = ImageIO.read(extractedFrame.toFile());
                BufferedImage cropped = cropToSquare(frame);
                BufferedImage resized = resizeImage(cropped);
                ImageIO.write(resized, "jpg", thumbPath.toFile());

                Files.deleteIfExists(tempVideo);
                Files.deleteIfExists(extractedFrame);
            }

            return "/files/" + folderPath.getFileName() + "/" + fileName;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}
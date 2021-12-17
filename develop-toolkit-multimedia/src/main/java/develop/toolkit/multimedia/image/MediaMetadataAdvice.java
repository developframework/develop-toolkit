package develop.toolkit.multimedia.image;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mov.QuickTimeDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import develop.toolkit.base.utils.DateTimeAdvice;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author qiushui on 2021-12-17.
 */
public abstract class MediaMetadataAdvice {

    /**
     * 根据扩展名获取文件类型
     *
     * @param extensionName 扩展名
     */
    public static FileType getFileTypeByExtensionName(String extensionName) {
        for (FileType fileType : FileType.values()) {
            for (String extension : fileType.getAllExtensions()) {
                if (extension.equals(extensionName)) {
                    return fileType;
                }
            }
        }
        return FileType.Unknown;
    }

    /**
     * 根据媒体类型获取文件类型
     *
     * @param mediaType 媒体类型
     */
    public static FileType getFileTypeByMediaType(String mediaType) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getMimeType().equals(mediaType)) {
                return fileType;
            }
        }
        return FileType.Unknown;
    }

    /**
     * 打印所有值
     *
     * @param inputStream 文件流
     */
    @SneakyThrows({ImageProcessingException.class, IOException.class})
    public static void printMetadataTags(InputStream inputStream) {
        final Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
        final Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }

    /**
     * 提取图片文件中的创建时间
     */
    @SneakyThrows({ImageProcessingException.class, IOException.class})
    public static LocalDateTime extractCreateTimeForImage(File file) {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        return getMetadataDate(metadata, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL).orElse(null);
    }

    /**
     * 提取视频文件中的创建时间
     */
    @SneakyThrows({ImageProcessingException.class, IOException.class})
    public static LocalDateTime extractCreateTimeForVideo(File file) {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        return getMetadataDate(metadata, Mp4Directory.class, Mp4Directory.TAG_CREATION_TIME)
                .orElseGet(() ->
                        getMetadataDate(metadata, QuickTimeDirectory.class, QuickTimeDirectory.TAG_CREATION_TIME).orElse(null)
                );
    }

    private static Optional<LocalDateTime> getMetadataDate(Metadata metadata, Class<? extends Directory> directoryClass, int tag) {
        return Optional
                .ofNullable(metadata.getFirstDirectoryOfType(directoryClass))
                .map(directory -> directory.getDate(tag))
                .map(DateTimeAdvice::toLocalDateTime);
    }
}

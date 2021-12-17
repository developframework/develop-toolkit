package develop.toolkit.multimedia.image;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import develop.toolkit.base.utils.CompareAdvice;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author qiushui on 2021-06-20.
 */
public abstract class ImageAdvice {

    /**
     * 修正图片角度后裁切图片
     *
     * @param inputStream  图片输入流
     * @param outputStream 输出流
     * @param rectangle    裁切区域
     * @param outFileType  输出图片类型
     * @throws IOException
     */
    public static void fixOrientationAndCut(InputStream inputStream, OutputStream outputStream, Rectangle rectangle, FileType outFileType) throws IOException {
        final byte[] data = IOUtils.toByteArray(inputStream);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        final int angle = readOrientationAngle(bais);
        bais.close();
        bais = new ByteArrayInputStream(data);
        final BufferedImage image = cut(
                rotate(ImageIO.read(bais), angle),
                rectangle
        );
        bais.close();
        ImageIO.write(image, outFileType.getAllExtensions()[0], outputStream);
    }

    /**
     * 修正图片角度后定宽缩放
     *
     * @param inputStream  图片输入流
     * @param outputStream 输出流
     * @param width        定宽
     * @param outFileType  输出图片类型
     * @throws IOException
     */
    public static void fixOrientationAndZoom(InputStream inputStream, OutputStream outputStream, int width, FileType outFileType) throws IOException {
        final byte[] data = IOUtils.toByteArray(inputStream);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        final int angle = readOrientationAngle(bais);
        bais.close();
        bais = new ByteArrayInputStream(data);
        final BufferedImage image = zoom(
                rotate(ImageIO.read(bais), angle),
                width
        );
        bais.close();
        ImageIO.write(image, outFileType.getAllExtensions()[0], outputStream);
    }

    /**
     * 裁切图片
     *
     * @param originalImage 原图
     * @param rectangle     裁切区域
     * @return 新图片
     */
    public static BufferedImage cut(BufferedImage originalImage, Rectangle rectangle) {
        final int MAX_WIDTH = originalImage.getWidth();
        final int MAX_HEIGHT = originalImage.getHeight();
        final int x = CompareAdvice.adjustRange((int) rectangle.getX(), 0, MAX_WIDTH - 1);
        final int y = CompareAdvice.adjustRange((int) rectangle.getY(), 0, MAX_HEIGHT - 1);
        final int width = CompareAdvice.adjustRange((int) rectangle.getWidth(), 1, MAX_WIDTH - x);
        final int height = CompareAdvice.adjustRange((int) rectangle.getHeight(), 1, MAX_HEIGHT - y);
        return originalImage.getSubimage(x, y, width, height);
    }

    /**
     * 旋转角度
     *
     * @param originalImage 原图
     * @param angle         旋转角度
     * @return 新图片
     */
    public static BufferedImage rotate(BufferedImage originalImage, int angle) {
        // 修正角度
        while (angle < 0) {
            angle += 360;
        }
        angle %= 360;
        if (angle == 0) {
            return originalImage;
        }
        final int MAX_WIDTH = originalImage.getWidth();
        final int MAX_HEIGHT = originalImage.getHeight();
        Rectangle rectangle = computeRotatedSize(new Rectangle(new Dimension(MAX_WIDTH, MAX_HEIGHT)), angle);
        BufferedImage newImage = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = newImage.createGraphics();
        graphics.translate((rectangle.width - MAX_WIDTH) / 2, (rectangle.height - MAX_HEIGHT) / 2);
        graphics.rotate(Math.toRadians(angle), (double) MAX_WIDTH / 2, (double) MAX_HEIGHT / 2);
        graphics.drawImage(originalImage, null, null);
        return newImage;
    }

    /**
     * 定宽缩放
     *
     * @param originalImage 原图
     * @param width         定宽
     * @return 新图片
     */
    public static BufferedImage zoom(BufferedImage originalImage, int width) {
        final int MAX_WIDTH = originalImage.getWidth();
        final int MAX_HEIGHT = originalImage.getHeight();
        if (MAX_WIDTH == width) {
            return originalImage;
        }
        final int newHeight = (int) ((double) width / (double) MAX_WIDTH * (double) MAX_HEIGHT);
        final BufferedImage newImage = new BufferedImage(width, newHeight, BufferedImage.TYPE_INT_RGB);
        final Image image = originalImage.getScaledInstance(width, newHeight, BufferedImage.SCALE_FAST);
        newImage.getGraphics().drawImage(image, 0, 0, null);
        return newImage;
    }

    /**
     * 计算旋转后的画布大小
     */
    private static Rectangle computeRotatedSize(Rectangle rectangle, int angle) {
        if (angle >= 90) {
            if (angle / 90 % 2 == 1) {
                int temp = rectangle.height;
                rectangle.height = rectangle.width;
                rectangle.width = temp;
            }
            angle %= 90;
        }
        double r = Math.sqrt(rectangle.height * rectangle.height + rectangle.width * rectangle.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angle) / 2) * r;
        double angelAlpha = (Math.PI - Math.toRadians(angle)) / 2;
        double angelDeltaWidth = Math.atan((double) rectangle.height / rectangle.width);
        double angelDeltaHeight = Math.atan((double) rectangle.width / rectangle.height);
        int lenDeltaWidth = (int) (len * Math.cos(Math.PI - angelAlpha - angelDeltaWidth));
        int lenDeltaHeight = (int) (len * Math.cos(Math.PI - angelAlpha - angelDeltaHeight));
        int desWidth = rectangle.width + lenDeltaWidth * 2;
        int des_height = rectangle.height + lenDeltaHeight * 2;
        return new Rectangle(new Dimension(desWidth, des_height));
    }

    /**
     * 读取图片拍摄角度
     *
     * @param inputStream 文件输入流
     * @return 角度
     * @throws IOException
     */
    private static int readOrientationAngle(InputStream inputStream) throws IOException {
        final int orientation;
        try {
            final Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            final ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            } else {
                orientation = 0;
            }
        } catch (ImageProcessingException | MetadataException e) {
            throw new RuntimeException("read image metadata fail: " + e.getMessage());
        }
        int angle;
        switch (orientation) {
            case 3:
                angle = 180;
                break;
            case 6:
                angle = 90;
                break;
            case 8:
                angle = 270;
                break;
            default:
                angle = 0;
                break;
        }
        return angle;
    }
}

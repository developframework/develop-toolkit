package develop.toolkit.base.utils;

import develop.toolkit.base.struct.ZipWrapper;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩增强
 *
 * @author qiushui on 2022-04-17.
 */
public abstract class CompressAdvice {

    public static class GZip {

        public static void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                inputStream.transferTo(gzipOutputStream);
            }
        }

        public static void compress(byte[] data, OutputStream outputStream) throws IOException {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
                compress(bais, outputStream);
            }
        }

        public static byte[] compress(InputStream inputStream) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                compress(inputStream, baos);
                return baos.toByteArray();
            }
        }

        public static byte[] compress(byte[] data) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                compress(data, baos);
                return baos.toByteArray();
            }
        }

        public static void uncompress(InputStream inputStream, OutputStream outputStream) throws IOException {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
                gzipInputStream.transferTo(outputStream);
            }
        }

        public static void uncompress(byte[] data, OutputStream outputStream) throws IOException {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
                uncompress(bais, outputStream);
            }
        }

        public static byte[] uncompress(InputStream inputStream) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                uncompress(inputStream, baos);
                return baos.toByteArray();
            }
        }

        public static byte[] uncompress(byte[] data) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                uncompress(data, baos);
                return baos.toByteArray();
            }
        }
    }

    public static class Zip {

        public static void compress(ZipWrapper zipWrapper, OutputStream outputStream) {
            ZipOutputStream zos = new ZipOutputStream(outputStream);


        }

        private static void recursiveCompress(ZipWrapper zipWrapper, String parentPath, ZipOutputStream zos) throws IOException {
            if (zipWrapper.isFile()) {
                final ZipEntry zipEntry = new ZipEntry(parentPath + File.separator + zipWrapper.getFilename());
                zos.putNextEntry(zipEntry);
                try (InputStream is = zipWrapper.getInputStreamSupplier().get()) {
                    is.transferTo(zos);
                }
                zos.closeEntry();
            } else {

            }
        }
    }
}

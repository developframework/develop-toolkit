package develop.toolkit.base.utils;

import develop.toolkit.base.struct.ZipWrapper;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
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

        public static void compress(Path path, OutputStream outputStream) throws IOException {
            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                Files.walkFileTree(path, new FileVisitor<>() {

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                        String currentName = filePath.subpath(path.getNameCount(), filePath.getNameCount()).toString();
                        File file = filePath.toFile();
                        final ZipEntry zipEntry = new ZipEntry(currentName);
                        zipEntry.setMethod(currentName.endsWith(".zip") ? ZipEntry.STORED : ZipEntry.DEFLATED);
                        zipEntry.setLastModifiedTime(FileTime.fromMillis(file.lastModified()));
                        zos.putNextEntry(zipEntry);
                        try (InputStream is = new FileInputStream(file)) {
                            is.transferTo(zos);
                        }
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }

        public static byte[] compress(Path path) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                compress(path, baos);
                return baos.toByteArray();
            }
        }

        public static void compress(Path path, Path outPath) throws IOException {
            try (OutputStream os = new FileOutputStream(outPath.toFile())) {
                compress(path, os);
            }
        }


        public static void compress(ZipWrapper zipWrapper, OutputStream outputStream) throws IOException {
            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                recursiveCompress(zipWrapper, "", zos);
            }
        }

        private static void recursiveCompress(ZipWrapper zipWrapper, String parentPath, ZipOutputStream zos) throws IOException {
            final String currentName = parentPath + zipWrapper.getFilename();
            if (zipWrapper.isFile()) {
                final ZipEntry zipEntry = new ZipEntry(currentName);
                zipWrapper.configureZipEntry(zipEntry);
                zos.putNextEntry(zipEntry);
                try (InputStream is = zipWrapper.getInputStreamSupplier().get()) {
                    is.transferTo(zos);
                }
                zos.closeEntry();
            } else {
                List<ZipWrapper> children = zipWrapper.getChildren();
                if (children != null) {
                    for (ZipWrapper childZipWrapper : children) {
                        recursiveCompress(childZipWrapper, currentName + File.separator, zos);
                    }
                }
            }
        }
    }
}

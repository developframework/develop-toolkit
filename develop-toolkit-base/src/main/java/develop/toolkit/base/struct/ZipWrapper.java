package develop.toolkit.base.struct;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;

/**
 * @author qiushui on 2022-04-19.
 */
@Getter
@Setter
public class ZipWrapper {

    private String filename;

    private Supplier<InputStream> inputStreamSupplier;

    private boolean file;

    private List<ZipWrapper> children;

    private boolean stored;

    private long compressedSize;

    private long size;

    private long crc;

    private FileTime creationTime;

    private FileTime lastAccessTime;

    private FileTime lastModifyTime;

    private long time;

    private String comment;

    private LocalDateTime timeLocal;

    private byte[] extra;

    public void configureZipEntry(ZipEntry zipEntry) {
        zipEntry.setMethod(stored ? ZipEntry.STORED : ZipEntry.DEFLATED);
        zipEntry.setCompressedSize(compressedSize);
        zipEntry.setSize(size);
        zipEntry.setCrc(crc);
        zipEntry.setCreationTime(creationTime);
        zipEntry.setLastAccessTime(lastAccessTime);
        zipEntry.setLastModifiedTime(lastModifyTime);
        zipEntry.setTime(time);
        zipEntry.setTimeLocal(timeLocal);
        zipEntry.setExtra(extra);
        zipEntry.setComment(comment);
    }
}

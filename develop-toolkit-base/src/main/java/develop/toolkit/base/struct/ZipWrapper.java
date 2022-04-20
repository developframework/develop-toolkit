package develop.toolkit.base.struct;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

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
}

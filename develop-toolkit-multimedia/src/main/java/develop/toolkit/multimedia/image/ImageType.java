package develop.toolkit.multimedia.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-06-20.
 */
@RequiredArgsConstructor
public enum ImageType {

    JPEG(new String[]{"jpg", "jpeg"}),

    PNG(new String[]{"png"}),

    GIF(new String[]{"gif"});

    @Getter
    private final String[] extensionNames;

    public static ImageType of(String extensionName) {
        for (ImageType type : ImageType.values()) {
            for (String name : type.extensionNames) {
                if (name.equalsIgnoreCase(extensionName)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("not support image type: " + extensionName);
    }
}

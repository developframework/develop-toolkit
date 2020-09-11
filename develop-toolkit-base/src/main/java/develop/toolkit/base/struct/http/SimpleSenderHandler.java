package develop.toolkit.base.struct.http;

/**
 * @author qiushui on 2020-09-11.
 */
public final class SimpleSenderHandler extends AbstractStringSenderHandler<String> {

    @Override
    public String convert(String body) {
        return body;
    }
}

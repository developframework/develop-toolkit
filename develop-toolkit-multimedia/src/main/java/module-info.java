/**
 * @author qiushui on 2021-06-20.
 */
module develop.toolkit.multimedia {
    requires metadata.extractor;
    requires develop.toolkit.base;
    requires java.desktop;
    requires lombok;
    requires org.apache.commons.io;

    exports develop.toolkit.multimedia.image;
}
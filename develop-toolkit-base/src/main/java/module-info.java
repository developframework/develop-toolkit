/**
 * @author qiushui on 2019-02-26.
 */
module develop.toolkit.base {
    requires lombok;
    requires org.slf4j;
    requires org.apache.commons.lang3;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.dataformat.xml;
    requires expression;

    exports develop.toolkit.base.components;
    exports develop.toolkit.base.constants;
    exports develop.toolkit.base.exception;
    exports develop.toolkit.base.struct;
    exports develop.toolkit.base.struct.http;
    exports develop.toolkit.base.struct.range;
    exports develop.toolkit.base.utils;
}
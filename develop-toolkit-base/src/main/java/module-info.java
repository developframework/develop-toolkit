/**
 * @author qiushui on 2019-02-26.
 */
module develop.toolkit.base {
    requires lombok;
    requires org.slf4j;
    requires org.apache.commons.lang3;

    exports develop.toolkit.base.components;
    exports develop.toolkit.base.constants;
    exports develop.toolkit.base.exception;
    exports develop.toolkit.base.struct;
    exports develop.toolkit.base.utils;
}
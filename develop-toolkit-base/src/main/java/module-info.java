/**
 * @author qiushui on 2019-02-26.
 */
module develop.toolkit.base {

    requires lombok;
    requires slf4j.api;

    exports develop.toolkit.base.components;
    exports develop.toolkit.base.constants;
    exports develop.toolkit.base.exception;
    exports develop.toolkit.base.struct;
    exports develop.toolkit.base.utils;
}
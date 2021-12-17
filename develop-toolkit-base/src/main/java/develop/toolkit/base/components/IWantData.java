package develop.toolkit.base.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 我想要的数据
 *
 * @author qiushui on 2021-10-30.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IWantData<T> {

    // 是否成功
    private final boolean success;

    // 失败信息
    private final String message;

    // 想要的数据
    private final T data;

    /**
     * 成功获取
     *
     * @param data 数据
     */
    public static <T> IWantData<T> ok(T data) {
        return new IWantData<>(true, null, data);
    }

    /**
     * 成功获取
     */
    public static IWantData<Void> ok() {
        return ok(null);
    }

    /**
     * 获取失败
     *
     * @param message 失败信息
     */
    public static <T> IWantData<T> fail(String message) {
        return new IWantData<>(false, message, null);
    }

    /**
     * 转换
     *
     * @param function 转换函数
     * @param <R>      目标类型
     * @return 转换值
     */
    public <R> IWantData<R> map(Function<? super T, ? extends R> function) {
        return success ? IWantData.ok(function.apply(data)) : IWantData.fail(message);
    }

    /**
     * 扁平化转换
     *
     * @param function 转换函数
     * @param <R>      目标类型
     * @return 转换值
     */
    public <R> IWantData<R> flatMap(Function<? super T, IWantData<R>> function) {
        return success ? function.apply(data) : IWantData.fail(message);
    }

    /**
     * 提供默认值的数据获取
     *
     * @param defaultValue    默认值
     * @param messageConsumer 失败信息处理
     * @return 数据值
     */
    public T returnData(T defaultValue, Consumer<String> messageConsumer) {
        if (success) {
            return data;
        }
        if (messageConsumer != null) {
            messageConsumer.accept(message);
        }
        return defaultValue;
    }

    /**
     * 提供默认值的数据获取
     *
     * @param defaultSupplier 默认值提供器
     * @param messageConsumer 失败信息处理
     * @return 数据值
     */
    public T returnData(Supplier<T> defaultSupplier, Consumer<String> messageConsumer) {
        if (success) {
            return data;
        }
        if (messageConsumer != null) {
            messageConsumer.accept(message);
        }
        return defaultSupplier.get();
    }

    /**
     * 会抛异常的数据获取
     *
     * @param throwableFunction 异常函数
     * @return 数据值
     */
    public T returnDataThrows(Function<String, ? extends RuntimeException> throwableFunction) {
        if (success) {
            return data;
        }
        throw throwableFunction.apply(message);
    }
}

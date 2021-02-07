package develop.toolkit.base.utils;

/**
 * 排序算法
 *
 * @author qiushui on 2021-02-07.
 */
public abstract class SortAdvice {

    /**
     * 快速排序
     *
     * @param array 数组
     * @param <T>   数组类型
     */
    public static <T extends Comparable<T>> void quickSort(T[] array) {
        if (array == null || array.length < 2) return;
        quickSortRecursive(array, 0, array.length - 1);
    }

    /**
     * 快速排序递归
     * <p>
     * 以第一个元素作为基准值
     * 从左往右找出第一个大于基准值的元素 和 从右往左找出第一个小于基准值的元素 交换
     * 划分出的两个区间继续用该方法交换，直到每个区间只有单个元素
     *
     * @param array 数组
     * @param start 开始区间
     * @param end   结束区间
     * @param <T>   数组类型
     */
    private static <T extends Comparable<T>> void quickSortRecursive(T[] array, int start, int end) {
        if (start >= end) return;
        int l = start, r = end;
        // 基准值
        final T standard = array[start];
        while (l != r) {
            // 从左边找大于基准值
            while (l < end && array[l].compareTo(standard) < 0) l++;
            // 从右边找小于基准值
            while (start < r && array[r].compareTo(standard) > 0) r--;
            // 交换值
            T t = array[l];
            array[l] = array[r];
            array[r] = t;
        }
        // 分成两份继续递归
        quickSortRecursive(array, 0, l - 1);
        quickSortRecursive(array, l + 1, end);
    }

    /**
     * 选择排序
     * <p>
     * 从左往右依次遍历
     * 每次选出剩余数组里最小的值，与当前值替换
     *
     * @param array 数组
     * @param <T>   数组类型
     */
    public static <T extends Comparable<T>> void selectSort(T[] array) {
        if (array == null || array.length < 2) return;
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                T t = array[i];
                array[i] = array[minIndex];
                array[minIndex] = t;
            }
        }
    }
}

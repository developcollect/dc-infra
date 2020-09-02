package com.developcollect.dcinfra.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 集合工具类
 * @author zak
 * @since 1.0.0
 */
public class CollectionUtil extends cn.hutool.core.collection.CollectionUtil {

    @FunctionalInterface
    public interface HashCodeCalculator<T> {

        /**
         * 计算指定对象的hashCode，可以与该对象实际的hashCode方法不同，用在特定场景下的hashCode计算
         * 实现不同的业务， 比如自定义去重
         *
         * @param obj 对象
         * @return int 该对象的自定义hashCode值
         */
        int calc(T obj);
    }

    /**
     * 去重
     *
     * @param list
     * @param hashCodeCalculator
     * @return java.util.List<T>
     */
    public static <T> List<T> distinct(List<T> list, HashCodeCalculator<T> hashCodeCalculator) {
        if (isEmpty(list)) {
            return list;
        }
        Iterator<T> iterator = list.iterator();
        Set<Integer> hashCodeSet = new HashSet<>();
        while (iterator.hasNext()) {
            int hashCode = hashCodeCalculator.calc(iterator.next());
            if (hashCodeSet.contains(hashCode)) {
                iterator.remove();
            } else {
                hashCodeSet.add(hashCode);
            }
        }
        return list;
    }

    /**
     * 将集合中的元素的位置打乱
     * @param list
     * @return java.util.List<T>
     * @author zak
     * @date 2020/8/24 9:57
     */
    public static <T> List<T> shuffle(List<T> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            swap(list, i, RandomUtil.randomInt(i + 1));
        }
        return list;
    }

    /**
     * 交换集合中的两个元素
     * @param list
     * @param a
     * @param b
     * @return java.util.List<T>
     * @author zak
     * @date 2020/8/24 10:04
     */
    public static <T> List<T> swap(List<T> list, int a, int b) {
        Collections.swap(list, a, b);
        return list;
    }

    /**
     * 根据集合中对象的某个方法的返回值是否和给定的值相等, 来判断集合中是否存在符合要求的对象
     *
     * @param collection 集合
     * @param function   方法
     * @param value      值
     * @return boolean
     * @author zak
     * @date 2019/11/20 10:04
     */
    public static <T> boolean contains(final Collection<T> collection, final Function<T, Object> function, final Object value) {
        return contains(collection, ele -> Objects.equals(function.apply(ele), value));
    }

    /**
     * 判断集合中是否有满足指定条件的元素
     * @param collection 集合
     * @param predicate 条件
     * @return boolean
     * @author zak
     * @date 2020/8/24 9:39
     */
    public static <T> boolean contains(final Collection<T> collection, final Predicate<T> predicate) {
        for (T ele : collection) {
            if (ele != null && predicate.test(ele)) {
                return true;
            }
        }
        return false;
    }


    public static <T> T get(final Collection<T> collection, final Function<T, Object> function, final Object value) {
        return get(collection, ele -> Objects.equals(function.apply(ele), value));
    }

    /**
     * 获取集合中满足指定条件的第一个元素
     * @param collection 集合
     * @param predicate 条件
     * @return boolean
     * @author zak
     * @date 2020/8/24 9:39
     */
    public static <T> T get(final Collection<T> collection, final Predicate<T> predicate) {
        for (T ele : collection) {
            if (ele != null && predicate.test(ele)) {
                return ele;
            }
        }
        return null;
    }

    /**
     * 获取集合中满足指定条件的第一个元素的下标
     * @param list 集合
     * @param predicate 条件
     * @return boolean
     * @author zak
     * @date 2020/8/24 9:39
     */
    public static <T> Integer getIndex(final List<T> list, final Predicate<T> predicate) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && predicate.test(list.get(i))) {
                return i;
            }
        }
        return null;
    }


    public static <T> List<T> sub(final Collection<T> collection, final Function<T, Object> function, final Object value) {
        return sub(collection, ele -> Objects.equals(function.apply(ele), value));
    }

    /**
     * 获取集合中满足指定条件的所有元素
     * @param collection 集合
     * @param predicate 条件
     * @return boolean
     * @author zak
     * @date 2020/8/24 9:39
     */
    public static <T> List<T> sub(final Collection<T> collection, final Predicate<T> predicate) {
        List<T> list = new ArrayList<>();

        for (T ele : collection) {
            if (ele != null && predicate.test(ele)) {
                list.add(ele);
            }
        }

        return list;
    }

    public static <T> T computeIfAbsent(Collection<T> collection, Function<T, Object> function, Object value, Supplier<T> supplier) {
        T ret = get(collection, function, value);
        if (ret == null) {
            ret = supplier.get();
            collection.add(ret);
        }
        return ret;
    }



    // region 笛卡尔积

    /**
     * 笛卡尔积
     *
     * @param lists
     * @return void
     * @author zak
     * @date 2019/11/29 18:03
     */
    public static <T> List<List<T>> cartesianProduct(final List<List<T>> lists) {
        T[][] arrays = (T[][]) new Object[lists.size()][];
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = (T[]) lists.get(i).toArray();
        }

        return cartesianProduct(arrays);
    }

    public static <T> List<List<T>> cartesianProduct(final T[][] arrays) {
        int[][] indexAndLength = new int[2][arrays.length];

        for (int i = 0; i < arrays.length; i++) {
            indexAndLength[0][i] = 0;
            indexAndLength[1][i] = arrays[i].length;
        }

        List<List<T>> cartesianProductList = new ArrayList<>();
        getOptions(arrays, indexAndLength, cartesianProductList);
        return cartesianProductList;
    }


    private static <T> void getOptions(final T[][] arrays, int[][] indexAndLength, List<List<T>> cartesianProductList) {
        List<T> ret = new ArrayList<>(arrays.length);
        cartesianProductList.add(ret);
        for (int i = 0; i < arrays.length; i++) {
            ret.add(arrays[i][indexAndLength[0][i]]);
        }

        if (addIndex(indexAndLength, arrays.length)) {
            getOptions(arrays, indexAndLength, cartesianProductList);
        }
    }

    private static boolean addIndex(int[][] indexAndLength, int index) {
        if (index <= 0) {
            return false;
        }

        if ((indexAndLength[0][index - 1] += 1) < indexAndLength[1][index - 1]) {
            return true;
        }
        indexAndLength[0][index - 1] = 0;
        return addIndex(indexAndLength, index - 1);
    }

    // endregion 笛卡尔积

    /**
     * 判断集合是否全为null
     *
     * @param collection Collection
     * @return
     */
    public static boolean isAllNull(final Collection<?> collection) {
        for (Object o : collection) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 在两个不同的泛型的集合中根据指定的比较器比较两个集合中的元素是否相同
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param comparator  比较器
     * @param sameSize    是否规定两个集合的大小相同  如果不规定,则以较小的集合为主进行比较
     * @return boolean
     * @author zak
     * @date 2019/12/26 17:43
     */
    public static <E1, E2> boolean crossClassMatch(Collection<E1> collection1, Collection<E2> collection2, CrossClassComparator<E1, E2> comparator, boolean sameSize) {

        if (sameSize && collection1.size() != collection2.size()) {
            return false;
        }

        final Iterator<E1> iterator1 = collection1.iterator();
        final Iterator<E2> iterator2 = collection2.iterator();
        Iterator mainIterator = collection1.size() < collection2.size()
                ? iterator1
                : iterator2;
        while (mainIterator.hasNext()) {
            if (comparator.compare(iterator1.next(), iterator2.next()) != 0) {
                return false;
            }
        }

        return true;

    }

    /**
     * 在两个不同的泛型的集合中根据指定的比较器比较两个集合中的元素是否相同
     *
     * @param collection1
     * @param collection2
     * @param comparator
     * @return boolean
     * @author zak
     * @date 2019/12/26 17:55
     */
    public static <E1, E2> boolean crossClassMatch(Collection<E1> collection1, Collection<E2> collection2, CrossClassComparator<E1, E2> comparator) {
        return crossClassMatch(collection1, collection2, comparator, true);
    }

    /**
     * 在两个不同的泛型的集合中根据指定的比较器比较两个集合中的元素是否相同
     * 但是不要求相同的对象所在的下标也一样
     *
     * @param collection1
     * @param collection2
     * @param comparator
     * @param sameSize
     * @return boolean
     * @author zak
     * @date 2019/12/26 18:07
     */
    public static <E1, E2> boolean crossClassBroadMatch(Collection<E1> collection1, Collection<E2> collection2,
                                                        CrossClassComparator<E1, E2> comparator, boolean sameSize) {
        if (sameSize && collection1.size() != collection2.size()) {
            return false;
        }


        boolean flg = collection1.size() < collection2.size()
                ? true
                : false;

        List list = new ArrayList();
        if (flg) {
            for (E1 e1 : collection1) {
                for (E2 e2 : collection2) {
                    boolean eq = false;
                    if (comparator.compare(e1, e2) == 0) {
                        if (contains(list, o -> o == e2)) {
                            continue;
                        }
                        list.add(e2);
                        eq = true;
                    }
                    if (eq == false) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
        } else {
            for (E2 e2 : collection2) {
                for (E1 e1 : collection1) {
                    boolean eq = false;
                    if (comparator.compare(e1, e2) == 0) {
                        if (contains(list, o -> o == e1)) {
                            continue;
                        }
                        list.add(e1);
                        eq = true;
                    }
                    if (eq == false) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
        }


        return true;
    }


    public static <E1, E2> boolean crossClassBroadMatch(Collection<E1> collection1, Collection<E2> collection2,
                                                        CrossClassComparator<E1, E2> comparator) {
        return crossClassBroadMatch(collection1, collection2, comparator, true);
    }


    /**
     *
     * @param collection
     * @param predicate
     * @return java.util.Collection<E>
     * @author zak
     * @date 2020/8/24 9:44
     */
    public static <E> Collection<E> removeAll(Collection<E> collection, Predicate<E> predicate) {
        collection.removeAll(sub(collection, predicate));
        return collection;
    }

    /**
     * 将targetList中的元素安装sourceList中的元素的顺序进行排序
     *
     * @param sourceList 源list
     * @param targetList 目标list
     * @param function1  源list中元素顺序的参考字段
     * @param function2  目标list中元素顺序的参考字段
     * @return java.util.List<E2>
     * @author zak
     * @date 2019/12/27 16:07
     */
    public static <E1, E2> List<E2> shadowSort(
            final List<E1> sourceList,
            final Function<E1, Object> function1,
            final List<E2> targetList,
            final Function<E2, Object> function2
    ) {
        targetList.sort((o1, o2) -> Optional.ofNullable(getIndex(sourceList, e1 -> Objects.equals(function1.apply(e1), function2.apply(o1)))).orElse(Integer.MAX_VALUE)
                - Optional.ofNullable(getIndex(sourceList, e1 -> Objects.equals(function1.apply(e1), function2.apply(o2)))).orElse(Integer.MAX_VALUE));
        return targetList;
    }

    public static <E1> List<E1> shadowSort(
            final List<E1> sourceList,
            final List<E1> targetList,
            final Function<E1, Object> function
    ) {
        return shadowSort(sourceList, function, targetList, function);
    }

    public static <E1> List<E1> shadowSort(
            final List<E1> sourceList,
            final List<E1> targetList
    ) {
        return shadowSort(sourceList, targetList, e -> e);
    }

    /**
     * 转换list
     * @param list
     * @param converter
     * @return java.util.List<R>
     * @author zak
     * @date 2020/8/24 10:22
     */
    public static <E, R> List<R> convert(List<E> list, Function<E, R> converter) {
        return list.stream().map(converter::apply).collect(Collectors.toList());
    }


    /**
     * 两个不同的类的对象进行比较
     */
    @FunctionalInterface
    public interface CrossClassComparator<T1, T2> {
        /**
         * 相同返回0 o1比o2小返回负数, o1比o2大返回正数
         *
         * @param o1
         * @param o2
         * @return int
         * @author zak
         * @date 2019/12/26 17:46
         */
        int compare(T1 o1, T2 o2);
    }
}

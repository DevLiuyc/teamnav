package com.lyc.teamnav.bean.entity;

import java.io.Serializable;

public interface ISortEntity<T> extends Serializable {

    /**
     * getSort
     *
     * @return Integer
     */
    Integer getSort();

    /**
     * setSort
     *
     * @param sort v
     * @return T
     */
    T setSort(Integer sort);

}

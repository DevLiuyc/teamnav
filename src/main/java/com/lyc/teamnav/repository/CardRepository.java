package com.lyc.teamnav.repository;

import com.lyc.teamnav.bean.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    /**
     * findByTag.
     *
     * @param category category
     * @return List
     */
    List<Card> findByCategoryOrderBySortAsc(String category);

    /**
     * findByTitleLikeOrContentLike.
     *
     * @param keyword keyword
     * @return List
     */
    @Query("select u from Card u "
            + "where lower(u.title) like %?1% or lower(u.content) like %?1%")
    List<Card> findByKeywords(String keyword);

    /**
     * getMaxSort
     *
     * @param category category
     * @return Integer
     */
    @Query("select coalesce(max(sort),0) from Card where category = ?1")
    Integer getMaxSort(String category);

    /**
     * deleteByCategory
     *
     * @param categoryId categoryId
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from Card where category = ?1")
    void deleteByCategoryId(String categoryId);
}

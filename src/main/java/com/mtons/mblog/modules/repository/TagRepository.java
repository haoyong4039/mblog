package com.mtons.mblog.modules.repository;

import com.mtons.mblog.modules.data.TagVO;
import com.mtons.mblog.modules.data.TagsVO;
import com.mtons.mblog.modules.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : langhsu
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    Tag findByName(String name);

    @Modifying
    @Query("update Tag set posts = posts - 1 where id in (:ids) and posts > 0")
    int decrementPosts(@Param("ids") Collection<Long> ids);

    @Query("SELECT DISTINCT mt.name FROM Channel mc JOIN Post mp ON mc.id=mp.channelId JOIN PostTag mpt ON mp.id=mpt.postId JOIN Tag mt ON mpt.tagId=mt.id WHERE mp.tags=mt.name AND mc.id=:channelId AND mp.state in (:states)")
    List<String> findTagsByChannelId(@Param("channelId") int channelId, @Param("states") Collection<Integer> states);
}

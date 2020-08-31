package com.mtons.mblog.modules.service;

import com.mtons.mblog.modules.data.PostTagVO;
import com.mtons.mblog.modules.data.TagVO;
import com.mtons.mblog.modules.data.TagsVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : langhsu
 */
public interface TagService {
    Page<TagVO> pagingQueryTags(Pageable pageable);
    Page<PostTagVO> pagingQueryPosts(Pageable pageable, String tagName);
    void batchUpdate(String names, long latestPostId);
    void deteleMappingByPostId(long postId);

    List<String> findTagsByChannelId(int channelId, int agentState);
}

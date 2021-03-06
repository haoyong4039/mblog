/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.web.controller.site;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.utils.MarkdownUtils;
import com.mtons.mblog.modules.data.PostVO;
import com.mtons.mblog.modules.data.TagVO;
import com.mtons.mblog.modules.data.TagsVO;
import com.mtons.mblog.modules.entity.Channel;
import com.mtons.mblog.modules.entity.PostAttribute;
import com.mtons.mblog.modules.service.ChannelService;
import com.mtons.mblog.modules.service.PostService;
import com.mtons.mblog.modules.service.TagService;
import com.mtons.mblog.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Channel 主页
 * @author langhsu
 *
 */
@Controller
public class ChannelController extends BaseController {
	@Autowired
	private ChannelService channelService;
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;

	@RequestMapping("/channel/{id}")
	public String channel(@PathVariable Integer id, ModelMap model,
			HttpServletRequest request) {
		System.out.println(77777);
		// init params
//		String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
	    String tag = ServletRequestUtils.getStringParameter(request, "tag", "all");
		int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		int agentState = request.getSession().getAttribute("agentState") == null ? 0 : Integer.valueOf(request.getSession().getAttribute("agentState")+"");

		Channel channel = channelService.getById(id);
		List<String> tags = tagService.findTagsByChannelId(id,agentState);

		// callback params
		model.put("channel", channel);
//		model.put("order", order);
		model.put("tag", tag);
		model.put("pageNo", pageNo);
		model.put("agentState", agentState);
		model.put("tags", tags);
		return view(Views.POST_INDEX);
	}

	@RequestMapping("/post/{id:\\d*}")
	public String view(@PathVariable Long id, ModelMap model) {
		PostVO view = postService.get(id);

		Assert.notNull(view, "该文章已被删除");

		if ("markdown".endsWith(view.getEditor())) {
			PostVO post = new PostVO();
			BeanUtils.copyProperties(view, post);
			post.setContent(MarkdownUtils.renderMarkdown(view.getContent()));
			view = post;
		}
		postService.identityViews(id);
		model.put("view", view);
		return view(Views.POST_VIEW);
	}
}

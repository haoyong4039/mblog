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

import javax.servlet.http.HttpServletRequest;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.utils.MD5;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtons.mblog.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
public class IndexController extends BaseController{
	
	public static final String MARK_TRUE = "true";
	
	@RequestMapping(value= {"/", "/index"})
	public String root(ModelMap model, HttpServletRequest request) {
		String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
		int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		
		// 是否代理商 0否 1是  默认0
		int agentState = 0;
		String mark = ServletRequestUtils.getStringParameter(request, "mark", "");
		// 公众号访问，切换账号session无法清除
		if (!StringUtils.isEmpty(mark)) {
			request.getSession().removeAttribute("agentState");
		}
		if (request.getSession().getAttribute("agentState") != null) {
			agentState = Integer.valueOf(request.getSession().getAttribute("agentState").toString());
		} else {
			agentState = getAgentState(mark);
			request.getSession().setAttribute("agentState", agentState);
		}
		
		model.put("order", order);
		model.put("pageNo", pageNo);
		model.put("agentState", agentState);
		return view(Views.INDEX);
	}
	
	private int getAgentState(String mark) {
		if (!StringUtils.isEmpty(mark) && mark.length() > 32) {
			SecurityUtils.getSubject().getSession(true).setAttribute("mark", mark);
			String md5State = MD5.encrypt(MARK_TRUE);
			if (md5State.equals(mark.substring(0, 32))) {
				return 1;
			}
		}
		return 0;
	}
}

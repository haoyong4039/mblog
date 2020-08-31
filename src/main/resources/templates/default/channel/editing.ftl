<#include "/default/inc/layout.ftl"/>
<@layout "编辑文章">

<form id="submitForm" class="form" action="${base}/post/submit" method="post" enctype="multipart/form-data">
    <input type="hidden" name="status" value="${view.status!0}"/>
    <input type="hidden" name="editor" value="${editor!'tinymce'}"/>
    <div class="row">
        <div class="col-xs-12 col-md-8">
            <div id="message"></div>
            <#if view??>
                <input type="hidden" name="id" value="${view.id}"/>
                <input type="hidden" name="authorId" value="${view.authorId}"/>
            </#if>
            <input type="hidden" id="thumbnail" name="thumbnail" value="${view.thumbnail}"/>

            <div class="form-group">
                <input type="text" class="form-control" name="title" maxlength="128" value="${view.title}" placeholder="请输入标题" required>
            </div>
            <div class="form-group">
                <select class="form-control" name="channelId" required>
                    <option value="">请选择栏目</option>
                    <#list channels as row>
                        <option value="${row.id}" <#if (view.channelId == row.id)> selected </#if>>${row.name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <#include "/default/channel/editor/${editor}.ftl"/>
            </div>
        </div>
        <div class="col-xs-12 col-md-4">
            <div class="panel panel-default corner-radius help-box">
                <div class="thumbnail-box">
                    <div class="convent_choice" id="thumbnail_image" <#if view.thumbnail?? && view.thumbnail?length gt 0> style="background: url(<@resource src=view.thumbnail/>);" </#if>>
                        <div class="upload-btn">
                            <label>
                                <span>点击选择一张图片</span>
                                <input id="upload_btn" type="file" name="file" accept="image/*" title="点击添加图片">
                            </label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default corner-radius help-box">
                <div class="panel-heading">
                    <h3 class="panel-title">标签(用逗号或空格分隔)</h3>
                </div>
                <div class="panel-body">
                    <input type="text" id="tags" name="tags" class="form-control" value="${view.tags}" placeholder="添加相关标签，逗号分隔 (最多4个)">
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">可见范围选择</h3>
                </div>
                <div class="panel-body">
                    <div style="font-size: 16px">
                        <#if view.state==null>
                            <input type="radio"  id="isAgent" name = "state" value="0" align="center" checked="checked"/>公开
                            <input type="radio"  id="isAgent" name = "state" value="1" align="center" style="margin-left: 10px"/>仅为代理商可见
                        </#if>
                        <#if view.state==0>
                            <input type="radio"  id="isAgent" name = "state" value="0" align="center" checked="checked"/>公开
                            <input type="radio"  id="isAgent" name = "state" value="1" align="center" style="margin-left: 10px"/>仅为代理商可见
                        </#if>
                        <#if view.state==1>
                            <input type="radio"  id="isAgent" name = "state" value="0" align="center"/>公开
                            <input type="radio"  id="isAgent" name = "state" value="1" align="center" checked="checked" style="margin-left: 10px"/>仅为代理商可见
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xs-12 col-md-8">
            <div class="form-group">
                <div class="text-right">
                    <button type="button" data-status="0" class="btn btn-primary" event="post_submit" style="padding-left: 15px; padding-right: 15px;">发布</button>
                </div>
            </div>
    </div>
</form>
<!-- /form-actions -->
<script type="text/javascript">
seajs.use('post', function (post) {
	post.init();
});
</script>
</@layout>

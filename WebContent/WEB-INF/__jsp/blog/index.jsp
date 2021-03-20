<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('blog_index.page_title')}
  page.keywords=${ctx.esc('blog_index.page_keywords')}
  page.desc=${ctx.esc('blog_index.page_desc')}
  page.robots=${empty pageContext.request.queryString ? "index, follow" : "noindex, follow"}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('blog_index.title_head')}</h1>
    <p class="lead mb-0">${ctx.escCt('blog_index.title_text')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col-md-8">

      <c:if test="${empty model.blogPosts}">
        <p class="mb-3 px-3 py-2 rounded messages-notice">${ctx.escCt('blog_index.no_post_message')}</p>
      </c:if>

      <t:iterate items="${model.blogPosts}" var="post">
        <div class="mb-5">

          <div class="d-flex w-100 justify-content-between mb-3">
            <h2 class="font-sl3 font-w5 mb-0">
              <t:actionLink action="post" __blogPostId="${post.blogPostId}" __title_path="${post.title_path}">                  
                    ${fx:escCt(post.titleText)}
              </t:actionLink>
            </h2>
            <small class="lower text-nowrap"><t:fmtDate value="${post.timeCreated}" /></small>
          </div>

          <p class="mb-3">
            <t:iterate items="${post.tagList}" var="tag">
              <t:actionLink action="index" __tag="${tag}" clazz="tag-link mr-3">${fx:escCt(tag)}</t:actionLink>
            </t:iterate>
          </p>

          <p class="mb-3">
            ${post.stp_content}
            <span class="mx-2">&hellip;</span>

            <t:actionLink action="post" __blogPostId="${post.blogPostId}" __title_path="${post.title_path}" clazz="lower font-sm1 font-italic text-muted">                  
                ${ctx.escCt('label.detail')}
            </t:actionLink>
          </p>
        </div>
      </t:iterate>

      <c:if test="${model.pagerModel.pageCount gt 1}">

        <t:pager model="${model.pagerModel}" tag="ul" clazz="pagination justify-content-center mb-4">
          <t:tpl type="page">
            <li class="page-item${selected ? ' active' : ''}">
              <t:actionLink clazz="page-link" action="index" __tag="${model.tag}" __pageIndex="${item.index}">${label}</t:actionLink>
            </li>
          </t:tpl>
          <t:tpl type="dots">
            <li class="page-item">
              <a href="#" class="page-link">...</a>
            </li>
          </t:tpl>
        </t:pager>
      </c:if>

    </div>

    <div class="col-md-4">

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('blog_index.tags')}</h2>

        <t:c t="ol" clazz="list-unstyled mb-0" render="${not empty model.blogTags}">
          <t:iterate items="${model.blogTags}" var="tag">
            <li class="mb-21">
              <t:actionLink action="index" __tag="${tag}" clazz="tag-link">${fx:escCt(tag)}</t:actionLink>
            </li>
          </t:iterate>
        </t:c>
      </div>

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('blog_index.signup_blog_title_head')}</h2>

        <div class="mb-0">
          <p class="mb-3">${ctx.escCt('blog_index.signup_blog_title_text')}</p>
          <p id="signUpMsg" class="mb-3 p-2 rounded d-none"></p>

          <div class="form-group mb-3">
            <t:textBox type="email" name="email" autocomplete="email" enterBtn="btnSignUp" class="form-control"
              placeholder="${ctx.esc('hint.enter_your_email')}" />
          </div>

          <t:button id="btnSignUp" clazz="btn btn-primary px-4" labelKey="label.signup"></t:button>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {
    // initClkSearch();
    initSignUp("#btnSignUp");

  });
</script>
<!-- @jsSection end -->
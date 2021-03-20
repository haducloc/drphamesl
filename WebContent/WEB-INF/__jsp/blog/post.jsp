<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${fx:esc(model.titleText)}
  page.keywords=${fx:esc(model.keywords)}
  page.desc=${fx:esc(model.descText)}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">
      <t:actionLink action="index">${ctx.escCt('blog_index.title_head')}</t:actionLink>
    </h1>
    <p class="lead mb-0">${ctx.escCt('blog_index.title_text')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col-md-8">

      <div class="mb-5">
        <div class="d-flex w-100 justify-content-between mb-3">
          <h2 class="font-sl3 font-w5 mb-0">${fx:escCt(model.titleText)}</h2>

          <small class="lower text-nowrap"><t:fmtDate value="${model.timeCreated}" /></small>
        </div>

        <p class="mb-3">
          <t:iterate items="${model.tagList}" var="tag">
            <t:actionLink action="index" __tag="${tag}" clazz="tag-link mr-3">
              ${fx:escCt(tag)}
            </t:actionLink>
          </t:iterate>
        </p>

        <div class="mb-3">${model.contentText}</div>

        <t:c t="div" clazz="d-flex w-100 justify-content-end mb-3" render="${not empty model.fbUrl}">
          <div class="d-inline-flex" role="group">
            <a href="${model.fbUrl}" class="lower font-sm1 font-italic">${ctx.escCt('label.comment_fb')}</a>
          </div>
        </t:c>
        
      </div>
    </div>

    <div class="col-md-4">

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('blog_index.tags')}</h2>

        <t:c t="ol" clazz="list-unstyled mb-0" render="${not empty blogTags}">
          <t:iterate items="${blogTags}" var="tag">
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
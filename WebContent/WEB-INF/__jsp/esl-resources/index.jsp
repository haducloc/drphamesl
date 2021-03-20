<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('esl_resources_index.page_title')}
  page.keywords=${ctx.esc('esl_resources_index.page_keywords')}
  page.desc=${ctx.esc('esl_resources_index.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('esl_resources_index.title_head')}</h1>
    <p class="lead mb-0">${ctx.fmtEscCt('esl_resources_index.title_text', '@{drPhamSpan}')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">

      <div class="mb-4">
        ${ctx.res('esl_resources_index.resources_text')}

        <t:c t="p" render="${empty @(principalPath)}">
          ${ctx.fmtEscCt('esl_resources_index.login_signup_message', model.loginLink, model.signupLink)}
         </t:c>
      </div>

      <t:c t="div" clazz="accordion mb-4" id="accordion1" render="${not empty @(principalPath) and not empty model.eslRess}">
        <t:iterate items="${model.eslRess}" var="item">

          <div class="mb-21">
            <h2 id="heading${item.pk}" class="collapse-heading mb-2">
              <button class="btn btn-link btn-block p-1 text-left collapsed" type="button" data-toggle="collapse" data-target="#collapse${item.pk}"
                aria-expanded="false" aria-controls="collapse${item.pk}">
                <i class="sym sym-plus"></i>${fx:escCt(item.titleText)}</button>
            </h2>
            <div id="collapse${item.pk}" class="collapse" aria-labelledby="heading${item.pk}" data-parent="#accordion1">
              <div class="border p-3">${item.descText}</div>
            </div>
          </div>
        </t:iterate>
      </t:c>

    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    $(".collapse").on('show.bs.collapse', function() {
      $(this).prev(".collapse-heading").find(".sym").removeClass("sym-plus").addClass("sym-minus");
    }).on('hide.bs.collapse', function() {
      $(this).prev(".collapse-heading").find(".sym").removeClass("sym-minus").addClass("sym-plus");
    });

  });
</script>
<!-- @jsSection end -->
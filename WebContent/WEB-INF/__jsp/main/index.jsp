<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('service_index.page_title')}
  page.keywords=${ctx.esc('service_index.page_keywords')}
  page.desc=${ctx.esc('service_index.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">DrPhamESL</h1>
    <p class="lead text-center mb-0">${ctx.escCt('service_index.title_text')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col-md-8">

      <div class="accordion" id="accordion">
        <t:iterate items="${serviceCats}" var="cat">

          <div class="mb-41">
            <h2 class="font-sl3 font-w5 mb-31">${fx:escCt(cat.titleText)}</h2>

            <t:iterate items="${cat.services}" var="item">
              <div class="mb-2">

                <h3 id="heading${item.pk}" class="collapse-heading mb-2">
                  <button class="btn btn-link btn-block p-1 text-left collapsed" type="button" data-toggle="collapse" data-target="#collapse${item.pk}"
                    aria-expanded="false" aria-controls="collapse${item.pk}">
                    <i class="sym sym-plus"></i>${fx:escCt(item.titleText)}</button>
                </h3>

                <div id="collapse${item.pk}" class="collapse" aria-labelledby="heading${item.pk}" data-parent="#accordion">
                  <div class="border p-3">
                    <dl class="row mb-0">
                      <dt class="col-md-3">${ctx.escCt('service.learning_method')}</dt>
                      <dd class="col-md-9">${ctx.escCt('service.learning_mth_online')}</dd>

                      <dt class="col-md-3">${ctx.escCt('service.desc')}</dt>
                      <dd class="col-md-9">${item.descText}</dd>

                      <dt class="col-md-3">${ctx.escCt('service.tuition')}</dt>
                      <dd class="col-md-9 mb-0">${item.tuitionText}</dd>
                    </dl>
                  </div>
                </div>
              </div>
            </t:iterate>
          </div>
        </t:iterate>
      </div>
    </div>

    <div class="col-md-4">

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('service_index.signup_news_title_head')}</h2>

        <div class="mb-0">
          <p class="mb-3">${ctx.escCt('service_index.signup_news_title_text')}</p>
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
    initSignUp("#btnSignUp");

    $(".collapse").on('show.bs.collapse', function() {
      $(this).prev(".collapse-heading").find(".sym").removeClass("sym-plus").addClass("sym-minus");
    }).on('hide.bs.collapse', function() {
      $(this).prev(".collapse-heading").find(".sym").removeClass("sym-minus").addClass("sym-plus");
    });
  });
</script>
<!-- @jsSection end -->
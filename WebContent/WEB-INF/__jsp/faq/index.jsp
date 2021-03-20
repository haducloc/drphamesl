<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('faq_index.page_title')}
  page.keywords=${ctx.esc('faq_index.page_keywords')}
  page.desc=${ctx.esc('faq_index.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('faq_index.title_head')}</h1>
    <p class="lead mb-0">${ctx.fmtEscCt('faq_index.title_text', model.contactLink, '@{drPhamSpan}')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">

      <div class="accordion mb-4" id="accordion1">

        <t:iterate items="${model.faqs}" var="item">
          <div class="mb-21">
            <h2 id="heading${item.pk}" class="collapse-heading mb-2">
              <button class="btn btn-link btn-block p-1 text-left collapsed" type="button" data-toggle="collapse" data-target="#collapse${item.pk}"
                aria-expanded="false" aria-controls="collapse${item.pk}">
                <i class="sym sym-plus"></i>${fx:escCt(item.questText)}</button>
            </h2>
            <div id="collapse${item.pk}" class="collapse" aria-labelledby="heading${item.pk}" data-parent="#accordion1">
              <div class="border p-3">${item.ansText}</div>
            </div>
          </div>
        </t:iterate>
      </div>
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
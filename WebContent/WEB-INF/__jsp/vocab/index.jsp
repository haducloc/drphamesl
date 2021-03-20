<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('vocab_index.page_title')}
  page.keywords=${ctx.esc('vocab_index.page_keywords')}
  page.desc=${ctx.esc('vocab_index.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('vocab_index.title_head')}</h1>
    <p class="lead mb-0">${ctx.fmtEscCt('vocab_index.title_text', '@{drPhamSpan}')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <t:c t="div" clazz="row" render="${not empty model.vocabLists}">
    <div class="col">

      <ul class="list-custom pl-1 mb-4">
        <t:iterate items="${model.vocabLists}">

          <li class="list-custom-item mb-31">
            <i class="sym-list-item mr-1"></i>
            <t:actionLink action="list" __pk="${item.idOrSpk}" __title_path="${item.title_path}" clazz="mr-2">
              ${fx:escCt(item.titleText)} <c:if test="${item.shareType ne 2}">&nbsp;[${item.shareType}]</c:if>
            </t:actionLink>

            <t:c t="span" clazz="badge badge-pill badge-info lower" render="${item.new}">${ctx.esc('label.new')}</t:c>
          </li>
        </t:iterate>
      </ul>

    </div>
  </t:c>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initSignUp("#btnSignUp");

  });
</script>
<!-- @jsSection end -->
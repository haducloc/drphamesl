<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${fx:esc(model.vocabList.titleText)}
  page.keywords=${fx:esc(model.vocabList.keywords)}
  page.desc=${fx:esc(model.vocabList.descText)}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">
      <t:actionLink action="index">${ctx.escCt('vocab_index.title_head')}</t:actionLink>
    </h1>
    <p class="lead mb-0">${ctx.fmtEscCt('vocab_index.title_text', '@{drPhamSpan}')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">

      <div class="mb-4 text-center">
        <h2 class="font-sl3 font-w4 mb-4">
          <span class="mr-3">${fx:escCt(model.vocabList.titleText)}</span>
          <span class="badge badge-pill badge-info">${ctx.esc('label.total_vocabs', model.vocabCount)}</span>
        </h2>

        <t:c t="div" clazz="d-inline-flex" render="${model.vocabCount gt 0}">
          <t:actionLink action="flashcard" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}"
            clazz="btn btn-primary btn-sm px-3 mr-3" role="button">${ctx.escCt('label.flash_cards')}</t:actionLink>

          <t:actionLink action="test" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}" clazz="btn btn-primary btn-sm px-3"
            role="button">${ctx.escCt('label.start_test')}</t:actionLink>
        </t:c>

        <t:c t="div" clazz="d-inline-flex" render="${model.vocabCount eq 0}">
          <a href="#" class="btn btn-primary btn-sm px-3 mr-3 disabled" tabindex="-1" role="button" aria-disabled="true">${ctx.escCt('label.flash_cards')}</a>
          <a href="#" class="btn btn-primary btn-sm px-3 disabled" tabindex="-1" role="button" aria-disabled="true">${ctx.escCt('label.start_test')}</a>
        </t:c>
      </div>

    </div>
  </div>

  <t:c t="div" clazz="row" render="${model.vocabCount gt 0}">
    <div class="col">

      <div class="mb-4 text-center">
        <p class="font-sl1">

          <t:iterate items="${model.vocabs}" var="vocab">
            <span class="clk-search words-vocab bg-white shadow rounded" title="${fx:esc(vocab.defs1Line)}">${fx:escCt(vocab.words)}</span>
          </t:iterate>
        </p>
      </div>
    </div>
  </t:c>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {
    initClkSearch();
  });
</script>
<!-- @jsSection end -->
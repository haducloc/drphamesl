<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_vocab_index.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form action="index" method="GET">
          <div class="input-group">
            <t:textBox path="model.query" clazz="form-control" fmt="QueryText" />
            <div class="input-group-append">
              <t:submitButton id="btnSearch" clazz="btn btn-primary px-4" labelKey="label.search" handleWait="false"></t:submitButton>
            </div>
          </div>

          <p class="mt-2 mb-0">
            <t:actionLink action="index" clazz="font-sl2 tag-link mr-3">X</t:actionLink>

            <t:iterate items="${vocabTagList}" var="tag">
              <a href="#" class="tag-opt tag-link mr-3">${fx:escCt(tag)}</a>
            </t:iterate>

            <button id="mytagsBtn" type="button" class="btn btn-light py-0">${ctx.escCt('label.tags')}</button>
          </p>
        </t:form>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col">
      <p class="mb-4 text-center">${ctx.escCt('manage_vocab_index.total_vocabs', model.pagerModel.recordCount)}</p>
    </div>
  </div>

  <c:if test="${model.pagerModel.pageCount gt 1}">
    <div class="row">
      <div class="col">

        <t:pager model="${model.pagerModel}" tag="ul" clazz="pagination justify-content-center mb-4">
          <t:tpl type="page">
            <li class="page-item${selected ? ' active' : ''}">
              <t:actionLink clazz="page-link" action="index" __query="${model.query}" __pageIndex="${item.index}"
                __recordCount="${model.pagerModel.recordCount}">${label}</t:actionLink>
            </li>
          </t:tpl>
          <t:tpl type="dots">
            <li class="page-item">
              <a href="#" class="page-link">...</a>
            </li>
          </t:tpl>
        </t:pager>
      </div>
    </div>
  </c:if>

  <t:iterate items="${model.vocabs}" var="vocab">
    <div class="row">
      <div class="col">

        <div class="mb-4 p-4 shadow-sm">

          <div class="d-flex mb-4">
            <div class="flex-grow-1">
              <h2 class="font-sl4 font-w6 mb-0">${f2:toClkSearch(vocab.words)}</h2>
            </div>

            <div class="d-inline-flex" role="group">
              <t2:googleTrans text="${vocab.words}" clazz="font-sm1 lower"></t2:googleTrans>

              <t:verBar clazz="ver-bar mx-2" />
              <t2:googleImg query="${vocab.imgQuery}" clazz="font-sm1 lower"></t2:googleImg>

              <t:verBar clazz="ver-bar mx-2" />
              <t:actionLink action="edit" __vocabId="${vocab.vocabId}" clazz="font-sm1 lower" target="_blank">
                ${ctx.escCt('label.edit')}
              </t:actionLink>
            </div>
          </div>

          <p class="mb-4">
            <t:iterate items="${vocab.tagList}" var="tag">
              <t:actionLink action="index" __query="${tag}" clazz="tag-link mr-3">${fx:escCt(tag)}</t:actionLink>
            </t:iterate>
          </p>

          <t:iterate items="${vocab.defGroups}" var="defGroup">
            <div class='${lastIndex ? "mb-0" : "mb-3" }'>
              <c:if test="${not empty defGroup.clazz}">
                <p class="words-class mb-1">${fx:escCt(defGroup.clazz)}</p>
              </c:if>
              <p class="words-def mb-1">
                <span class="mr-2">${f2:toClkSearchSt(defGroup.def)}</span>
                <t2:googleTrans text="${defGroup.def}" clazz="font-sm1 lower"></t2:googleTrans>
              </p>
              <t:iterate items="${defGroup.sents}" var="sent">
                <p class="words-sent mb-1">
                  <span class="mr-2">${f2:toClkSearchSt(sent)}</span>
                  <t2:googleTrans text="${sent}" clazz="font-sm1 lower"></t2:googleTrans>
                </p>
              </t:iterate>
            </div>
          </t:iterate>

        </div>
      </div>
    </div>
  </t:iterate>

  <c:if test="${model.pagerModel.pageCount gt 1}">
    <div class="row">
      <div class="col">

        <t:pager model="${model.pagerModel}" tag="ul" clazz="pagination justify-content-center mb-4">
          <t:tpl type="page">
            <li class="page-item${selected ? ' active' : ''}">
              <t:actionLink clazz="page-link" action="index" __query="${model.query}" __pageIndex="${item.index}"
                __recordCount="${model.pagerModel.recordCount}">${label}</t:actionLink>
            </li>
          </t:tpl>
          <t:tpl type="dots">
            <li class="page-item">
              <a href="#" class="page-link">...</a>
            </li>
          </t:tpl>
        </t:pager>
      </div>
    </div>
  </c:if>
</div>

<%@ include file="../includes/mytags_dialog.jsp"%>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {
    initClkSearch();

    $(".tag-opt").click(function() {
      $("#query").val($(this).text());
      $("#btnSearch")[0].click();
    });

    $("#mytagsBtn").click(function() {

      ajax("GET", "<t:action action='mytags' />", null, null, false, function(res) {

        var dlg = $("#mytagsDlg");
        $(".modal-body", dlg).html(res);
        dlg.modal('show');

      }, function(err) {
      }, function() {
      });
    });

  });
</script>
<!-- @jsSection end -->
<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.escCt('label.flash_cards')} - ${fx:esc(model.vocabList.titleText)}
  page.keywords=${fx:esc(model.vocabList.keywords)}
  page.desc=${fx:esc(model.vocabList.descText)}
  page.robots=noindex, nofollow
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('label.flash_cards')}</h1>
    <p class="lead mb-0">
      <t:actionLink action="list" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}" clazz="mr-3">${fx:escCt(model.vocabList.titleText)}</t:actionLink>
      <span class="badge badge-pill badge-info">${ctx.esc('label.total_vocabs', model.recordCount)}</span>
    </p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">

      <t:c t="div" id="alert" clazz="alert alert-info alert-dismissible fade show mb-4" role="alert" render="${not fx:hasCookie(pageContext, 'dblclickSearch')}">
        ${ctx.res('message.word_dblclick_definition')}
        
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </t:c>

      <div class="mb-4 p-4 shadow-sm">

        <t:form action="flashcard" method="GET" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group row">
                <t:fieldLabel field="vocabOrder" clazz="col-md-3 col-form-label" labelKey="flashcardModel.vocabOrder" />
                <div class="col-md-9">
                  <t:select path="model.vocabOrder" items="${model.vocabOrders}" clazz="form-control" triggerSubmit="true" />
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group row">
                <t:fieldLabel field="nextType" clazz="col-md-5 col-form-label" labelKey="flashcardModel.nextType" />
                <div class="col-md-7">
                  <t:select path="model.nextType" items="${model.nextTypes}" clazz="form-control" triggerSubmit="true" />
                </div>
              </div>
            </div>
          </div>
        </t:form>
      </div>
    </div>
  </div>

  <t:c t="div" clazz="row" render="${not empty model.vocab}">
    <div class="col">

      <div class="mb-4 p-4 shadow-sm">

        <div class="d-flex mb-4">
          <div class="flex-grow-1">
            <h2 class="font-sl6 font-w6 mb-0">${f2:toClkSearch(model.vocab.words)}</h2>
          </div>
          <div class="d-inline-flex" role="group">
            <t2:googleTrans text="${model.vocab.words}" clazz="font-sm1 lower"></t2:googleTrans>

            <t:verBar clazz="ver-bar mx-2" />
            <t2:googleImg query="${model.imgQuery}" clazz="font-sm1 lower"></t2:googleImg>

            <c:if test="${f2:isAdminUser(pageContext.request.userPrincipal)}">
              <t:verBar clazz="ver-bar mx-2" />
              <t:actionLink action="edit" controller="manage-vocab" __vocabId="${model.vocab.vocabId}" clazz="font-sm1 lower" target="_blank">
                 ${ctx.escCt('label.edit')}
              </t:actionLink>
            </c:if>
          </div>
        </div>

        <div class="collapse mb-4" id="defGroups">
          <t:iterate items="${model.vocab.defGroups}" var="defGroup">

            <div class="mb-3">
              <t:c t="p" clazz="words-class mb-1" render="${not empty defGroup.clazz}">
                ${fx:escCt(defGroup.clazz)}
              </t:c>

              <p class="words-def mb-1">
                <span class="mr-2">${f2:toClkSearchSt(defGroup.def)}</span>
                <t2:googleTrans text="${defGroup.def}" clazz="font-sm1 lower"></t2:googleTrans>
              </p>

              <t:iterate items="${defGroup.sents}" var="sent">
                <p class="words-sent mb-1">
                  <span class="mr-2">${f2:toClkSearchSt(sent)}</span>
                  <t2:googleTrans text="${sent}" clazz="font-sm1 lower font-normal"></t2:googleTrans>
                </p>
              </t:iterate>
            </div>
          </t:iterate>
        </div>

        <div class="mb-0">
          <t:buttonLink id="btnNext" action="flashcard" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}"
            __vocabOrder="${model.vocabOrder}" __nextType="${model.nextType}" __index="${model.nextIndex}" clazz="btn btn-primary px-5" autofocus="true">

            <span class="mr-2">${ctx.escCt('label.next')}</span>
            <t:c t="span" clazz="mr-2 font-sm1" render="${model.vocabOrder eq 2}">(${model.nextIndex}/${model.recordCount})</t:c>
            <span id="countDownNext" class="px-2 rounded-pill border border-info font-sm2" style="display: none;"></span>
          </t:buttonLink>
        </div>
      </div>
    </div>
  </t:c>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">

  function countDown(remaining, seconds, spanSel) {
    var jqCt = $(spanSel);
    if (remaining == seconds - 1) jqCt.show();
    if (remaining > 0) jqCt.text(remaining); else jqCt.hide();
  }
  
  $(document).ready(function() {

    initClkSearch();

    setTimeout(function() {
      $("#defGroups").collapse('show');      
    }, 3500);

    $('#defGroups').on('shown.bs.collapse', function () {
      $("#btnNext")[0].scrollIntoView({behavior: "smooth"});
    });
    
    <c:if test="${model.nextInMs gt 0}">
      initCountDown(${model.nextInMs} / 1000, "#countDownNext", countDown);
      
      setTimeout(function() {
        $("#btnNext")[0].click();
      }, 
      ${model.nextInMs});
    </c:if>

    $('#alert').on('closed.bs.alert', function () {
      setCookie("dblclickSearch", "true", 1800, "/");
    });
    
  });
</script>
<!-- @jsSection end -->
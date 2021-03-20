<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.escCt('label.start_test')} - ${fx:esc(model.vocabList.titleText)}
  page.keywords=${fx:esc(model.vocabList.keywords)}
  page.desc=${fx:esc(model.vocabList.descText)}
  page.robots=noindex, nofollow
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('label.start_test')}</h1>
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

        <t:form action="test" method="GET" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group row">
                <t:fieldLabel field="testOrder" clazz="col-md-4 col-form-label" labelKey="vocabTestModel.testOrder" />
                <div class="col-md-8">
                  <t:select path="model.testOrder" items="${model.testOrders}" clazz="form-control" triggerSubmit="true" />
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group row">
                <t:fieldLabel field="nextType" clazz="col-md-4 col-form-label" labelKey="vocabTestModel.nextType" />
                <div class="col-md-8">
                  <t:select path="model.nextType" items="${model.nextTypes}" clazz="form-control" triggerSubmit="true" />
                </div>
              </div>
            </div>
          </div>
        </t:form>
      </div>
    </div>
  </div>

  <t:c t="div" clazz="row" render="${not empty model.vocabs}">
    <div class="col">

      <div class="mb-4 p-4 shadow-sm">

        <div class="mb-3 font-sl0">
          <t:iterate items="${model.vocab.markedDefs}" var="def">

            <p class="words-def words-def-marked mb-21">
              <span class="mr-2">${f2:toClkSearchSt(def)}</span>

              <span class="d-inline-flex" role="group">
                <t2:googleTrans text="${def}" clazz="font-sm1 lower font-w4"></t2:googleTrans>

                <c:if test="${f2:isAdminUser(pageContext.request.userPrincipal)}">
                  <t:verBar clazz="ver-bar mx-2" />
                  <t:actionLink action="edit" controller="manage-vocab" __vocabId="${model.vocab.vocabId}" clazz="font-sm1 lower" target="_blank">
                    ${ctx.escCt('label.edit')}
                  </t:actionLink>
                </c:if>
              </span>
            </p>
          </t:iterate>
        </div>

        <div class="mb-4">
          <ul class="list-group">

            <t:iterate items="${model.vocabs}" var="vocab">
              <li id="li${vocab.vocabId}" class="list-group-item border-0">

                <div class="form-check pl-1">
                  <input type="radio" name="opt" id="opt${vocab.vocabId}" data-vocabid="${vocab.vocabId}" class="form-check-input"
                    style="transform: scale(1.111);" />

                  <label class="form-check-label" for="opt${vocab.vocabId}">
                    <span class="mr-2">${f2:toClkSearch(vocab.words)}</span>

                    <span class="d-inline-flex" role="group">
                      <t2:googleTrans text="${vocab.words}" clazz="font-sm1 lower"></t2:googleTrans>
                      <t:verBar clazz="ver-bar mx-2" />
                      <t2:googleImg query="${vocab.imgQuery}" clazz="font-sm1 lower"></t2:googleImg>
                    </span>
                  </label>
                </div>
              </li>
            </t:iterate>
          </ul>
        </div>

        <div class="mb-0">
          <t:buttonLink id="btnNext" action="test" __pk="${model.vocabList.idOrSpk}" __title_path="${model.vocabList.title_path}"
            __testOrder="${model.testOrder}" __nextType="${model.nextType}" __index="${model.nextIndex}" clazz="btn btn-primary px-5" autofocus="true">

            <span class="mr-2">${ctx.escCt('label.next')}</span>
            <t:c t="span" clazz="mr-2 font-sm1" render="${model.testOrder eq 2}">(${model.nextIndex}/${model.recordCount})</t:c>
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

    $("#btnNext")[0].scrollIntoView({behavior: "smooth"});
    
    initClkSearch();

    $("input[name='opt']").change(
        function() {
          var vocabId = $(this).attr("data-vocabid");
          if (vocabId == "${model.vocabId}") {
            $("#li" + vocabId).removeClass("border-0").addClass("border border-success");
          }
        }
    );
    
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
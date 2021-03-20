<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_vocab_edit.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="edit" __vocabId="${model.vocabId}" autocomplete="off" actionType="true">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <t:hidden path="model.vocabId" />
          <t:hidden path="model.timeCreated" />

          <div class="form-row">
            <div class="form-group col">
              <t:fieldLabel field="words" labelKey="vocab.words" required="true" />

              <div class="input-group">
                <t:textBox type="text" path="model.words" clazz="form-control form-control-lg" enterBtn="btnLoad" autofocus="${empty model.words}" />
                <div class="input-group-append">
                  <button id="btnLoad" type="button" class="btn btn-secondary">&#8681;</button>
                </div>
              </div>
              <t:fieldError field="words" />
            </div>
          </div>

          <div class="form-group">
            <t:fieldLabel field="tags" labelKey="vocab.tags" required="true" />
            <t:textBox path="model.tags" clazz="form-control" fmt="DbTags" />

            <p class="mt-1 mb-0">
              <t:iterate items="${vocabTagList}" var="tag">
                <a href="#" class="tag-opt tag-link${lastIndex ? '' : ' mr-3'}">${fx:escCt(tag)}</a>
              </t:iterate>
            </p>
          </div>

          <div class="form-group">
            <t:fieldLabel field="definitions" labelKey="vocab.definitions" required="true" />
            <t:textArea path="model.definitions" clazz="form-control" rows="16" />
          </div>

          <div class="form-row">
            <div class="col-auto mr-auto">
              <t:submitButton id="btnSave" clazz="btn btn-primary px-4" labelKey="label.save"></t:submitButton>
            </div>
            <div class="col-auto">
              <t:submitButton id="btnDel" actionType="remove" clazz="btn btn-danger px-4" labelKey="label.remove" render="${not empty model.pk}"></t:submitButton>
            </div>
          </div>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    $("#btnLoad").click(function() {
      var words = $("#words").val().trim();
      if (words.length == 0) {
        return;
      }
      $(this).prop("disabled", true);

      ajax("POST", "<t:action action='load' />", "words=" + encodeURIComponent(words), null, true, function(obj) {
        $("#vocabId").val(obj.vocabId);
        $("#words").val(obj.words);
        $("#definitions").val(obj.definitions);

        $("#btnLoad").removeAttr("disabled");

        $(".field-error").removeClass("field-error");
        $("span[id^=err_]").text("").css("display", "none");

        if (obj.vocabId != null) {
          if (!$("#words").prop("readonly")) {
            $("#err_words").text(obj.message).css("display", "initial");
          }
          $("#tags").val(obj.tags);
        }

      }, function(err) {
        $("#btnLoad").removeAttr("disabled");
      });
    });

    $(".tag-opt").click(function() {
      var jqTags = $("#tags");
      jqTags.val(updateTags(jqTags.val(), $(this).text()));
      jqTags.focus();
    });

    initButtons("#btnSave,#btnDel");

  });
</script>
<!-- @jsSection end -->
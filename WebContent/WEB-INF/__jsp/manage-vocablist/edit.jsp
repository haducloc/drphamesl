<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_vocablist_edit.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="edit" __vocabListId="${model.vocabListId}" autocomplete="off" actionType="true">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <t:hidden path="model.spk" />
          <t:hidden path="model.timeCreated" />

          <div class="form-group">
            <t:fieldLabel field="tag" labelKey="vocabList.tag" required="true" />
            <t:textBox path="model.tag" clazz="form-control" readonly="${not empty model.vocabListId}" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="titleText" labelKey="vocabList.titleText" required="true" />
            <t:textBox path="model.titleText" clazz="form-control" />
            <small class="form-text text-muted">${ctx.escCt('page_meta.title_rules')}</small>
          </div>

          <div class="form-group">
            <t:fieldLabel field="keywords" labelKey="vocabList.keywords" required="true" />
            <t:textBox path="model.keywords" clazz="form-control" maxlength="255" />
            <small class="form-text text-muted">${ctx.escCt('page_meta.keywords_rules')}</small>
          </div>

          <div class="form-group">
            <t:fieldLabel field="descText" labelKey="vocabList.descText" required="true" />
            <t:textArea path="model.descText" clazz="form-control" rows="2" maxlength="160" />
            <small class="form-text text-muted">${ctx.escCt('page_meta.desc_rules')}</small>
          </div>

          <div class="form-group">
            <t:fieldLabel field="dispPos" labelKey="vocabList.dispPos" required="true" />
            <t:textBox type="text" path="model.dispPos" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="shareType" labelKey="vocabList.shareType" required="true" />
            <t:select path="model.shareType" items="${shareTypes}" clazz="form-control" />
          </div>

          <div class="form-group">
            <div class="form-check">
              <t:checkbox submitValue="1" path="model.notified" checked="${model.notified eq 1}" readonly="true" clazz="form-check-input mr-2" />
              <label class="form-check-label" for="notified"> ${ctx.escCt('vocabList.notified')} </label>
            </div>
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

    initButtons("#btnSave,#btnDel");
  });
</script>
<!-- @jsSection end -->
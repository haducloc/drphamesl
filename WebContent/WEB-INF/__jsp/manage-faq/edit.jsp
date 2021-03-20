<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_faq_edit.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="edit" __faqId="${model.faqId}" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="questText" labelKey="faq.questText" required="true" />
            <t:textBox path="model.questText" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="ansText" labelKey="faq.ansText" required="true" />
            <t:textArea path="model.ansText" clazz="form-control" rows="4" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="dispPos" labelKey="faq.dispPos" required="true" />
            <t:textBox type="text" path="model.dispPos" clazz="form-control" />
          </div>

          <div class="form-group">
            <div class="form-check">
              <t:checkbox submitValue="1" path="model.active" checked="${model.active eq 1}" clazz="form-check-input mr-2" />
              <label class="form-check-label" for="active"> ${ctx.escCt('faq.active')} </label>
            </div>
          </div>

          <t:submitButton id="btnSave" clazz="btn btn-primary px-4" labelKey="label.save"></t:submitButton>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnSave");

  });
</script>
<!-- @jsSection end -->
<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_news_index.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="index" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="subject" labelKey="newsletterModel.subject" required="true" />
            <t:textBox path="model.subject" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="message" labelKey="newsletterModel.message" required="true" />
            <t:textArea path="model.message" clazz="form-control" rows="16" />
          </div>

          <t:submitButton id="btnSend" clazz="btn btn-primary px-4" labelKey="label.send"></t:submitButton>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnSend");
  });
</script>
<!-- @jsSection end -->
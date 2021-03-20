<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_resource_edit.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="edit" __resourceId="${model.resourceId}" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="resourceId" labelKey="resource.resourceId" required="true" />
            <t:textBox path="model.resourceId" clazz="form-control" readonly="true" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="resourceText" labelKey="resource.resourceText" />
            <t:textArea path="model.resourceText" clazz="form-control" rows="12" />
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
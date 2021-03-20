<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_service_edit.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="edit" __serviceId="${model.serviceId}" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="serviceCatId" labelKey="service.serviceCatId" required="true" />
            <t:select items="${serviceCats}" path="model.serviceCatId" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="titleText" labelKey="service.titleText" required="true" />
            <t:textBox path="model.titleText" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="descText" labelKey="service.descText" required="true" />
            <t:textArea path="model.descText" clazz="form-control" rows="8" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="tuitionText" labelKey="service.tuitionText" required="true" />
            <t:textArea path="model.tuitionText" clazz="form-control" rows="2" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="dispPos" labelKey="service.dispPos" required="true" />
            <t:textBox type="text" path="model.dispPos" clazz="form-control" />
          </div>

          <div class="form-group">
            <div class="form-check">
              <t:checkbox submitValue="1" path="model.active" checked="${model.active eq 1}" clazz="form-check-input mr-2" />
              <label class="form-check-label" for="active"> ${ctx.escCt('service.active')} </label>
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

    initDropdowns("#serviceCatId");
    initButtons("#btnSave");
  });
</script>
<!-- @jsSection end -->
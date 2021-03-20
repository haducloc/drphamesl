<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('account_profile.page_title')}
  page.keywords=${ctx.esc('account_profile.page_keywords')}
  page.desc=${ctx.esc('account_profile.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('account_profile.title_head')}</h1>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row justify-content-center">
    <div class="col-md-8">

      <div class="mb-4 p-4 shadow-sm">

        <t:form id="form1" action="profile" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="firstName" labelKey="account.firstName" required="true" />
            <t:textBox type="text" path="model.firstName" autocomplete="given-name" clazz="form-control" />
            <t:fieldError field="firstName" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="lastName" labelKey="account.lastName" required="true" />
            <t:textBox type="text" path="model.lastName" autocomplete="family-name" clazz="form-control" />
            <t:fieldError field="lastName" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="email" labelKey="account.email" required="true" />
            <t:textBox type="email" path="model.email" clazz="form-control" readonly="true" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="password" labelKey="account.password" />
            <t:actionLink action="changepwd" clazz="d-block py-1 font-sm1">${ctx.escCt('account_profile.change_password')}</t:actionLink>
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
<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('account_resetpwd.page_title')}
  page.keywords=${ctx.esc('account_resetpwd.page_keywords')}
  page.desc=${ctx.esc('account_resetpwd.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('account_resetpwd.title_head')}</h1>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 shadow-sm">

        <t:form id="form1" action="resetpwd" __series="${model.series}" __token="${model.token}" __email="${model.email}" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="newPassword" labelKey="resetPasswordModel.newPassword" />
            <t:textBox type="password" path="model.newPassword" autocomplete="new-password" clazz="form-control" />

            <small class="form-text text-muted">${ctx.escCt('account_signup.password_rules')}</small>
            <t:fieldError field="newPassword" />
          </div>

          <t:submitButton id="btnResetPwd" clazz="btn btn-primary px-4" disabled="${not empty requestScope['com.appslandia.plum.base.AuthFailureResult']}"
            labelKey="label.reset"></t:submitButton>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnResetPwd");
  });
</script>
<!-- @jsSection end -->
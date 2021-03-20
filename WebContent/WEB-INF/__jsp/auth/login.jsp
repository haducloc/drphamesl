<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('auth_login.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row justify-content-center">
    <div class="col-md-6">

      <div class="p-4 shadow">

        <t:form action="login" __returnUrl="${param.returnUrl}">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="userName" labelKey="loginModel.userName" />
            <t:textBox type="text" path="model.userName" enterBtn="btnLogin" autocomplete="username" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="password" labelKey="loginModel.password" />
            <t:textBox type="password" path="model.password" enterBtn="btnLogin" clazz="form-control" />
          </div>

          <div class="form-group">
            <div class="form-check">
              <t:checkbox path="model.rememberMe" submitValue="true" enterBtn="btnLogin" clazz="form-check-input" />
              <t:fieldLabel field="rememberMe" labelKey="loginModel.rememberMe" clazz="form-check-label field-label" />
            </div>
          </div>

          <div class="form-row">
            <div class="col-auto mr-auto">
              <t:submitButton id="btnLogin" clazz="btn btn-primary px-4" labelKey="label.login"></t:submitButton>
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

    initButtons("#btnLogin");

  });
</script>
<!-- @jsSection end -->
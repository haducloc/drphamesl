<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('account_login.page_title')}
  page.keywords=${ctx.esc('account_login.page_keywords')}
  page.desc=${ctx.esc('account_login.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('account_login.title_head')}</h1>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row justify-content-center">
    <div class="col-md-6">

      <div class="mb-4 p-4 shadow">

        <t:form action="login" __returnUrl="${param.returnUrl}">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="email" labelKey="emailLoginModel.email" required="true" />
            <t:textBox type="email" path="model.email" enterBtn="btnLogin" autocomplete="email" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="password" labelKey="emailLoginModel.password" required="true" />
            <t:textBox type="password" path="model.password" enterBtn="btnLogin" clazz="form-control" />
          </div>

          <div class="form-group">
            <div class="form-check">
              <t:checkbox path="model.rememberMe" submitValue="true" enterBtn="btnLogin" clazz="form-check-input" />
              <t:fieldLabel field="rememberMe" labelKey="emailLoginModel.rememberMe" clazz="form-check-label field-label" />
            </div>
          </div>

          <div class="form-row">
            <div class="col-auto mr-auto">
              <t:submitButton id="btnLogin" clazz="btn btn-primary px-4" labelKey="label.login"></t:submitButton>
            </div>

            <div class="col-auto">
              <div class="d-inline-flex font-sm1" role="group">
                <t:actionLink action="signup">${ctx.escCt('label.signup')}</t:actionLink>
                <t:verBar clazz="ver-bar mx-2" />
                <t:actionLink action="forgotpwd">${ctx.escCt('label.forgot_password')}</t:actionLink>
              </div>
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
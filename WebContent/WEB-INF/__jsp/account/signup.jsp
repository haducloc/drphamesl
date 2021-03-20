<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('account_signup.page_title')}
  page.keywords=${ctx.esc('account_signup.page_keywords')}
  page.desc=${ctx.esc('account_signup.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('account_signup.title_head')}</h1>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row justify-content-center">
    <div class="col-md-8">

      <div class="mb-4 p-4 shadow-sm">

        <t:form id="form1" action="signup" __returnUrl="${param.returnUrl}" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:fieldLabel field="firstName" labelKey="registrationModel.firstName" required="true" />
            <t:textBox type="text" path="model.firstName" autocomplete="given-name" clazz="form-control" />
            <t:fieldError field="firstName" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="lastName" labelKey="registrationModel.lastName" required="true" />
            <t:textBox type="text" path="model.lastName" autocomplete="family-name" clazz="form-control" />
            <t:fieldError field="lastName" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="email" labelKey="registrationModel.email" required="true" />
            <t:textBox type="email" path="model.email" autocomplete="email" clazz="form-control lower" />

            <small class="form-text text-muted">${ctx.escCt('account_signup.email_help_text')}</small>
            <t:fieldError field="email" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="password" labelKey="registrationModel.password" required="true" />
            <t:textBox type="password" path="model.password" autocomplete="new-password" clazz="form-control" />

            <small class="form-text text-muted">${ctx.escCt('account_signup.password_rules')}</small>
            <t:fieldError field="password" />
          </div>

          <t:submitButton id="btnSignUp" clazz="btn btn-primary px-4" labelKey="label.signup"></t:submitButton>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnSignUp");
  });
</script>
<!-- @jsSection end -->
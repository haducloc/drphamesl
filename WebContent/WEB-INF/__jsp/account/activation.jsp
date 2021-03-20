<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('account_activation.page_title')}
  page.keywords=${ctx.esc('account_activation.page_keywords')}
  page.desc=${ctx.esc('account_activation.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('account_activation.title_head')}</h1>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 shadow-sm">
        <p>${ctx.escCt('account_activation.account_activation_text')}</p>

        <t:form id="form1" action="activation" autocomplete="off">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <div class="form-group">
            <t:textBox type="email" path="model.email" autocomplete="email" enterBtn="btnSubmit" placeholder="${ctx.esc('hint.enter_your_email')}"
              clazz="form-control lower" />
            <t:fieldError field="email" />
          </div>

          <t:submitButton id="btnSubmit" clazz="btn btn-primary px-4" labelKey="label.submit"></t:submitButton>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnSubmit");

  });
</script>
<!-- @jsSection end -->
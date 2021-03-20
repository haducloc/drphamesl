<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('drpham_contact.page_title')}
  page.keywords=${ctx.esc('drpham_contact.page_keywords')}
  page.desc=${ctx.esc('drpham_contact.page_desc')}
  __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">${ctx.escCt('drpham_contact.title_head')}</h1>
    <p class="lead mb-0">${ctx.fmtEscCt('drpham_contact.title_text', '@{drPhamSpan}')}</p>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col-md-8">

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('drpham_contact.message_title_head')}</h2>

        <t:form id="form1" action="contact">

          <div class="form-row">
            <div class="form-group col-md-5">
              <t:fieldLabel field="yourName" labelKey="contactUsModel.yourName" required="true" />
              <t:textBox type="text" path="model.yourName" autocomplete="name" clazz="form-control" />
            </div>
            <div class="form-group col-md-7">
              <t:fieldLabel field="yourEmail" labelKey="contactUsModel.yourEmail" required="true" />
              <t:textBox type="email" path="model.yourEmail" autocomplete="email" clazz="form-control" />
            </div>
          </div>

          <div class="form-group">
            <t:fieldLabel field="message" labelKey="contactUsModel.message" required="true" />
            <t:textArea path="model.message" clazz="form-control" rows="8" maxlength="8000" />
          </div>
          <t:submitButton id="btnSendMsg" clazz="btn btn-primary px-4" labelKey="label.send_msg"></t:submitButton>
        </t:form>

      </div>
    </div>

    <div class="col-md-4">

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('drpham_contact.connect_zalo_head')}</h2>

        <p class="mb-0">${ctx.escCt('drpham_contact.connect_zalo_qr')}</p>
        <img alt="Dr. Pham" src="@(contextPathExpr)/static/images/dr-pham-qr.jpg" height="128" />
      </div>

      <div class="mb-4 p-4 shadow-sm">
        <h2 class="font-sl3 font-w5 mb-3">${ctx.escCt('drpham_contact.connect_email_head')}</h2>

        <div class="d-flex align-items-center">
          <img class="mr-3" alt="Dr. Pham" src="@(contextPathExpr)/static/images/email.png" height="32">
          <a href="mailto:DrPhamESL@gmail.com">DrPhamESL@gmail.com</a>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnSendMsg");

  });
</script>
<!-- @jsSection end -->
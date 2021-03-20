<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('drpham_about.page_title')}
  page.keywords=${ctx.esc('drpham_about.page_keywords')}
  page.desc=${ctx.esc('drpham_about.page_desc')}
  __layout=layout
 -->

<div class="container my-5">
  <div class="heading-box mb-0">

    <div class="row justify-content-center">
      <img class="mr-3" alt="Dr. Pham" src="@(contextPathExpr)/static/images/dr-pham.png" height="80" />
      <div>
        <h1 class="font-sl10 font-w5 text-nowrap">${ctx.escCt('drpham_about.dr_pham')}</h1>
        <p class="lead mb-0">${ctx.escCt('drpham_about.title_text')}</p>
      </div>
    </div>
  </div>
</div>

<div class="container mb-4">

  <%@include file="../includes/messages.jsp"%>

  <div class="row">
    <div class="col">
      <div class="mb-4">${ctx.res('drpham_about.about_text')}</div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {
  });
</script>
<!-- @jsSection end -->
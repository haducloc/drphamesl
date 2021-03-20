<!-- @variables
  page.title=${ctx.esc('error_page.page_title')}
    __layout=layout
 -->

<div class="container my-5 text-center">
  <div class="heading-box mb-0">

    <h1 class="font-sl10 font-w5">@{page.title}</h1>
  </div>
</div>

<div class="container mb-4">

  <ul class="mb-0 px-4 py-2 rounded messages-error">
    <li>${requestScope['com.appslandia.plum.base.Problem'].title}</li>
  </ul>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {
  });
</script>
<!-- @jsSection end -->
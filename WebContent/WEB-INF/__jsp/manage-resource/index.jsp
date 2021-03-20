<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_resource_index.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="table-responsive mb-4">
        <table class="table table-sm nowrap-head table-bordered table-hover">
          <thead>
            <tr>
              <th scope="col">${ctx.escCt('resource.resourceId')}</th>
              <th scope="col">${ctx.escCt('resource.resourceText')}</th>
            </tr>
          </thead>
          <tbody>
            <t:iterate items="${resources}" var="item">
              <tr>
                <td>
                  <t:actionLink action="edit" __resourceId="${item.pk}">
                     ${item.resourceId}
                  </t:actionLink>
                </td>
                <td>${fx:escCt(item.truncatedText)}&hellip;</td>
              </tr>
            </t:iterate>
            <t:c t="tr" render="${empty resources}">
              <td colspan="2">${ctx.escCt('record.no_records_found')}</td>
            </t:c>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {
  });
</script>
<!-- @jsSection end -->
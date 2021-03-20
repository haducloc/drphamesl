<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_service_index.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="table-responsive mb-4">
        <table class="table table-sm nowrap-head table-bordered table-hover">
          <thead>
            <tr>
              <th scope="col" colspan="3">
                <t:actionLink action="edit" class="btn btn-sm btn-secondary font-w4">&plus;</t:actionLink>
              </th>
              <th scope="col" class="text-right">
                <span class="font-w4">${ctx.escCt('record.record_count', services.size())}</span>
              </th>
            </tr>
            <tr>
              <th scope="col">${ctx.escCt('service.titleText')}</th>
              <th scope="col">${ctx.escCt('service.serviceCatId')}</th>
              <th scope="col">${ctx.escCt('service.dispPos')}</th>
              <th scope="col">${ctx.escCt('service.active')}</th>
            </tr>
          </thead>
          <tbody>
            <t:iterate items="${services}" var="item">
              <tr>
                <td>
                  <t:actionLink action="edit" __serviceId="${item.serviceId}">
                     ${fx:escCt(item.titleText)}
                  </t:actionLink>
                </td>
                <td>${fx:escCt(item.serviceCat.titleText)}</td>
                <td>${fx:escCt(item.dispPos)}</td>
                <td>
                  <t:checkMark render="${item.active eq 1}" />
                </td>
              </tr>
            </t:iterate>
            <t:c t="tr" render="${empty services}">
              <td colspan="4">${ctx.escCt('record.no_records_found')}</td>
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
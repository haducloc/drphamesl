<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_eslres_index.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="table-responsive mb-4">
        <table class="table table-sm nowrap-head table-bordered table-hover">
          <thead>
            <tr>
              <th scope="col" colspan="2">
                <t:actionLink action="edit" class="btn btn-sm btn-secondary font-w4">&plus;</t:actionLink>
              </th>
              <th scope="col" class="text-right">
                <span class="font-w4">${ctx.escCt('record.record_count', eslRess.size())}</span>
              </th>
            </tr>
            <tr>
              <th scope="col">${ctx.escCt('eslRes.titleText')}</th>
              <th scope="col">${ctx.escCt('eslRes.dispPos')}</th>
              <th scope="col">${ctx.escCt('eslRes.active')}</th>
            </tr>
          </thead>
          <tbody>
            <t:iterate items="${eslRess}" var="item">
              <tr>
                <td>
                  <t:actionLink action="edit" __eslResId="${item.eslResId}">
                     ${fx:escCt(item.titleText)}
                  </t:actionLink>
                </td>
                <td>${fx:escCt(item.dispPos)}</td>
                <td>
                  <t:checkMark render="${item.active eq 1}" />
                </td>
              </tr>
            </t:iterate>
            <t:c t="tr" render="${empty eslRess}">
              <td colspan="3">${ctx.escCt('record.no_records_found')}</td>
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
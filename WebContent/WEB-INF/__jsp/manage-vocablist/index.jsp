<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_vocablist_index.page_title')}
  __layout=layout_admin
  genUrl=<t:action action='list' controller='vocab' __pk='${item.idOrSpk}' __title_path='${item.title_path}' absUrl='true' />
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form action="index" method="GET" clazz="form-inline">
          <t:fieldLabel field="shareType" clazz="my-2 mr-3">${ctx.escCt('vocabList.shareType')}</t:fieldLabel>
          <t:select path="model.shareType" items="${model.shareTypes}" clazz="form-control mr-4" triggerSubmit="true" />

          <t:fieldLabel field="shareType" clazz="my-2 mr-3">${ctx.escCt('vocabList.tag')}</t:fieldLabel>
          <t:textBox path="model.tag" clazz="form-control mr-4" />

          <t:submitButton id="btnSearch" clazz="btn btn-primary my-2 px-4" labelKey="label.search" handleWait="false"></t:submitButton>
        </t:form>
      </div>
    </div>
  </div>

  <c:if test="${model.pagerModel.pageCount gt 1}">
    <div class="row">
      <div class="col">

        <t:pager model="${model.pagerModel}" tag="ul" clazz="pagination justify-content-center mb-4">
          <t:tpl type="page">
            <li class="page-item${selected ? ' active' : ''}">
              <t:actionLink clazz="page-link" action="index" __tag="${model.tag}" __pageIndex="${item.index}" __recordCount="${model.pagerModel.recordCount}">${label}</t:actionLink>
            </li>
          </t:tpl>
          <t:tpl type="dots">
            <li class="page-item">
              <a href="#" class="page-link">...</a>
            </li>
          </t:tpl>
        </t:pager>
      </div>
    </div>
  </c:if>

  <div class="row">
    <div class="col">

      <div class="table-responsive mb-4">
        <table class="table table-sm nowrap-head table-bordered table-hover mb-0">
          <thead>
            <tr>
              <th scope="col" colspan="5">
                <t:actionLink action="edit" class="btn btn-sm btn-secondary font-w4">&plus;</t:actionLink>
              </th>
              <th scope="col" class="text-right">
                <span class="font-w4">${ctx.escCt('record.record_count', model.vocabLists.size())}</span>
              </th>
            </tr>
            <tr>
              <th scope="col">${ctx.escCt('vocabList.titleText')}</th>
              <th scope="col">${ctx.escCt('vocabList.tag')}</th>
              <th scope="col">${ctx.escCt('vocabList.dispPos')}</th>
              <th scope="col">${ctx.escCt('vocabList.shareType')}</th>
              <th scope="col">${ctx.escCt('vocabList.notified')}</th>
              <th scope="col">${ctx.escCt('label.url')}</th>
            </tr>
          </thead>
          <tbody>
            <t:iterate items="${model.vocabLists}" var="item">
              <tr>
                <td>
                  <t:actionLink action="edit" __vocabListId="${item.vocabListId}">${fx:escCt(item.titleText)}</t:actionLink>
                </td>
                <td>
                  <t:actionLink action="index" controller="manage-vocab" __query="${item.tag}">${fx:escCt(item.tag)}</t:actionLink>
                </td>
                <td>${fx:escCt(item.dispPos)}</td>
                <td>
                  <t:constDesc constGroup="shareTypes" value="${item.shareType}" />
                </td>
                <td>
                  <t:checkMark render="${item.shareType eq 2 and item.notified eq 1}" />
                  <t:actionLink action="notify" __vocabListId="${item.vocabListId}" render="${item.shareType eq 2 and item.notified eq 0}">
                      ${ctx.escCt('label.send')}
                  </t:actionLink>
                </td>
                <td>
                  <div class="d-inline-flex">
                    <input id="txtUrl${item.vocabListId}" type="text" value="@(genUrl)" class="form-control-sm border" readonly="readonly" />
                    <button data-txt="txtUrl${item.vocabListId}" type="button" class="btn btn-link btn-copy-url font-sm1 py-0">${ctx.escCt('label.copy')}</button>
                  </div>
                </td>
              </tr>
            </t:iterate>
            <t:c t="tr" render="${empty model.vocabLists}">
              <td colspan="6">${ctx.escCt('record.no_records_found')}</td>
            </t:c>
          </tbody>

        </table>
      </div>
    </div>
  </div>

  <c:if test="${model.pagerModel.pageCount gt 1}">
    <div class="row">
      <div class="col">

        <t:pager model="${model.pagerModel}" tag="ul" clazz="pagination justify-content-center mb-4">
          <t:tpl type="page">
            <li class="page-item${selected ? ' active' : ''}">
              <t:actionLink clazz="page-link" action="index" __tag="${model.tag}" __pageIndex="${item.index}" __recordCount="${model.pagerModel.recordCount}">${label}</t:actionLink>
            </li>
          </t:tpl>
          <t:tpl type="dots">
            <li class="page-item">
              <a href="#" class="page-link">...</a>
            </li>
          </t:tpl>
        </t:pager>
      </div>
    </div>
  </c:if>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    $("button.btn-copy-url").click(function() {
      var txtId = $(this).attr("data-txt")
      $("#" + txtId).select();

      try {
        document.execCommand("copy");
      } catch (err) {
      }
    });

  });
</script>
<!-- @jsSection end -->
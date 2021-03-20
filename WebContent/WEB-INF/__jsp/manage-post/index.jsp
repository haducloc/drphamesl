<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_post_index.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form action="index" method="GET">
          <div class="input-group">
            <div class="input-group-prepend">
              <div class="input-group-text">${ctx.escCt('label.tag')}</div>
            </div>
            <t:textBox path="model.tag" clazz="form-control" />
            <div class="input-group-append">
              <t:submitButton id="btnSearch" clazz="btn btn-primary px-4" labelKey="label.search" handleWait="false"></t:submitButton>
            </div>
          </div>
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
                <span class="font-w4">${ctx.escCt('record.record_count', model.blogPosts.size())}</span>
              </th>
            </tr>
            <tr>
              <th scope="col">${ctx.escCt('blogPost.titleText')}</th>
              <th scope="col">${ctx.escCt('blogPost.tags')}</th>
              <th scope="col">${ctx.escCt('blogPost.timeCreated')}</th>
              <th scope="col">${ctx.escCt('blogPost.active')}</th>
              <th scope="col">${ctx.escCt('label.notify')}</th>
              <th scope="col">${ctx.escCt('label.preview')}</th>
            </tr>
          </thead>
          <tbody>
            <t:iterate items="${model.blogPosts}" var="item">
              <tr>
                <td>
                  <t:actionLink action="edit" __blogPostId="${item.blogPostId}">
                     ${fx:escCt(item.titleText)}
                  </t:actionLink>
                </td>
                <td>
                  <t:iterate items="${item.tagList}" var="tag">
                    <t:actionLink action="index" __tag="${tag}" clazz="tag-link mr-3">
                       ${fx:escCt(tag)}
                  </t:actionLink>
                  </t:iterate>
                </td>
                <td>${fx:escCt(item.timeCreated)}</td>
                <td>
                  <t:checkMark render="${item.active eq 1}" />
                </td>
                <td>
                  <t:checkMark render="${item.notified eq 1}" />
                  <t:actionLink action="notify" __blogPostId="${item.blogPostId}" render="${item.notified eq 0}">
                      ${ctx.escCt('label.send')}
                  </t:actionLink>
                </td>
                <td>
                  <t:actionLink action="preview" __blogPostId="${item.blogPostId}" __title_path="${item.title_path}">${ctx.escCt('label.view')}</t:actionLink>
                </td>
              </tr>
            </t:iterate>
            <t:c t="tr" render="${empty model.blogPosts}">
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
  });
</script>
<!-- @jsSection end -->
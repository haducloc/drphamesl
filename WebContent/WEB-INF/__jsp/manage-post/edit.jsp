<%@include file="../includes/libs.jsp"%>

<!-- @variables
  page.title=${ctx.esc('manage_post_edit.page_title')}
  __layout=layout_admin
 -->

<div class="container mb-4">
  <div class="row">
    <div class="col">

      <div class="mb-4 p-4 border">

        <t:form id="form1" action="edit" __blogPostId="${model.blogPostId}" autocomplete="off" actionType="true">
          <t:formErrors clazz="px-4 py-2" modelLevelOnly="true" />

          <t:hidden path="model.timeCreated" />
          <t:hidden path="model.notified" />

          <div class="form-group">
            <t:fieldLabel field="titleText" labelKey="blogPost.titleText" required="true" />
            <t:textBox path="model.titleText" clazz="form-control" />
            <small class="form-text text-muted">${ctx.escCt('page_meta.title_rules')}</small>
          </div>

          <div class="form-group">
            <t:fieldLabel field="contentText" labelKey="blogPost.contentText" required="true" />
            <t:textArea path="model.contentText" clazz="form-control" rows="8" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="tags" labelKey="blogPost.tags" required="true" />
            <t:textBox path="model.tags" clazz="form-control" fmt="DbTags" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="fbUrl" labelKey="blogPost.fbUrl" />
            <t:textBox path="model.fbUrl" clazz="form-control" />
          </div>

          <div class="form-group">
            <t:fieldLabel field="keywords" labelKey="blogPost.keywords" required="true" />
            <t:textBox path="model.keywords" clazz="form-control" maxlength="255" />
            <small class="form-text text-muted">${ctx.escCt('page_meta.keywords_rules')}</small>
          </div>

          <div class="form-group">
            <t:fieldLabel field="descText" labelKey="blogPost.descText" required="true" />
            <t:textArea path="model.descText" clazz="form-control" rows="2" maxlength="160" />
            <small class="form-text text-muted">${ctx.escCt('page_meta.desc_rules')}</small>
          </div>

          <div class="form-group">
            <div class="form-check">
              <t:checkbox submitValue="1" path="model.active" checked="${model.active eq 1}" clazz="form-check-input mr-2" />
              <label class="form-check-label" for="active"> ${ctx.escCt('blogPost.active')} </label>
            </div>
          </div>

          <div class="form-row">
            <div class="col-auto mr-auto">
              <t:submitButton id="btnSave" clazz="btn btn-primary px-4" labelKey="label.save"></t:submitButton>
            </div>
            <div class="col-auto">
              <t:submitButton id="btnDel" actionType="remove" clazz="btn btn-danger px-4" labelKey="label.remove" render="${not empty model.pk}"></t:submitButton>
            </div>
          </div>
        </t:form>
      </div>
    </div>
  </div>
</div>

<!-- @jsSection begin -->
<script type="text/javascript">
  $(document).ready(function() {

    initButtons("#btnSend, #btnDel");
  });
</script>
<!-- @jsSection end -->
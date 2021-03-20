<%@ taglib prefix="t" uri="http://www.appslandia.com/jstl/tags"%>
<t:c t="div" clazz="row" render="${not empty messages}">
  <div class="col">
    <div class="messages-wrapper mb-5">
      <t:messages clazz="mb-0 px-4 py-2 rounded" />
    </div>
  </div>
</t:c>
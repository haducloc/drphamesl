<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="t" uri="http://www.appslandia.com/jstl/tags"%>
<%@ taglib prefix="fx" uri="http://www.appslandia.com/jstl/functions"%>

<div class="container-fluid">
  <div class="row">
    <div class="col">
      <p class="my-2">
        <t:iterate items="${model.mytags}" var="tag">
          <t:actionLink action="index" __query="${tag}" clazz="tag-link mr-3 mb-2">
              ${fx:escCt(tag)}
            </t:actionLink>
        </t:iterate>
      </p>
    </div>
  </div>
</div>
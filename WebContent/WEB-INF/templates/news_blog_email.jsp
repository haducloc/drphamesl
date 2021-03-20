<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="t" uri="http://www.appslandia.com/jstl/tags"%>
<%@ taglib prefix="f2" uri="http://www.appslandia.com/jstl/functions"%>
<%@ taglib prefix="t2" uri="http://www.drphamesl.com/jstl/tags"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<title>${f2:escCt(model.subject)}</title>
</head>
<body>
  <p>Chào bạn,</p>
  <p>Dr. Pham có bài viết Blog mới. Mời bạn nhấp vào liên kết dưới đây để xem.</p>

  <a href="${model.blogPostUrl}" style="text-decoration: underline;">${model.blogPostUrl}</a>

  <hr />
  <p>
    Thank you, <br /> DrPhamESL.com
  </p>
  <p>
    <small>Nếu bạn không còn muốn nhận được bản tin Blog từ Dr. Pham, vui lòng nhấp vào <a href="${model.unsubscribeUrl}">hủy đăng ký</a>.
    </small>
  </p>
  <span style="display: none !important;">${f2:now()}</span>
</body>
</html>
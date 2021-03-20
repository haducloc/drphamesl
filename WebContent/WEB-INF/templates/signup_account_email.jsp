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

  <p>Cảm ơn bạn đã tạo tài khoản trên DrPhamESL. Vui lòng nhấp vào liên kết dưới đây để kích hoạt tài khoản.</p>

  <a href="${model.activateUrl}" style="text-decoration: underline;">${model.activateUrl}</a>

  <hr />
  <p>
    Thank you, <br /> DrPhamESL.com
  </p>
  <span style="display: none !important;">${f2:now()}</span>
</body>
</html>
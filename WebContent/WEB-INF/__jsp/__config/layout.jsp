<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="t" uri="http://www.appslandia.com/jstl/tags"%>
<%@ taglib prefix="fx" uri="http://www.appslandia.com/jstl/functions"%>
<%@ taglib prefix="t2" uri="http://www.drphamesl.com/jstl/tags"%>
<%@ taglib prefix="f2" uri="http://www.drphamesl.com/jstl/functions"%>
<!doctype html>
<html lang="${ctx.languageId}" class="h-100">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="description" content="@(page.desc)">
<meta name="keywords" content="@(page.keywords)" />
<meta name="author" content="@(page.author)" />
<meta name="robots" content='@{page.robots}' />

<meta property="og:url" content="${ctx.requestUrl}" />
<meta property="og:title" content="@{page.title}" />
<meta property="og:description" content="@(page.desc)" />
<meta property="og:type" content="@(page.type)" />

<link rel="shortcut icon" href="@(contextPathExpr)/favicon.ico">
<link rel="apple-touch-icon" sizes="180x180" href="@(contextPathExpr)/apple-touch-icon.png">

<link rel="icon" type="image/png" sizes="512x512" href="@(contextPathExpr)/android-icon-512x512.png">
<link rel="icon" type="image/png" sizes="192x192" href="@(contextPathExpr)/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32" href="@(contextPathExpr)/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="@(contextPathExpr)/favicon-16x16.png">

<link rel="stylesheet" href="@(contextPathExpr)/static/css/bootstrap.min.css">
<link rel="stylesheet" href="@(contextPathExpr)/static/css/app.min.css">

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-173528772-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag() {
    dataLayer.push(arguments);
  }
  gtag('js', new Date());

  gtag('config', 'UA-173528772-1');
</script>

<title>@{page.title}</title>
</head>
<!-- @variables: shared.properties -->

<body class="d-flex flex-column h-100">
  <header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div class="container">
        <t:actionLink clazz="navbar-brand" action="index" controller="main">${ctx.escCt('app.name')}</t:actionLink>

        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false"
          aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCollapse">
          <ul class="navbar-nav ml-auto">
            <t2:navItem clazz="nav-item mr-2" action="index" controller="main" linkKey="nav.services" />
            <t2:navItem clazz="nav-item mr-2" action="index" controller="faq" linkKey="nav.faq" />
            <t2:navItem clazz="nav-item mr-2" action="contact" controller="dr-pham" linkKey="nav.contact_us" />

            <t2:navItem clazz="nav-item mr-2" action="index" controller="esl-resources" linkKey="nav.esl_resources" />
            <t2:navItem clazz="nav-item mr-2" action="index" controller="vocab" linkKey="nav.vocab" />

            <t2:navItem clazz="nav-item mr-2" action="about" controller="dr-pham" linkKey="nav.about_drpham" />
            <t2:navItem clazz="nav-item mr-2" action="index" controller="blog" linkKey="nav.blog" />
          </ul>

          <ul class="navbar-nav ml-auto">
            <t2:navItem clazz="nav-item" action="login" controller="account" linkKey="label.login" render="${empty @(principalPath)}" />

            <t:c t="li" clazz="nav-item dropdown" render="${not empty @(principalPath)}">
              <a class="nav-link dropdown-toggle" href="#" id="authDrp" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <t:displayName />
              </a>
              <div class="dropdown-menu dropdown-menu-right" aria-labelledby="authDrp">
                <t:actionLink clazz="dropdown-item" action="logout" controller="account">${ctx.escCt('label.logout')}</t:actionLink>
                <div class="dropdown-divider"></div>
                <t:actionLink clazz="dropdown-item" action="profile" controller="account">${ctx.escCt('label.profile')}</t:actionLink>
              </div>
            </t:c>
          </ul>
        </div>
      </div>
    </nav>
  </header>

  <main class="flex-shrink-0" role="main">
    <!-- @doBody -->
  </main>

  <footer class="mt-auto bg-light py-1">
    <div class="container">
      <div class="row justify-content-center">
        <small class="text-muted">Copyright &copy; ${fx:cprYears(2020)} DrPhamESL.com</small>
      </div>
    </div>
  </footer>

  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="@(contextPathExpr)/static/js/bootstrap.bundle.min.js"></script>

  <script type="text/javascript">
      var LABEL_WAIT = "${ctx.esc('label.please_wait')}";
      var SIGNUP_URL = '<t:action action="index" controller="signup" />';
    </script>
  <script src="@(contextPathExpr)/static/js/app.min.js"></script>

  <!-- @jsSection? -->
</body>
</html>

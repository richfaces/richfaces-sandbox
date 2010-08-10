<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Richfaces examples and tests</title>
</head>
<body>
<jsp:useBean id="pages" scope="application" class="org.ajax4jsf.PagesBean">
  <jsp:setProperty name="pages" property="servletContext" value="${pageContext.servletContext}"/>
  <jsp:setProperty name="pages" property="path" value="/pages"/>
</jsp:useBean>
<h2>JSF pages</h2>
<ul>
<c:forEach var="page" items="${pages.jspPages}">
  <li><a href="${pageContext.request.contextPath}/faces${page.path}">${page.title}</a></li>
</c:forEach>
</ul>
<h2>Facelets pages</h2>
<ul>
<c:forEach var="page" items="${pages.xhtmlPages}">
  <li><a href="${pageContext.request.contextPath}/faces${page.path}">${page.title}</a></li>
</c:forEach>
</ul>
</body>
</html>
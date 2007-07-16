<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><tiles:getAsString name="title"/></title>
<s:head theme="css_xhtml"/>
<link rel="stylesheet" type="text/css" href="<s:url value="/common/styles.css"/>" />
</head>
<body>

	<div id="header">
		<br/>
		<s:url id="homeUrl" value="/index.jsp" />
		<s:a href="%{homeUrl}">Easygen</s:a>
	</div>

	<table width="90%" border="0">
	<tr>
		<td width="30%" valign="top">
		    <tiles:insertAttribute name="menu"/>
		</td>
		<td valign="top">

			<p class="spacer">&nbsp;</p>
			
			<s:if test="hasActionMessages()">
			<div id="messages">
			<s:actionmessage />
			<p>&nbsp;</p>
			</div>
			</s:if>
			
			<s:if test="hasActionErrors()">
			<div id="messages">
			<s:actionerror />
			<p>&nbsp;</p>
			</div>
			</s:if>
			
			<s:if test="hasFieldErrors()">
			<div id="messages">
			  <p class="errorMessage">Validation Errors :</p>
			  <s:fielderror />
			<p>&nbsp;</p>
			</div>
			</s:if>

	        <tiles:insertAttribute name="body"/>

		</td>
	</tr>
	</table>

	<div id="footer">
	<p>Generated using EasyGen</p>
	</div>

</body>
</html>

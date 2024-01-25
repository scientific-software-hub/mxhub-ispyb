<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<FORM action="/bcr/bcr/user/localizationList.do" method="post" name="localizationForm">
<INPUT name="reqCode" value="display" type="hidden">

<INPUT name="location" value="<layout:write name="localizationForm" property="location"/>" type="hidden">
<INPUT name="dewarList" value="<layout:write name="localizationForm" property="dewarList"/>" type="hidden">
<INPUT name="nbDewars" value="<layout:write name="localizationForm" property="nbDewars"/>" type="hidden">

<TABLE class="tableForm1" id="table0" cellSpacing=8px cellPadding=0>
	<TR>
		<TD align="center" nowrap><FONT class="normText1_RED">ERREUR</FONT></TD>
	</TR>
	<TR>
		<TD align="center" nowrap>
			<FONT class="normText1">
				<html:messages id="error">
				<bean:write name="error"/><BR>
				</html:messages>
			</FONT>
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan=2>
		<input class="buttonType1" type="submit" value="Ok" onclick="this.form.elements['reqCode'].value='display';loading('Traitement en cours...');">
		<input class="buttonType1" type="button" value="Appli" onClick="parent.location='/bcr/bcr/user/chooseappli.do';loading('Traitement en cours...');">
		</TD>
	</TR>
</TABLE>

<!-- EMBED SRC="<%=request.getContextPath()%>/images/nok.wav" hidden="true" autostart="true" --> 

</FORM>


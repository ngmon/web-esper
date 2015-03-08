<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>
<h3>Enter schema information</h3>
<div align="center">
    <form:form action="/addSchema" method="POST" modelAttribute="SchemaHelper">
        <table border="0">
            <tr>
                <td><form:label path="eventType">Event type:</form:label></td>
                <td><form:input path="eventType" /></td>
            </tr>
            <tr>
                <td><form:label path="properties">Properties (in format: v1 String, v2 integer, ...):</form:label></td>
                <td><form:input path="properties" /></td>
            </tr>
            <tr>
                <td colspan="2" align="center"><input type="submit" value="Add" /></td>
                <td colspan="2" align="center"><input type="reset" value="Reset" /></td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>
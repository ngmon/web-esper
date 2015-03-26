<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>
    <h3>Enter event type name</h3>
    <div align="center">
        <form:form action="/removeEventType" method="POST" modelAttribute="EventTypeModel">
            <table border="0">
                <tr>
                    <td><form:label path="eventType">Event type:</form:label></td>
                    <td><form:input path="eventType" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><input type="submit" value="Remove" /></td>
                    <td colspan="2" align="center"><input type="reset" value="Reset" /></td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>
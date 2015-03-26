<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <body>
        <div align="center">
            <h3>Enter event type information</h3>
            <form:form action="/addEventType" method="POST" modelAttribute="EventTypeModel">
                <table style="border:2px solid black;">
                    <tr>
                        <th><form:label path="eventType">Event type name:</form:label></th>
                        <th><form:input path="eventType" /></th>
                    </tr>
                    <tr>
                        <th><form:label path="properties">Properties (in format: v1 String, v2 Integer, ...):</form:label></th>
                        <th><form:input path="properties" /></th>
                    </tr>
                    <tr>
                        <th align="center"><input type="submit" value="Add" /></th>
                        <th align="center"><input type="reset" value="Reset" /></th>
                    </tr>
                </table>
            </form:form>
            <form action="/">
                <input type="submit" value="Return home">
            </form>
        </div>
    </body>
</html>
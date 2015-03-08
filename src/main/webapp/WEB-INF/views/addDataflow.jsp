<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
    </head>
    <body>
        <h3>Enter dataflow information</h3>
        <div align="center">
            <form:form action="/addDataflow" method="POST" modelAttribute="EPLHelper">
                <table border="0">
                    <tr>
                    </tr>
                    <tr>
                        <td><form:label path="dataflowName">Dataflow name:</form:label></td>
                        <td><form:input path="dataflowName" /></td>
                    </tr>
                    <tr>
                        <td><form:label path="eventType">Event type:</form:label></td>
                        <td><form:input path="eventType" /></td>
                    </tr>
                    <tr>
                        <td><form:label path="query">Statement (empty for AMQP source):</form:label></td>
                        <td><form:input path="query" /></td>
                    </tr>
                    <tr>
                        <td><form:label path="queueName">Queue name:</form:label></td>
                        <td><form:input path="queueName" /></td>
                    </tr>
                    <tr>
                        <td><form:label path="exchangeName">Exchange name:</form:label></td>
                        <td><form:input path="exchangeName" /></td>
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
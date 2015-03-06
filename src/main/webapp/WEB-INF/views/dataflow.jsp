<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Dataflow Creation</title>
</head>
<body>

<h2>Dataflow Information</h2>
<form:form method="POST" action="/adddataflow">
    <table>
        <tr>
            <td><form:label path="dataflowName">Dataflow name</form:label></td>
            <td><form:input path="dataflowName" /></td>
        </tr>
        <tr>
            <td><form:label path="eventType">Event type</form:label></td>
            <td><form:input path="eventType" /></td>
        </tr>
        <tr>
            <td><form:label path="queueName">Queue name</form:label></td>
            <td><form:input path="queueName" /></td>
        </tr>
        <tr>
            <td><form:label path="exchangeName">Exchange name</form:label></td>
            <td><form:input path="exchangeName" /></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Add dataflow"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Esper Continuous Query Engine</title>
</head>
<body>
<h2>Esper Continuous Query Engine</h2>
<p>Select your action:</p>
<form:form method="GET" action="/addEventType">
    <table>
        <tr>
            <td>
                <input type="submit" value="Add event type"/>
            </td>
        </tr>
    </table>
</form:form>
<form:form method="GET" action="/addDataflow">
    <table>
        <tr>
            <td>
                <input type="submit" value="Add dataflow"/>
            </td>
        </tr>
    </table>
</form:form>
<form:form method="GET" action="/removeEventType">
    <table>
        <tr>
            <td>
                <input type="submit" value="Remove event type"/>
            </td>
        </tr>
    </table>
</form:form>
<form:form method="GET" action="/removeDataflow">
    <table>
        <tr>
            <td>
                <input type="submit" value="Remove dataflow"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
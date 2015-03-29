<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Event types management</title>
</head>
<body>
<div align="center">
    <h2>Select your action:</h2>
    <form:form method="GET" action="/addEventType">
        <table>
            <tr>
                <th>
                    <input type="submit" align="center" value="Add event type"/>
                </th>
            </tr>
        </table>
    </form:form>
    <form:form method="GET" action="/removeEventType">
        <table>
            <tr>
                <th>
                    <input type="submit" align="center" value="Remove event type"/>
                </th>
            </tr>
        </table>
    </form:form>
    <form:form method="GET" action="/showEventTypes">
        <table>
            <tr>
                <th>
                    <input type="submit" align="center" value="Show all event types"/>
                </th>
            </tr>
        </table>
    </form:form>
    <form action="/">
        <input type="submit" value="Return home">
    </form>
</div>
</body>
</html>
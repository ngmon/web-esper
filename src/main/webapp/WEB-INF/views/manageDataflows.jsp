<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Dataflows management</title>
</head>
<body>
<div align="center">
    <h2>Select your action:</h2>
    <form:form method="GET" action="/addDataflow">
        <table>
            <tr>
                <th>
                    <input type="submit" align="center" value="Add dataflow"/>
                </th>
            </tr>
        </table>
    </form:form>
    <form:form method="GET" action="/removeDataflow">
        <table>
            <tr>
                <th>
                    <input type="submit" align="center" value="Remove dataflow"/>
                </th>
            </tr>
        </table>
    </form:form>
    <form:form method="GET" action="/showDataflows">
        <table>
            <tr>
                <th>
                    <input type="submit" align="center" value="Show dataflows"/>
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
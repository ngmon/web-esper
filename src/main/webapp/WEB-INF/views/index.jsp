<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Esper Continuous Query Engine</title>
    </head>
    <body>
        <div align="center">
            <h2>Esper Continuous Query Engine</h2>
            <p>Select your action:</p>
            <form:form method="GET" action="/manageEventTypes">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Manage event types"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/manageDataflows">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Manage dataflows"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/manageEPLStatements">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Manage EPL statements"/>
                        </th>
                    </tr>
                </table>
            </form:form>
        </div>
    </body>
</html>
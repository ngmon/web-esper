<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Esper Continuous Query Engine</title>
    </head>
    <body>
        <div align="center">
            <h2>Esper Continuous Query Engine</h2>
            <p>Select your action:</p>
            <form:form method="GET" action="/addEventType">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Add event type"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/addDataflow">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Add dataflow"/>
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
            <form:form method="GET" action="/removeDataflow">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Remove dataflow"/>
                        </th>
                    </tr>
                </table>
            </form:form>
        </div>
    </body>
</html>
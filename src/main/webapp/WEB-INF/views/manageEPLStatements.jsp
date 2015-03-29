<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>EPL statements management</title>
</head>
    <body>
        <div align="center">
            <h2>Select your action:</h2>
            <form:form method="GET" action="/addOneStreamEPL">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Add one-stream EPL statement"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/addTwoStreamEPL">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Add two-stream EPL statement"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/addThreeStreamEPL">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Add three-stream EPL statement"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/removeEPL">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Remove EPL statement"/>
                        </th>
                    </tr>
                </table>
            </form:form>
            <form:form method="GET" action="/showEPLStatements">
                <table>
                    <tr>
                        <th>
                            <input type="submit" align="center" value="Show all EPL statements"/>
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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
    </head>
    <body>
        <div align="center">
            <h3>Enter dataflow information</h3>
            <form:form action="/addDataflow" method="POST" modelAttribute="DataflowModel">
                <table style="border:2px solid black;">
                    <tr>
                        <th><form:label path="dataflowName">Dataflow name:</form:label></th>
                        <th><form:input path="dataflowName" /></th>
                    </tr>
                    <tr>
                        <th><form:label path="firstEventType">Event type:</form:label></th>
                        <th><form:input path="firstEventType" /></th>
                    </tr>
                    <tr>
                        <th><form:label path="queueName">Queue name:</form:label></th>
                        <th><form:input path="queueName" /></th>
                    </tr>
                    <tr>
                        <th><form:label path="exchangeName">Exchange name:</form:label></th>
                        <th><form:input path="exchangeName" /></th>
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
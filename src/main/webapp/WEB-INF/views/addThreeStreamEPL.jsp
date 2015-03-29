<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>
<div align="center">
    <h3>Enter three-stream EPL statement information</h3>
    <form:form action="/addThreeStreamEPL" method="POST" modelAttribute="DataflowModel">
        <table style="border:2px solid black;">
            <tr>
                <th><form:label path="dataflowName">Statement name:</form:label></th>
                <th><form:input path="dataflowName" /></th>
            </tr>
            <tr>
                <th><form:label path="firstEventType">First event type:</form:label></th>
                <th><form:input path="firstEventType" /></th>
            </tr>
            <tr>
                <th><form:label path="secondEventType">Second event type:</form:label></th>
                <th><form:input path="secondEventType" /></th>
            </tr>
            <tr>
                <th><form:label path="thirdEventType">Third event type:</form:label></th>
                <th><form:input path="thirdEventType" /></th>
            </tr>
            <tr>
                <th><form:label path="outputEventType">Output event type:</form:label></th>
                <th><form:input path="outputEventType" /></th>
            </tr>
            <tr>
                <th><form:label path="query">Statement:</form:label></th>
                <th><form:input path="query" /></th>
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
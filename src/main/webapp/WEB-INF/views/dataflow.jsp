<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Dataflow Creation</title>
</head>
<body>

<h2>Dataflow Information</h2>
<h1>Form</h1>
<form action="#" th:action="@{/adddataflow}" method="post">
    <p>Dataflow name: <input type="text" th:field="*{dataflowName}" /></p>
    <p>Event type: <input type="text" th:field="*{eventType}" /></p>
    <p>Queue name: <input type="text" th:field="*{queueName}" /></p>
    <p>Exchange name: <input type="text" th:field="*{exchangeName}" /></p>
    <p><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></p>
</form>
</body>
</html>
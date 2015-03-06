<%--<html>
<head>
    <title>Esper Continuous Query Engine</title>
    <style type="text/css">
    </style>
</head>
    <body>
        <br>
        <div align='center'>
        <h2>
            Possible commands are:<br> <br>
        </h2>
        <h3>
            <a href="addschema.html">Add event type and its schema, must be called BEFORE creating dataflows upon that event type</a>
        </h3>
        <h3>
        <a href="dafaflow.html">Add dataflow operating as AMQPSource, sending event to EventBusSink</a>
        </h3>
        <h3>
            <a href="addoutputdafaflow.html">Add output dataflow containing SELECT statement, with output of AMQP Sink</a>
        </h3>
        <h3>
            <a href="removedataflow.html">Stop and remove dataflow with given name</a>
        </h3>
        <h3>
            <a href="alldataflows.html">List all dataflows currently present in Esper engine</a>
        </h3>
    </div>
</body>
</html>
--%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Esper Continuous Query Engine</title>
</head>
<body>
<h2>Esper Continuous Query Engine</h2>
<p>Select your action:</p>
<form:form method="GET" action="/addschema">
    <table>
        <tr>
            <td>
                <input type="submit" value="Add schema"/>
            </td>
        </tr>
    </table>
</form:form>
<form:form method="GET" action="/adddataflow">
    <table>
        <tr>
            <td>
                <input type="submit" value="Add dataflow"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
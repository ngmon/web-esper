<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>
    <h3>Enter dataflow name</h3>
    <div align="center">
        <form:form action="/removeDataflow" method="POST" modelAttribute="DataflowModel">
            <table border="0">
                <tr>
                    <td><form:label path="dataflowName">Dataflow name:</form:label></td>
                    <td><form:input path="dataflowName" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><input type="submit" value="Remove" /></td>
                    <td colspan="2" align="center"><input type="reset" value="Reset" /></td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>
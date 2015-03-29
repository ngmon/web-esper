<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <body>
        <div align="center">
            <h3>All dataflows</h3>
            <div align="left">
                <c:if test="${not empty lists}">

                    <ul>
                        <c:forEach var="listValue" items="${lists}">
                            <p>
                            <li>${listValue}</li>
                            </p>
                        </c:forEach>
                    </ul>

                </c:if>
            </div>
            <form action="/">
                <input type="submit" value="Return home">
            </form>
        </div>
    </body>
</html>
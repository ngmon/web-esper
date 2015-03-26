<body>
    <div align="center">
        <h3>Event type removed:</h3>
        <table>
            <tr>
                <th>Event type name :</th>
            </tr>
            <tr>
                <th>${eventType}</th>
            </tr>
            <tr>
                <th>Result :</th>
            </tr>
            <tr>
                <th>${properties}</th>
            </tr>
        </table>
        <form action="/removeEventType">
            <input type="submit" value="Remove another event type">
        </form>
        <form action="/">
            <input type="submit" value="Return home">
        </form>
    </div>
</body>
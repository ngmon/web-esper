<body>
    <div align="center">
        <h3>Event type created:</h3>
        <table style="border:2px solid black;">
            <tr>
                <th>Event type :</th>
            </tr>
            <tr>
                <th>${eventType}</th>
            </tr>
            <tr>
                <th>Properties :</th>
            </tr>
            <tr>
                <th>${properties}</th>
            </tr>
        </table>
        <form action="/addEventType">
            <input type="submit" value="Create new event type">
        </form>
        <form action="/">
            <input type="submit" value="Return home">
        </form>
    </div>
</body>
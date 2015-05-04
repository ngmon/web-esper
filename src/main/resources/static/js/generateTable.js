function paintTable(){
    var selected = $("#exchangeSelect").find("option:selected").text();
    var URL = "http://localhost:8080/getSchema?name=" + selected;
    $.getJSON(URL, function(data) {
        successCallBack(data);
    });

    function successCallBack(data) {
        $("#dyntable").find("tbody").empty();
        $.each(data, function (k, v) {
            var newRowContent = "<tr><td><input type=\"checkbox\" checked=\"true\"></td>" +
                "<td>" + k + "</td>" +
                "<td>" + v + "</td></tr>";
            $("#dyntable").find("tbody").append(newRowContent);
        });
    }
}
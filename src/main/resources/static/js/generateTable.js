function paintTable(){
    var selected = $("#exchangeSelect").find("option:selected").text();
    var URL = "http://localhost:8080/getSchema?name=" + selected;
    $.getJSON(URL, function(data) {
        successCallBack(data);
    });

    function successCallBack(data) {
        $("#dyntable").find("tbody").empty();

        if(data == null) {
            return;
        }
        var i = 0;
        $.each(data, function (k, v) {
            var newRowContent = '<tr class="oneProperty">' +
                '<td><input type="checkbox" class="selectedProp" onclick="sortIds()" checked></td>';

            var id = "mapProperties" + i + ".key";
            var arrayId = "mapProperties[" + i + "].key";
            newRowContent += "<td><input readonly type=\"text\" class=\"propertyName\" id=\"" + id + "\" name=\"" + arrayId + "\" value=\"" + k + "\"></td>";

            id = "mapProperties" + i + ".value";
            arrayId = "mapProperties[" + i + "].value";
            newRowContent += ("<td><input readonly type=\"text\" class=\"propertyType\" id=\"" + id + "\" name=\"" + arrayId + "\" value=\"" + v + "\"></td>");

            $("#dyntable").find("tbody").append(newRowContent);
            i++;
        });
    }
}

function sortIds(){
    var index = 0;
    $('#dyntable').find('tbody').find('tr').each(function() {
        var checkBoxChecked = $(this).find("input[class=selectedProp]")[0].checked;
        var propName = $(this).find("input[class=propertyName]")[0];
        var propType = $(this).find("input[class=propertyType]")[0];

        var id;
        var name;

        if(checkBoxChecked) {
            id = "mapProperties" + index + ".key";
            name = "mapProperties[" + index + "].key";

            propName.id = id;
            propName.name = name;

            id = "mapProperties" + index + ".value";
            name = "mapProperties[" + index + "].value";

            propType.id = id;
            propType.name = name;

            index++;
        } else {
            id = "meh";
            name = "nah";

            propName.id = id;
            propName.name = name;

            propType.id = id;
            propType.name = name;
        }
    });
}
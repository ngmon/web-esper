function addRow(tableID){
    var table = document.getElementById(tableID);
    var rowCount = table.rows.length;
    var newRow = table.insertRow();

    var oCell = newRow.insertCell(0);
    oCell.innerHTML = "<input type='checkbox' name='chk'/>";

    oCell = newRow.insertCell(1);
    var id = "listProp" + rowCount + ".key";
    var arrayId = "listProp[" + rowCount + "].key";
    oCell.innerHTML = '<input type="text" id="' + id + '" name="' + arrayId + '"/>';

    oCell = newRow.insertCell(2);
    id = "listProp" + rowCount + ".value";
    arrayId = "listProp[" + rowCount + "].value";
    oCell.innerHTML = '<select id="' + id + '" name="' + arrayId + '">' +
    '<option value="String">String</option>' +
    '<option value="Integer">Integer</option>' +
    '<option value="Long">Long</option>' +
    '<option value="Boolean">Boolean</option>' +
    '</select>';
}

function deleteRow(tableID){
    var table = document.getElementById(tableID);
    var rowCount = table.rows.length;

    for(var i = 0; i < rowCount; i++){
        var row = table.rows[i];
        var chkbox = row.querySelector('input[type=checkbox]');
        if (null != chkbox && true == chkbox.checked) {
            if(rowCount <= 1) {
                break;
            }
            table.deleteRow(i);
            rowCount--;
            i--;
        }
    }
}
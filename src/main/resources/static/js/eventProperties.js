var allProps;
var i;

function addRow() {
    allProps = $('#p_props');
    i = $('#p_props tr').size() + 1;

    allProps.append('<tr>' +
    '<td><input type="text"/></td>' +
    '<td><select>' +
    '<option value="String">String</option>' +
    '<option value="Integer">Integer</option>' +
    '<option value="Long">Long</option>' +
    '<option value="Boolean">Boolean</option>' +
    '</select></td>' +
    '<td><a href="#" id="remBtn">Remove</a></td></tr>');
    i++;
    return false;
}

//Remove button
$(document).on('click', '#remBtn', function() {
    if (i > 2) {
        $(this).closest('tr').remove();
        i--;
    }
    return false;
});
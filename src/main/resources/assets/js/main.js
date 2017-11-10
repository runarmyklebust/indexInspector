$(function () {

    renderTable();


});


var renderTable = function () {
    $('#resultTable').DataTable({
        "paging": false
    });
};
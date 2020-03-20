var stockHistoryBody = $('#stockHistoryBody');
var tableEle = stockHistoryBody.find('table');
var stockEditHistoryDetailTable;
$(function() {
    $('input[name="stock.stockNo"]').val();

    // Stock Edit History  
    var modalStockHistoryEle = $('#modal-stock-history');
    modalStockHistoryEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

        let stockNo = $('input[name="stock.stockNo"]').val();
        setEditHistoryDetailsData(stockNo)

    }).on('hide.bs.modal', function(e) {
        $('#stockHistoryBody').find('table').dataTable().fnDestroy();
    })

    // Customize Datatable
    $('#table-filter-editHistory-search').keyup(function() {
        var query = regex_escape($(this).val());
        stockEditHistoryDetailTable.search(query, true, false).draw();
    });
    $('#table-filter-editHistory-length').change(function() {
        stockEditHistoryDetailTable.page.len($(this).val()).draw();
    });

})

function regex_escape(text) {
    return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
}

function setEditHistoryDetailsData(stockNo) {
    stockEditHistoryDetailTable = tableEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ordering": false,
        "ajax": {
            url: myContextPath + "/stock/edit/history",
            data: function(data) {
                data.stockNo = stockNo;
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "details-control"
        }, {
            targets: 0,
            "data": "displayName",

        }, {
            targets: 1,
            "className": "details-control",
            "data": "originalValue",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "newValue"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "createdDate"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "createdBy",

        }]
    })
}

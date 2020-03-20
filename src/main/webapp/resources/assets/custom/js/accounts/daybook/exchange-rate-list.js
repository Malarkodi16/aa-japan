$(function() {
    var tableEle = $('#table-list-exchange-rate');
    var table = tableEle.DataTable({

         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/exchange/list-page/data-source",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            "visible": false,
            className: 'select-checkbox',
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "createdDate"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "createdBy"
        }]
    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/\s+/g, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //expand details
    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            row.child(format(row.data())).show();
            tr.addClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        }
    });
});

//Exchange Rate list expand details format
function format(rowData) {
    var element = $('#exchange-rate-format>.exchange-rate-detail-view').clone();
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    if (rowData.groupItems.length == 0) {
        $(element).addClass('hide');
    } else {

        for (var i = 0; i < rowData.groupItems.length; i++) {
            var row = $(rowClone).clone();
            $(row).find('td.s-no>span').html(i + 1);
            $(row).find('td.currency').html(rowData.groupItems[i].currency + " - " + rowData.groupItems[i].symbol)
            $(row).find('td.exchangeRate').html(ifNotValid(rowData.groupItems[i].exchangeRate, ''));
            $(row).find('td.salesExchangeRate').html(ifNotValid(rowData.groupItems[i].salesExchangeRate, ''));
            $(row).find('td.specialExchangeRate').html(ifNotValid(rowData.groupItems[i].specialExchangeRate, ''));
            $(row).removeClass('hide');
            $(element).find('table>tbody').append(row);

        }
    }

    return element;
}

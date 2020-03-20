$(function() {
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    /* Sales order invoice list Data-table */

    var tableEle = $('#table-branch-salesOrder-list');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": myContextPath + "/accounts/branch-salesOrdered/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className": "details-control",
            "data": "fullName"
        }, {
            targets: 1,
            "className": "details-control",
            "data": "companyName"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "email"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "mobileNo",
        }, {
            targets: 4,
            "className": "details-control",
            "data": "country"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "port"
        }, {
            targets: 6,
            "data": "invoiceNo",
            "className": 'align-center',
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="' + myContextPath + '/download/branch/sales/invoice/' + row.invoiceNo + '.pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-download"></i></a>'
                return html;
            }
        }]

    });

    /* table on click to append sales invoice */

    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

});

/* function to format invoice details */

function format(rowData) {
    var element = $('#clone-container>#branch-sales-order>.clone-element').clone();
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.salesDetails.length; i++) {
        var row = $(rowClone).clone();

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo>span').html(ifNotValid(rowData.salesDetails[i].stockNo, ''));
        $(row).find('td.stockNo>input').val(ifNotValid(rowData.salesDetails[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.salesDetails[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.salesDetails[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.salesDetails[i].model, ''));
        $(row).find('td.total>span').html(ifNotValid(rowData.salesDetails[i].total, ''));
        $(row).find('td.salesDate').html(ifNotValid(moment(rowData.salesDetails[i].salesDate,"ddd MMM DD HH:mm:ss z YYYY").format("DD-MM-YYYY"), ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}

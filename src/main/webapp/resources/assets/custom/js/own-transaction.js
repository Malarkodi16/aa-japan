$(function() {
    var table = $('#table-ownttAllocation').DataTable({
        "dom": '<<t>ip>',
    	"pageLength" : 25,
        "ajax": myContextPath + "/sales/own-tt-allocation-dataSource",
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
            "data": "remitDate",

        }, {
            targets: 1,
            "className": "details-control",
            "data": "remitter",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "bank",

        }, {
            targets: 3,
            "className": "details-control",
            "data": "amount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currency + ' ">' + data + '</span>';

            }

        }, {
            targets: 4,
            "className": "details-control",
            "data": "remarks",

        }],
        "drawCallback": function(settings, json) {
            $('#table-ownttAllocation').find('span.autonumber').autoNumeric('init')
        }

    });
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
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });
    $('span.autonumber').autoNumeric('init')
    function format(rowData) {

        var ifNotValid = function(val, str) {
            return typeof val === 'undefined' || val == null ? str : val;
        }
        var tbody = '';
        for (var i = 0; i < rowData.items.length; i++) {
            tbody += '<tr><input type="hidden" name="dayBookTransId" value="' + rowData.items[i].dayBookTransId + '"/>' + '<td>' + (i + 1) + '</td><td class="align-center"><input type="hidden" name="stockNo" value="' + ifNotValid(rowData.items[i].stockNo, '') + '"/>' + ifNotValid(rowData.items[i].stockNo, '') + '</td><td class="align-center">' + ifNotValid(rowData.items[i].invoiceNo, '') + '</td><td class="align-center">' + ifNotValid(rowData.items[i].chassisNo, '') + '</td><td class="align-center">' + ifNotValid(rowData.items[i].firstName, '') + '</td><td class="align-center">' + rowData.items[i].currency +' '+ ifNotValid(rowData.items[i].amount, '') + '</td><td class="align-center">' + '<button type="button" id="delete-dayBooktransaction" class="ml-5 btn btn-danger btn-xs"><i class="fa fa-close"></i>'
        }

        var html = '<div class="box-body no-padding bg-darkgray"><div class="order-item-container"><input type="hidden" name="dayBookId" value="' + rowData.id + '"/>' + '    <table class="table table-bordered">' + '<thead><tr><th class="align-center bg-ghostwhite" style="width: 10px">#</th><th style="width: 60px" class="align-center bg-ghostwhite">Stock No</th><th style="width: 100px" class="align-center bg-ghostwhite">Invoice No</th><th style="width: 100px" class="align-center bg-ghostwhite">Chasis No</th><th style="width: 100px" class="align-center bg-ghostwhite">Customer</th><th style="width: 100px" class="align-center bg-ghostwhite">Amount</th><th style="width: 100px" class="align-center bg-ghostwhite">Action</th></tr></thead>' + '<tbody>' + tbody + '</tbody>' + '</table>' + '</div>' + '</div>';
        console.log(html);
        return html;
    }

    $('#table-ownttAllocation').on('click', '#delete-dayBooktransaction', function() {
        var dayBookTransId = $(this).closest('tr').find('input[name="dayBookTransId"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            //data: JSON.stringify(itemId),
            url: myContextPath + "/sales/dayBookTransaction/delete?dayBookTransId=" + dayBookTransId,
            contentType: "application/json",
            success: function(data) {
                console.log("deleted successfully");
                table.ajax.reload();
            }
        });
    });

    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
})
